package com.reputationoverflow.repository

import androidx.lifecycle.MutableLiveData
import com.reputationoverflow.network.StackExchangeApiService
import com.reputationoverflow.session.SessionDao
import com.reputationoverflow.session.SessionDatabase
import com.reputationoverflow.session.SessionEntity
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtensionContext

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoryTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    val mockApiService = mockk<StackExchangeApiService>()
    val mockSessionDb = mockk<SessionDatabase>()
    val mockDao = mockk<SessionDao>()
    val mockSession = MutableLiveData<SessionEntity?>()

    @Before
    fun beforeAll(context: ExtensionContext?) {
        // Set Coroutine Dispatcher.
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
        // Reset Coroutine Dispatcher and Scope.
        testDispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }

    lateinit var subject: Repository

    @BeforeEach
    fun setup() {
        clearAllMocks()
        every { mockSessionDb.sessionDao } returns mockDao
        every { mockDao.getSession() } returns mockSession

        subject = Repository(mockSessionDb, mockApiService)
    }

    @Nested
    inner class logout {
        @Test
        fun `Given no session available, When calling logout, Then nothing should be called`() {
            testScope.launch {
                mockSession.postValue(null)
                subject.logout()
                coVerify {
                    mockApiService.deAuthenticate(any()) wasNot called
                }
            }
        }

        @Test
        fun `Given a session available, When calling logout, Then api deAuthenticate should be called`() {
            testScope.launch {
                mockSession.postValue(SessionEntity("abcde"))
                subject.logout()

                coVerify {
                    mockApiService.deAuthenticate("abcde")
                }
            }
        }

        @Test
        fun `Given a session available, When calling logout, Then db deleteAll should be called`() {
            testScope.launch {
                mockSession.postValue(SessionEntity("abcde"))
                subject.logout()

                coVerify {
                    mockDao.deleteAll()
                }
            }
        }
    }

    @Nested
    inner class getReputation {
        @Test
        fun `Given nothing, When calling getReputation, Then api should be called`() {
            testScope.launch {
                subject.getReputation()

                coVerify {
                    mockApiService.getMyReputation()
                }
            }
        }
    }
}