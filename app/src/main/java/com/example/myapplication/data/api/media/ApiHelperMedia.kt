package com.example.myapplication.data.api.media

class ApiHelperMedia(private val apiServiceMedia: ApiServiceMedia) {

    suspend fun getMediaList(
        token: String,
        after: String
    ) = apiServiceMedia.getMediaList(
        token,
        "10",
        "id,media_type,media_url,children{media_url,media_type}",
        after
    )
}
