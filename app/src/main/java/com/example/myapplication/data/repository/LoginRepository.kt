package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Token

interface LoginRepository {

    suspend fun getToken(
        clientId: Long,
        client_secret: String,
        grant_type: String,
        redirect_uri: String,
        code: String
    ): Token

}
