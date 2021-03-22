package com.example.myapplication.ui.login.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Token
import com.example.myapplication.data.repository.LoginRepository
import com.example.myapplication.utils.Const
import com.example.myapplication.utils.Resource
import com.example.myapplication.utils.Status
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val liveData = MutableLiveData<Resource<Token>>()

    fun getToken(
        clientId: Long,
        client_secret: String,
        grant_type: String,
        redirect_uri: String,
        code: String
    ) {
        Resource.loading(data = null)
        try {
            liveData.value = Resource(Status.LOADING, null, null)
            viewModelScope.launch {
                liveData.value = (Resource.success(
                    data = loginRepository.getToken(
                        clientId,
                        client_secret,
                        grant_type,
                        redirect_uri,
                        code
                    )
                ))
                val editor = sharedPreferences.edit()
                editor.putString(
                    Const.APP_PREFS_TOKEN,
                    (liveData.value as Resource<Token>).data?.access_token
                )
                editor.apply()
            }

        } catch (exception: Exception) {
            liveData.postValue(
                Resource.error(
                    data = null,
                    message = exception.message ?: "Error Occurred!"
                )
            )
        }
    }
}