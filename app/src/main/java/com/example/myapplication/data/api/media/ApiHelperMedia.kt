package com.example.myapplication.data.api.media

class ApiHelperMedia(private val apiServiceMedia: ApiServiceMedia) {

    suspend fun getMediaList(token:String,
                     field:String,
                     after:String)=apiServiceMedia.getMediaList(token, field, after)
}
