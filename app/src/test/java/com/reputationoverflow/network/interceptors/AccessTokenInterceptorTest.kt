package com.reputationoverflow.network.interceptors

import androidx.lifecycle.MutableLiveData
import com.reputationoverflow.session.SessionEntity
import com.reputationoverflow.util.InstantExecutorExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(InstantExecutorExtension::class)
class AccessTokenInterceptorTest {
    private val mockWebServer = MockWebServer()
    private val mockSessionData = MutableLiveData<SessionEntity?>()

    private lateinit var subject: AccessTokenInterceptor

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
        mockWebServer.enqueue(MockResponse())
        subject = AccessTokenInterceptor(mockSessionData)
    }

    @Test
    fun `Given no access token, When a new request comes in, Then no access_token key gets injected`() {
        mockSessionData.postValue(null)

        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(subject).build()
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()
        val request = mockWebServer.takeRequest()

        val keyValue = request.requestUrl?.queryParameter("access_token")
        Assertions.assertNull(keyValue)
    }

    @Test
    fun `Given a access token, When a new request comes in, Then access_token key gets injected`() {
        mockSessionData.postValue(SessionEntity(token = "ABCDEFG"))

        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(subject).build()
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()
        val request = mockWebServer.takeRequest()

        val keyValue = request.requestUrl?.queryParameter("access_token")
        Assertions.assertEquals("ABCDEFG", keyValue)
    }
}