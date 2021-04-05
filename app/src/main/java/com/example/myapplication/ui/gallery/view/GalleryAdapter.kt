package com.example.myapplication.ui.gallery.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.model.Data
import com.example.myapplication.databinding.ItemEmptyBinding
import com.example.myapplication.databinding.ItemGalleryBinding

class GalleryAdapter(private val data: ArrayList<Data>,
                     private val onClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val onItemClickListener=onClickListener
    override fun getItemViewType(position: Int): Int {
        return if(data.size==0)
            TYPE_EMPTY
        else
            TYPE_DATA
    }

    inner class EmptyViewHolder(binding: ItemEmptyBinding) : RecyclerView.ViewHolder(binding.root)
    inner class DataViewHolder(binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType== TYPE_EMPTY)
        return EmptyViewHolder(
            ItemEmptyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else
            return DataViewHolder(
                ItemGalleryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

    }

    override fun getItemCount(): Int{
        return if(data.size==0)
            1
        else
            data.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {
            Glide.with(ItemGalleryBinding.bind(holder.itemView).image.context)
                .load(data[position].media_url)
                .into(ItemGalleryBinding.bind(holder.itemView).image)
            ViewCompat.setTransitionName(ItemGalleryBinding.bind(holder.itemView).image,data[position].id)

            holder.itemView.setOnTouchListener{ _, event ->
                if (event.action == MotionEvent.ACTION_UP&&ItemGalleryBinding.bind(holder.itemView).motionLayout.progress==0f) {
                    onItemClickListener.onItemClick(data[position],ItemGalleryBinding.bind(holder.itemView).image)
                }
                false
            }
            /*holder.itemView.setOnClickListener {
                onItemClickListener.onItemClick(data[position],ItemGalleryBinding.bind(holder.itemView).image)
            }*/
        }
    }

    fun addMedia(data: List<Data>) {
        this.data.apply {
            clear()
            addAll(data)
        }

    }

    companion object {
        const val TYPE_EMPTY = 0
        const val TYPE_DATA = 1
    }

    interface OnItemClickListener {
        fun onItemClick(data: Data, imageView: ImageView)
    }
}