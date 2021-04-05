package com.example.myapplication.injection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.BuildConfig

class PreferencesImpl(private val application: Application) : Preferences {
    override fun provideSecurePreferences(): SharedPreferences =
        application.getSharedPreferences(BuildConfig.SECURE_PREFS_FILE_KEY, Context.MODE_PRIVATE)
}