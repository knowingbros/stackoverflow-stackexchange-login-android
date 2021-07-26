package com.reputationoverflow.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.reputationoverflow.logger.Logger
import com.reputationoverflow.network.StackExchangeApiService
import com.reputationoverflow.session.SessionDatabase
import com.reputationoverflow.session.SessionEntity
import com.reputationoverflow.session.SessionUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val sessionDatabase: SessionDatabase,
    private val apiService: StackExchangeApiService
) {
    val session: LiveData<SessionEntity?> = sessionDatabase.sessionDao.getSession()
    val isLogged: LiveData<Boolean> = Transformations.map(session) { input -> input != null }

    suspend fun setSession(sessionReply: SessionUtil.AuthResponse.AuthSuccess) {
        withContext(Dispatchers.IO) {
            sessionDatabase.sessionDao.deleteAll()
            sessionDatabase.sessionDao.insert(
                SessionEntity(
                    token = sessionReply.token,
                    expires = sessionReply.expires
                )
            )
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            val sessionValue = session.value
            if (sessionValue != null) {
                val result = apiService.deAuthenticate(sessionValue.token)
                Logger.log(result)
                sessionDatabase.sessionDao.deleteAll()
            }
        }
    }

    suspend fun getReputation() {
        withContext(Dispatchers.IO) {
            val result = apiService.getMyReputation()
            Logger.log("NICERESULT: $result")
        }
    }
}