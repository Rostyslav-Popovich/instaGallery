package com.example.myapplication.data.api

import com.example.myapplication.data.api.login.ApiServiceLogin
import com.example.myapplication.data.api.media.ApiServiceMedia
import com.example.myapplication.utils.Const.Companion.AUTH_URL
import com.example.myapplication.utils.Const.Companion.MEDIA_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private fun getRetrofitAuth(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
    }

    private fun getRetrofitMedia(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MEDIA_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val apiServiceLogin: ApiServiceLogin = getRetrofitAuth().create(ApiServiceLogin::class.java)
    val apiServiceMedia: ApiServiceMedia = getRetrofitMedia().create(ApiServiceMedia::class.java)
}
