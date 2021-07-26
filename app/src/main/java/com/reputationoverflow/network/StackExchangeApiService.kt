package com.reputationoverflow.network

import retrofit2.http.GET
import retrofit2.http.Path

interface StackExchangeApiService {
    @GET("apps/{accessToken}/de-authenticate")
    suspend fun deAuthenticate(@Path("accessToken") token: String): String

    @GET("/me?order=desc&sort=reputation&site=stackoverflow")
    suspend fun getMyReputation(): String
}

