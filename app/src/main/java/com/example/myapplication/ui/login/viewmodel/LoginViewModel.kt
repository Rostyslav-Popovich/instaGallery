package com.example.myapplication.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.myapplication.data.repository.LoginRepository
import com.example.myapplication.utils.Resource
import kotlinx.coroutines.Dispatchers

class LoginViewModel (private val loginRepository: LoginRepository) : ViewModel() {

    fun getToken(clientId: Long,
                 client_secret: String,
                 grant_type: String,
                 redirect_uri: String,
                 code: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.getToken(clientId, client_secret, grant_type, redirect_uri, code)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}