package com.reputationoverflow.utils

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineScope

fun ViewModel.getViewModelScope(coroutineScope: CoroutineScope?) =
    coroutineScope ?: this.viewModelScope

// Need to do that to be able to test the viewModel
@Module
@InstallIn(ActivityComponent::class)
object CoroutineModel {
    @Provides
    fun provideViewScopeModel(): CoroutineScope? = null
}