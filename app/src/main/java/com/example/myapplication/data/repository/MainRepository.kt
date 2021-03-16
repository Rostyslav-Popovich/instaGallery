package com.example.myapplication.data.repository

import com.example.myapplication.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getToken (clientId: Long,
                          client_secret: String,
                          grant_type: String,
                          redirect_uri: String,
                          code: String) = apiHelper.getToken(clientId, client_secret, grant_type, redirect_uri, code)
}
