package com.reputationoverflow.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class StackExchangeApiServiceTest {
    private val mockWebServer = MockWebServer()
    private lateinit var subject: StackExchangeApiService

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupAll() {
        mockWebServer.start()
    }

    @After
    fun afterAll() {
        mockWebServer.shutdown()
    }

    @BeforeEach
    fun setup() {
        subject = provideRetrofit(mockWebServer.url("/").toString(), arrayOf())
        mockWebServer.enqueue(MockResponse())
    }

    @Test
    fun `Given nothing, When calling deAuthenticate, it should call the correct URL`() {
        runBlocking {
            subject.deAuthenticate("abcde")
            val request: RecordedRequest = mockWebServer.takeRequest()
            assertEquals("/apps/abcde/de-authenticate", request.path)
        }
    }

    @Test
    fun `Given nothing, When calling getMyReputation, it should call the correct URL`() {
        runBlocking {
            subject.getMyReputation()
            val request: RecordedRequest = mockWebServer.takeRequest()
            assertEquals("/me/reputation?site=stackoverflow", request.path)
        }
    }
}