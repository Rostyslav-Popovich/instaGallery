package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Gallery

interface MediaRepository {
    suspend fun getMediaList(
        token: String,
        after: String
    ): Gallery
}
