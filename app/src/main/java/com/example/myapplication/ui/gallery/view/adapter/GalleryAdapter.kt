package com.example.myapplication.ui.gallery.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.model.Data
import com.example.myapplication.databinding.ItemGalleryBinding

class GalleryAdapter(
    private val data: ArrayList<Data>,
    onClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<GalleryAdapter.DataViewHolder>() {

    private val onItemClickListener = onClickListener

    inner class DataViewHolder(binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            ItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        Glide.with(ItemGalleryBinding.bind(holder.itemView).image.context)
            .load(data[position].media_url)
            .into(ItemGalleryBinding.bind(holder.itemView).image)
        ViewCompat.setTransitionName(
            ItemGalleryBinding.bind(holder.itemView).image,
            data[position].id
        )

        holder.itemView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && ItemGalleryBinding.bind(holder.itemView).motionLayout.progress == 0f) {
                onItemClickListener.onItemClick(
                    data[position],
                    ItemGalleryBinding.bind(holder.itemView).image
                )
            }
            false
        }
    }

    fun addMedia(data: List<Data>) {
        this.data.apply {
            addAll(data)
        }
    }

    fun getMedia(): List<Data> {
        return this.data
    }

    fun clearMedia() {
        this.data.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(data: Data, imageView: ImageView)
    }
}