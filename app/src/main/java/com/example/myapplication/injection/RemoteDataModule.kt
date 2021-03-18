package com.example.myapplication.injection

import com.example.myapplication.data.api.login.ApiHelperLogin
import com.example.myapplication.data.api.RetrofitBuilder
import com.example.myapplication.data.api.media.ApiHelperMedia
import org.koin.dsl.module

val remoteDataModule = module {

    single { ApiHelperLogin(get()) }
    single { ApiHelperMedia(get()) }
    single {RetrofitBuilder.apiServiceLogin}
    single {RetrofitBuilder.apiServiceMedia}

}