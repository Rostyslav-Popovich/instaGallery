package com.example.myapplication.injection

import com.example.myapplication.data.repository.MainRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { MainRepository(get()) }
}