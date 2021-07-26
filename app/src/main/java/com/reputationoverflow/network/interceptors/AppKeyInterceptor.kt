package com.reputationoverflow.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AppKeyInterceptor(private val appKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newUrl = originalRequest
            .url.newBuilder()
            .addQueryParameter("key", appKey)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }
}