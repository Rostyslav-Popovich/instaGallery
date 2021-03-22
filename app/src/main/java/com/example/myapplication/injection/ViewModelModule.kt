package com.example.myapplication.injection

import com.example.myapplication.ui.gallery.viewmodel.GalleryViewModel
import com.example.myapplication.ui.login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(),get()) }
    viewModel { GalleryViewModel(get()) }
}