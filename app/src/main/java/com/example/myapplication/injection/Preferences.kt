package com.example.myapplication.injection

import android.content.SharedPreferences

interface Preferences {
    fun provideSecurePreferences(): SharedPreferences
}