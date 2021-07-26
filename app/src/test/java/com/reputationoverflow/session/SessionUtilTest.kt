package com.reputationoverflow.session

import android.content.Intent
import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionUtilTest {
    @Nested
    inner class fromUri {
        val mockUri = mockk<Uri>()

        @Test
        fun `Given the fromUri, When calling the method with empty fragment, Then should return null`() {
            every { mockUri.fragment } returns null
            val result = SessionUtil.fromUri(mockUri)

            assertNull(result)
        }

        @Test
        fun `Given the fromUri, When a correct fragment is used, Then the auth parameters should be available `() {
            every { mockUri.fragment } returns "access_token=abcd&expires=1234"

            val result = SessionUtil.fromUri(mockUri)
            val success = result!!.getSuccess()

            assertNotNull(success)
            success?.let {
                assertEquals("abcd", success.token)
                assertEquals(1234, success.expires)
            }
        }

        @Test
        fun `Given the fromUri, When a correct fragment is used, Then it should consider as authorized `() {
            every { mockUri.fragment } returns "access_token=abcd&expires=1234"
            val result = SessionUtil.fromUri(mockUri)
            assertTrue(result!!.isAuthorized())
        }

        @Test
        fun `Given a successful authentication, When access_token param is not in fragment, Then correct error should throw `() {
            every { mockUri.fragment } returns "ACCESS_TOKEN=abcd&expires=1234"

            val exception = assertThrows(IllegalStateException::class.java) {
                SessionUtil.fromUri(mockUri)
            }

            // Since it does not consider it as authorized
            assertEquals("No valid error_description param has been provided", exception.message)
        }

        @Test
        fun `Given an unsuccessful authentication, When a correct fragment is used, Then the auth error should be available `() {
            every { mockUri.fragment } returns "error=access_denied&error_description=abcde"

            val result = SessionUtil.fromUri(mockUri)
            val error = result?.getError()

            assertNotNull(error)
            error?.let {
                assertEquals("abcde", error.description)
            }
        }

        @Test
        fun `Given an unsuccessful authentication, When a correct fragment is used, Then it should consider as not authorized `() {
            every { mockUri.fragment } returns "error=access_denied&error_description=abcde"

            val result = SessionUtil.fromUri(mockUri)
            assertFalse(result!!.isAuthorized())
        }

        @Test
        fun `Given an unsuccessful authentication, When error_description param is not in fragment, Then correct error should throw`() {
            every { mockUri.fragment } returns "error=access_denied&EROURHEHURHU=abcde"

            val exception = assertThrows(IllegalStateException::class.java) {
                SessionUtil.fromUri(mockUri)
            }

            assertEquals("No valid error_description param has been provided", exception.message)
        }
    }
}