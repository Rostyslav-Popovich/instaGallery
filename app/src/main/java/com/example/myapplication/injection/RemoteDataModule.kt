package com.example.myapplication.injection

import com.example.myapplication.data.api.ApiHelper
import com.example.myapplication.data.api.RetrofitBuilder
import org.koin.dsl.module

val remoteDataModule = module {

    single {ApiHelper(get())}
    single {RetrofitBuilder.apiService}

}