package com.example.myapplication.ui.gallery.view

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.model.Data
import com.example.myapplication.databinding.FragmentGalleryBinding
import com.example.myapplication.ui.detail.DetailFragment
import com.example.myapplication.ui.gallery.view.GalleryAdapter.Companion.TYPE_DATA
import com.example.myapplication.ui.gallery.viewmodel.GalleryViewModel
import com.example.myapplication.utils.Const.APP_PREFS_TOKEN
import com.example.myapplication.utils.Status
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class GalleryFragment : Fragment(), GalleryAdapter.OnItemClickListener {

    private val viewModel: GalleryViewModel by viewModel()
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val preferences: SharedPreferences by inject()
    private lateinit var adapter: GalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(layoutInflater)

        setupUI()

        binding.swipe.setOnRefreshListener {
            setupObservers(
                preferences.getString(APP_PREFS_TOKEN, "").toString(),
                "id,media_type,media_url,children{media_url,media_type}",
                ""
            )
        }
        setupObservers(
            preferences.getString(APP_PREFS_TOKEN, "").toString(),
            "id,media_type,media_url,children{media_url,media_type}",
            ""
        )

        return binding.root
    }

    private fun setupUI() {
        (activity as AppCompatActivity?)!!.findViewById<TextView>(R.id.title).text =
            getString(R.string.title_gallery)
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    TYPE_DATA -> 1
                    else -> 2
                }
            }
        }
        binding.recyclerView.layoutManager = gridLayoutManager

        adapter = GalleryAdapter(arrayListOf(), this)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.recyclerView.adapter = adapter
    }

    @Suppress("UNCHECKED_CAST")
    private fun setupObservers(
        token: String,
        field: String,
        after: String
    ) {

        viewModel.getGallery(token, field, after)


        viewModel.statusLiveData.observe(viewLifecycleOwner, {
            when (it) {
                Status.SUCCESS -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.swipe.isRefreshing = false
                    adapter.apply {
                        addMedia(viewModel.successLiveData.value as List<Data>)
                        notifyDataSetChanged()
                    }
                }

                Status.ERROR -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.swipe.isRefreshing = false
                    //progressBar.visibility = View.GONE
                    Log.d("GALLERY_FRAGMENT: ", (viewModel.errorLiveData.value!!.error.message))
                    requireActivity().onBackPressed()
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onItemClick(data: Data, imageView: ImageView) {

        requireActivity().supportFragmentManager.commit {
            val detailFragment = DetailFragment(data, imageView.transitionName)
            replace(R.id.container, detailFragment)
            setReorderingAllowed(true)
            addSharedElement(imageView, ViewCompat.getTransitionName(imageView)!!)
            addToBackStack("detail")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}