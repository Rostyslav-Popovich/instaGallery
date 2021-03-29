package com.example.myapplication.ui.gallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.myapplication.data.model.Data
import com.example.myapplication.data.model.Error
import com.example.myapplication.data.repository.MediaRepositoryImpl
import com.example.myapplication.utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class GalleryViewModel(private val mediaRepository: MediaRepositoryImpl) : ViewModel() {

    fun getGallery(
        token: String,
        field: String,
        after: String
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = getFilteredList(
                        mediaRepository.getMediaList(
                            token,
                            field,
                            after
                        ).data
                    )
                )
            )
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val errorResponse = convertErrorBody(throwable)
                    emit(
                        Resource.error(
                            data = errorResponse,
                            message = throwable.message ?: "Error Occurred!"
                        )
                    )
                }
            }

        }
    }

    private fun convertErrorBody(throwable: HttpException): Error? {
        return try {
            throwable.response()?.errorBody()?.string().let {
                return Gson().fromJson(it.toString(), Error::class.java)
            }
        } catch (exception: Exception) {
            null
        }
    }

    private fun getFilteredList(list: List<Data>?): List<Data> {
        return if (list.isNullOrEmpty())
            emptyList()
        else {
            val filterList = arrayListOf<Data>()

            val mediaList =
                list.filter { (it.media_type == "IMAGE" || it.media_type == "CAROUSEL_ALBUM") }
            for (item in mediaList) {
                if (item.media_type == "IMAGE")
                    filterList.add(item)
                else {
                    item.children!!.data.filter { it.media_type == "IMAGE" || it.media_type == "CAROUSEL_ALBUM" }
                        .let {
                            filterList.addAll(it)
                        }
                }
            }
            filterList
        }
    }
}