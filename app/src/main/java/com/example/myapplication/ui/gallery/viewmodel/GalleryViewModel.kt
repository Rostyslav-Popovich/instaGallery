package com.example.myapplication.ui.gallery.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.model.Data
import com.example.myapplication.data.model.Error
import com.example.myapplication.data.model.Paging
import com.example.myapplication.data.repository.MediaRepository
import com.example.myapplication.ui.base.BaseViewModel
import com.example.myapplication.utils.Status
import com.google.gson.Gson
import retrofit2.HttpException

class GalleryViewModel(private val mediaRepository: MediaRepository) : BaseViewModel() {

    val statusLiveData = MutableLiveData<Status>()
    val successLiveData = MutableLiveData<List<Data>>()
    val pagingLiveData = MutableLiveData<Paging>()
    val errorLiveData = MutableLiveData<Error>()

    fun getGallery(
        token: String,
        after: String
    ) {
        statusLiveData.value = Status.LOADING
        launch(
            {
                when (it) {
                    is HttpException -> {
                        val errorResponse = convertErrorBody(it)
                        errorLiveData.postValue(errorResponse!!)
                        statusLiveData.postValue(Status.ERROR)
                    }
                }
            },
            null,

            {
                val gallery = mediaRepository.getMediaList(
                    token,
                    after
                )

                successLiveData.postValue(
                    getFilteredList(
                        gallery.data
                    )
                )
                pagingLiveData.postValue(gallery.paging)
                statusLiveData.postValue(Status.SUCCESS)
            }
        )
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