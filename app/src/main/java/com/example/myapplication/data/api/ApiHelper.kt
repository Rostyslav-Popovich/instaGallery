package com.example.myapplication.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun getToken(clientId: Long,
                         client_secret: String,
                         grant_type: String,
                         redirect_uri: String,
                         code: String) = apiService.getToken(clientId, client_secret, grant_type, redirect_uri, code)
}
