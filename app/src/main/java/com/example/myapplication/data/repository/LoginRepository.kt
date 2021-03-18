package com.example.myapplication.data.repository

import com.example.myapplication.data.api.login.ApiHelperLogin

class LoginRepository(private val apiHelperLogin: ApiHelperLogin) {

    suspend fun getToken (clientId: Long,
                          client_secret: String,
                          grant_type: String,
                          redirect_uri: String,
                          code: String) = apiHelperLogin.getToken(clientId, client_secret, grant_type, redirect_uri, code)

}
