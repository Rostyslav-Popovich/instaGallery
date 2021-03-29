package com.example.myapplication.ui.login.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.model.Token
import com.example.myapplication.data.repository.LoginRepositoryImpl
import com.example.myapplication.ui.base.BaseViewModel
import com.example.myapplication.utils.Const
import com.example.myapplication.utils.Resource
import com.example.myapplication.utils.Status

class LoginViewModel(
    private val loginRepository: LoginRepositoryImpl,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel() {

    val liveData = MutableLiveData<Resource<Token>>()

    fun getToken(
        clientId: Long,
        client_secret: String,
        grant_type: String,
        redirect_uri: String,
        code: String
    ) {
        liveData.value = Resource(Status.LOADING, null, null)
        launch(
            {
                liveData.postValue(
                    Resource.error(
                        data = null,
                        message = it.message ?: "Error Occurred!"
                    )
                )
            },
            null,

            {
                val token = loginRepository.getToken(
                    clientId,
                    client_secret,
                    grant_type,
                    redirect_uri,
                    code
                )
                val editor = sharedPreferences.edit()
                editor.putString(Const.APP_PREFS_TOKEN, token.access_token)
                editor.apply()

                liveData.postValue(Resource.success(data = token))
            }

        )

    }
}