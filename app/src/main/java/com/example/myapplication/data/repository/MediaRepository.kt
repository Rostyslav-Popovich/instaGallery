package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Gallery
import kotlinx.coroutines.flow.*

interface MediaRepository {
    suspend fun getMediaList(
        token: String,
        after: String
    ): Flow<Gallery>
}
