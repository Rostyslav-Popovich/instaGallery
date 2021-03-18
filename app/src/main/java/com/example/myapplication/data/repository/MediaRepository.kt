package com.example.myapplication.data.repository

import com.example.myapplication.data.api.media.ApiHelperMedia

class MediaRepository(private val apiHelperMedia: ApiHelperMedia) {

    suspend fun getMediaList(
        token: String,
        field: String,
        after: String
    ) = apiHelperMedia.getMediaList(token, field, after)
}
