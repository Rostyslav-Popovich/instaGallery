package com.example.myapplication.data.repository

import com.example.myapplication.data.api.media.ApiHelperMedia

class MediaRepositoryImpl(private val apiHelperMedia: ApiHelperMedia) : MediaRepository {

    override suspend fun getMediaList(
        token: String,
        after: String
    ) = apiHelperMedia.getMediaList(token, after)
}
