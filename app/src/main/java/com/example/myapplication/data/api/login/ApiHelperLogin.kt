package com.example.myapplication.data.api.login

class ApiHelperLogin(private val apiServiceLogin: ApiServiceLogin) {
    suspend fun getToken(clientId: Long,
                         client_secret: String,
                         grant_type: String,
                         redirect_uri: String,
                         code: String) = apiServiceLogin.getToken(clientId, client_secret, grant_type, redirect_uri, code)
}
