package com.example.myapplication.data.repository

import com.example.myapplication.data.api.media.ApiHelperMedia
import com.example.myapplication.data.model.Gallery
import kotlinx.coroutines.flow.*

class MediaRepositoryImpl(private val apiHelperMedia: ApiHelperMedia) : MediaRepository {

    override suspend fun getMediaList(
        token: String,
        after: String
    ): Flow<Gallery> = flow{
        emit(apiHelperMedia.getMediaList(token, after))
    }
}
