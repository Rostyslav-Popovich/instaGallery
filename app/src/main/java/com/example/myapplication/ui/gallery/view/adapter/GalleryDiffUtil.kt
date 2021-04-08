package com.example.myapplication.ui.gallery.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.data.model.Data

class GalleryDiffUtil(private val oldData: List<Data>, private val newData: List<Data>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData[oldItemPosition]
        val newItem = newData[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData[oldItemPosition]
        val newItem = newData[newItemPosition]
        return oldItem.media_url.equals(newItem.media_url)
    }
}