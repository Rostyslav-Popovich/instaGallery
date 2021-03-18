package com.example.myapplication.data.api.media

import com.example.myapplication.data.model.Gallery
import com.example.myapplication.data.model.Token
import retrofit2.http.*

interface ApiServiceMedia {

    @GET("me/media")
    suspend fun getMediaList(@Query("access_token") token:String,
                     @Query("fields") field:String,
                     @Query("after") after:String): Gallery

}