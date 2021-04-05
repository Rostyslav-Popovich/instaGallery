package com.example.myapplication.injection

import com.example.myapplication.data.api.RetrofitBuilder
import com.example.myapplication.data.api.login.ApiHelperLogin
import com.example.myapplication.data.api.media.ApiHelperMedia
import com.example.myapplication.data.repository.LoginRepository
import com.example.myapplication.data.repository.LoginRepositoryImpl
import com.example.myapplication.data.repository.MediaRepository
import com.example.myapplication.data.repository.MediaRepositoryImpl
import com.example.myapplication.ui.gallery.viewmodel.GalleryViewModel
import com.example.myapplication.ui.login.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

class InjectionModule {

    val preferencesModule = module {
        single { PreferencesImpl(androidApplication()).provideSecurePreferences() } bind Preferences::class
    }

    val remoteDataModule = module {
        single { ApiHelperLogin(get()) }
        single { ApiHelperMedia(get()) }
        single { RetrofitBuilder.apiServiceLogin }
        single { RetrofitBuilder.apiServiceMedia }
    }

    val repositoryModule = module {
        single { LoginRepositoryImpl(get()) } bind LoginRepository::class
        single { MediaRepositoryImpl(get()) } bind MediaRepository::class
    }

    val viewModelModule = module {
        viewModel { LoginViewModel(get(), get()) }
        viewModel { GalleryViewModel(get()) }
    }
}