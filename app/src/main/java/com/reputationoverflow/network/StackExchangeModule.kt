package com.reputationoverflow.network

import com.reputationoverflow.BuildConfig
import com.reputationoverflow.logger.Logger
import com.reputationoverflow.network.interceptors.AccessTokenInterceptor
import com.reputationoverflow.network.interceptors.AppKeyInterceptor
import com.reputationoverflow.session.SessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


private const val BASE_URL = "https://api.stackexchange.com/2.3/"
private const val KEY = BuildConfig.STACK_EXCHANGE_KEY

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        sessionDao: SessionDao
    ): StackExchangeApiService {
        val logRequests = Interceptor { chain ->
            Logger.log(chain.request().url.toString())
            chain.proceed(chain.request())
        }
        return provideRetrofit(
            BASE_URL, arrayOf(
                AppKeyInterceptor(KEY),
                AccessTokenInterceptor(sessionDao.getSession()),
                logRequests
            )
        )
    }
}

fun provideRetrofit(
    url: String,
    interceptors: Array<Interceptor>
): StackExchangeApiService {

    val okHttpClient = OkHttpClient().newBuilder()

    if (BuildConfig.DEBUG) {
        okHttpClient.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
    }

    okHttpClient.build()

    interceptors.forEach {
        okHttpClient.addInterceptor(it)
    }

    /* val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build() */

    return Retrofit.Builder()
        .client(okHttpClient.build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(url)
        .build()
        .create(StackExchangeApiService::class.java)
}