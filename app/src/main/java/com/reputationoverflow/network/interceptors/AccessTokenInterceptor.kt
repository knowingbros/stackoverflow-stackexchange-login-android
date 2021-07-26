package com.reputationoverflow.network.interceptors

import androidx.lifecycle.LiveData
import com.reputationoverflow.session.SessionEntity
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor constructor(
    sessionData: LiveData<SessionEntity?>
) : Interceptor {
    private var token: String? = null

    init {
        sessionData.observeForever {
            token = it?.token
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        return if (token.isNullOrEmpty()) {
            chain.proceed(originalRequest)
        } else {
            val newRequest = originalRequest.newBuilder()
                .url(
                    originalRequest
                        .url.newBuilder()
                        .addQueryParameter("access_token", token)
                        .build()
                )
                .build()
            chain.proceed(newRequest)
        }
    }
}