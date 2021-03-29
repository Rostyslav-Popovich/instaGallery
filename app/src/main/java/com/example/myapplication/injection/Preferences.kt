package com.example.myapplication.injection

import android.app.Application
import android.content.SharedPreferences

interface Preferences {
    fun provideSecurePreferences(app: Application): SharedPreferences
}