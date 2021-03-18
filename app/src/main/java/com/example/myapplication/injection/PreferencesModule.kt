package com.example.myapplication.injection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val preferencesModule = module {
    single { provideSecurePreferences(androidApplication()) }
}

private val SECURE_PREFS_FILE_KEY = "com.example.myapplication.secure_preferences"

private fun provideSecurePreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(SECURE_PREFS_FILE_KEY, Context.MODE_PRIVATE)
