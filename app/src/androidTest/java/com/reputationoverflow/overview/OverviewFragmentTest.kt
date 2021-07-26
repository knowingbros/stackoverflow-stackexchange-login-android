package com.reputationoverflow.overview

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.reputationoverflow.HiltTestActivity
import com.reputationoverflow.MainActivity
import com.reputationoverflow.R
import com.reputationoverflow.hilt.launchFragmentInHiltContainer
import com.reputationoverflow.logger.Logger
import com.reputationoverflow.session.SessionUtil
import com.reputationoverflow.setVisibility
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.*
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

// TODO: Make this test work
@Ignore("Ignored because can't make the viewModel injection to work")
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class OverviewFragmentTest {
    val hiltRule = HiltAndroidRule(this)
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    @get: Rule
    val testRules = RuleChain
        .outerRule(hiltRule)
        .around(countingTaskExecutorRule)


    val mockSessionUtil = mockkObject(SessionUtil)
    val mockViewModel = mockk<OverviewViewModel>()
    val mockIsLogged = MutableLiveData<Boolean>()

    val bindingAdapter = mockkStatic("com.reputationoverflow.BindingAdapters")

    @BindValue @JvmField
    val viewModel: OverviewViewModel = mockViewModel

    @Before
    fun setup () {
        clearAllMocks()
        every {mockViewModel.isLogged} returns mockIsLogged
        hiltRule.inject()
    }

    @Test
    fun Given_nothing_When_clicking_login_button_Then_login_intent_triggers() {
        Intents.init()
        every { SessionUtil.getAuthIntent() } returns Intent(Intent.ACTION_VIEW, Uri.parse("https://toto"))

        launchFragmentInHiltContainer<OverviewFragment>()

        mockIsLogged.postValue(true)
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)

        onView(withId(R.id.login_button)).perform(click())

        verify {
            SessionUtil.getAuthIntent()
        }
        intended(
            hasAction(Intent.ACTION_VIEW)
        )
        intended(
            hasData("https://toto")
        )

        Intents.release()
    }

    @Test
    fun Given_null_response_When_clicking_logout_button_Then_call_toaster() {
        launchFragmentInHiltContainer<OverviewFragment>()

        mockIsLogged.postValue(true)
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)

        onView(withId(R.id.logout_button)).perform(click())

        verify {
            viewModel.logout()
        }
    }

}