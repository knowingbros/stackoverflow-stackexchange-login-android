package com.reputationoverflow.overview

import androidx.lifecycle.MutableLiveData
import com.reputationoverflow.repository.Repository
import com.reputationoverflow.session.SessionEntity
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtensionContext

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OverviewViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

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

    val mockedRespository = mockk<Repository>()

    @Nested
    inner class logout {

        lateinit var subject: OverviewViewModel

        @BeforeEach
        fun setup() {
            every {mockedRespository.isLogged} returns MutableLiveData<Boolean>()
            every {mockedRespository.session} returns MutableLiveData<SessionEntity>()

            subject = OverviewViewModel(testScope, mockedRespository)
        }

        @Test
        fun `Given the viewModel, When calling logout, Then repository logout should be called`() {
            subject.logout()
            coVerify {
                mockedRespository.logout()
            }
        }
    }
}