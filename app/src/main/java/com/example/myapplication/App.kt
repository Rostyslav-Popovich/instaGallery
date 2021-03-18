package com.example.myapplication

import androidx.multidex.MultiDexApplication
import com.example.myapplication.injection.preferencesModule
import com.example.myapplication.injection.remoteDataModule
import com.example.myapplication.injection.repositoryModule
import com.example.myapplication.injection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                remoteDataModule,
                repositoryModule,
                viewModelModule,
                preferencesModule
            )
        }

    }
}