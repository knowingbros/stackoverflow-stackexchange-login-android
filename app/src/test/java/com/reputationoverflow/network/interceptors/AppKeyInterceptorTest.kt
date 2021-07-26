package com.reputationoverflow.network.interceptors

import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AppKeyInterceptorTest {
    private val mockWebServer = MockWebServer()
    private lateinit var subject: AppKeyInterceptor

    @Before
    fun setupAll() {
        mockWebServer.start()
    }

    @After
    fun afterAll() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Given nothing, When a new request comes in, Then the key parameter should be injected`() {
        mockWebServer.enqueue(MockResponse())
        subject = AppKeyInterceptor("ABCDEFG")

        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(subject).build()
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()
        val request = mockWebServer.takeRequest()

        val keyValue = request.requestUrl?.queryParameter("key")
        assertEquals("ABCDEFG", keyValue)
    }
}