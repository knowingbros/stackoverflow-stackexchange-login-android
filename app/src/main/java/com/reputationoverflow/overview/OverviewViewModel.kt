package com.reputationoverflow.overview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.reputationoverflow.repository.Repository
import com.reputationoverflow.utils.getViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class OverviewViewModel @ViewModelInject constructor(
    coroutineScopeProvider: CoroutineScope?,
    private val repository: Repository
): ViewModel() {

    private val coroutineScope = getViewModelScope(coroutineScopeProvider)

    val isLogged = repository.isLogged
    val session = repository.session

    fun logout() {
        coroutineScope.launch {
            repository.logout()
        }
    }

    fun getReputation() {
        coroutineScope.launch {
            repository.getReputation()
        }
    }
}