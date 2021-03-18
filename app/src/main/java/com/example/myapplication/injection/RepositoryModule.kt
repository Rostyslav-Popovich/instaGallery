package com.example.myapplication.injection

import com.example.myapplication.data.repository.LoginRepository
import com.example.myapplication.data.repository.MediaRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { LoginRepository(get()) }
    single { MediaRepository(get()) }
}