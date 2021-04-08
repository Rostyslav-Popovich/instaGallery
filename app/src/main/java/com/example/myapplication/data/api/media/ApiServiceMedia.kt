package com.example.myapplication.data.api.media

import com.example.myapplication.data.model.Gallery
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceMedia {

    @GET("me/media")
    suspend fun getMediaList(
        @Query("access_token") token: String,
        @Query("limit") limit: String = "10",
        @Query("fields") field: String,
        @Query("after") after: String
    ): Gallery

}