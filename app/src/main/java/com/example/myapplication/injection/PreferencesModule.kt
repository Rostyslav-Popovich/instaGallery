package com.example.myapplication.injection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val preferencesModule = module {
    single { provideSecurePreferences(androidApplication()) }
}

private fun provideSecurePreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(BuildConfig.SECURE_PREFS_FILE_KEY, Context.MODE_PRIVATE)
