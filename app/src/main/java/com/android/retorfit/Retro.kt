package com.android.retorfit

import com.android.test.BuildConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


   class Retro{
companion object {
    val clientR = OkHttpClient().newBuilder()
        .addInterceptor(provideCacheInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(" https://api.github.com")
        .client(clientR)
        .addConverterFactory(GsonConverterFactory.create()).build()

    val calApi = retrofit.create(Apis::class.java)

    private val CACHE_CONTROL = "Cache-Control"//

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl = CacheControl.Builder()
                .maxAge(2, TimeUnit.MINUTES)
                .build()

            response.newBuilder()
                .header(CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }
}

}