package com.example.myapplication.data.api

import com.example.myapplication.data.model.Token
import retrofit2.http.*

interface ApiService {

    @POST("/oauth/access_token")
    @Multipart
    suspend fun getToken(@Part("client_id") clientId: Long,
                         @Part("client_secret") client_secret: String,
                         @Part("grant_type") grant_type: String,
                         @Part("redirect_uri") redirect_uri: String,
                         @Part("code") code: String):Token
}