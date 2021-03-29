package com.example.myapplication.injection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.BuildConfig
import com.example.myapplication.data.api.RetrofitBuilder
import com.example.myapplication.data.api.login.ApiHelperLogin
import com.example.myapplication.data.api.media.ApiHelperMedia
import com.example.myapplication.data.repository.LoginRepositoryImpl
import com.example.myapplication.data.repository.MediaRepositoryImpl
import com.example.myapplication.ui.gallery.viewmodel.GalleryViewModel
import com.example.myapplication.ui.login.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class InjectionModule : Preferences {

    val preferencesModule = module {
        single { provideSecurePreferences(androidApplication()) }
    }

    val remoteDataModule = module {
        single { ApiHelperLogin(get()) }
        single { ApiHelperMedia(get()) }
        single { RetrofitBuilder.apiServiceLogin }
        single { RetrofitBuilder.apiServiceMedia }
    }

    val repositoryModule = module {
        single { LoginRepositoryImpl(get()) }
        single { MediaRepositoryImpl(get()) }
    }

    val viewModelModule = module {
        viewModel { LoginViewModel(get(), get()) }
        viewModel { GalleryViewModel(get()) }
    }

    override fun provideSecurePreferences(app: Application): SharedPreferences =
        app.getSharedPreferences(BuildConfig.SECURE_PREFS_FILE_KEY, Context.MODE_PRIVATE)
}