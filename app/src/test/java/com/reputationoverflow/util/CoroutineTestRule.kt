package com.reputationoverflow.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.ExternalResource

class CoroutineTestRule : ExternalResource() {
    override fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    override fun after() {
        Dispatchers.resetMain()
    }
}