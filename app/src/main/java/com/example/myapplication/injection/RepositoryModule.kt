package com.example.myapplication.injection

import com.example.myapplication.data.repository.LoginRepositoryImpl
import com.example.myapplication.data.repository.MediaRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single { LoginRepositoryImpl(get()) }
    single { MediaRepositoryImpl(get()) }
}