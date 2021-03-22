package com.example.myapplication.ui.detail

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Data
import com.example.myapplication.databinding.FragmentDetailBinding

class DetailFragment(val data: Data, private val transitionName: String?) : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        (activity as AppCompatActivity?)!!.findViewById<TextView>(R.id.title).text = data.id

        //postponeEnterTransition(250,TimeUnit.MILLISECONDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(
                android.R.transition.move
            )
            binding.image.transitionName = transitionName
        }


        Glide.with(binding.image.context)
            .load(data.media_url)
            .into(binding.image)
        return binding.root
    }
}