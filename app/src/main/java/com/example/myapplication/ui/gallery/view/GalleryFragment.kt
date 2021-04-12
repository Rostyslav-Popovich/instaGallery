package com.example.myapplication.ui.gallery.view

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.*
import com.example.myapplication.R
import com.example.myapplication.data.model.Data
import com.example.myapplication.databinding.FragmentGalleryBinding
import com.example.myapplication.ui.detail.DetailFragment
import com.example.myapplication.ui.gallery.view.adapter.GalleryAdapter
import com.example.myapplication.ui.gallery.view.adapter.GalleryDiffUtil
import com.example.myapplication.ui.gallery.viewmodel.GalleryViewModel
import com.example.myapplication.utils.Const.APP_PREFS_TOKEN
import com.example.myapplication.utils.Status
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class GalleryFragment : Fragment(), GalleryAdapter.OnItemClickListener {

    private val TAG = GalleryFragment::class.simpleName
    private val viewModel: GalleryViewModel by viewModel()
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding
    private val preferences: SharedPreferences by inject()
    private lateinit var adapter: GalleryAdapter
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    private var pageSize = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(layoutInflater)

        setupUI()

        setupObservers(
            preferences.getString(APP_PREFS_TOKEN, "").toString()
        )

        binding?.swipe?.setOnRefreshListener {
            isLoading = false
            isLastPage = false
            adapter.clearMedia()
            viewModel.getGallery(
                preferences.getString(APP_PREFS_TOKEN, "").toString(),
                ""
            )
            binding?.swipe?.isRefreshing = false
        }

        return binding?.root
    }

    private fun setupUI() {
        (activity as AppCompatActivity?)?.findViewById<TextView>(R.id.title)?.text =
            getString(R.string.title_gallery)
        val gridLayoutManager = GridLayoutManager(context, 2)

        binding?.recyclerView?.layoutManager = gridLayoutManager

        adapter = GalleryAdapter(arrayListOf(), this)
        binding?.recyclerView?.addItemDecoration(
            DividerItemDecoration(
                binding?.recyclerView?.context,
                (binding?.recyclerView?.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding?.recyclerView?.adapter = adapter

        binding?.recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
                val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                val isNotAtBeginning = firstVisibleItemPosition >= 0
                val shouldPaginate =
                    isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isScrolling
                if (shouldPaginate) {
                    viewModel.pagingLiveData.value?.cursors?.after?.let {
                        viewModel.getGallery(
                            preferences.getString(APP_PREFS_TOKEN, "").toString(),
                            it
                        )
                    }
                    isScrolling = false
                } else {
                    binding?.recyclerView?.setPadding(0, 0, 0, 0)
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
            }
        })

    }

    @Suppress("UNCHECKED_CAST")
    private fun setupObservers(
        token: String
    ) {
        viewModel.statusLiveData.observe(viewLifecycleOwner, {
            when (it) {
                Status.SUCCESS -> {
                    if ((viewModel.successLiveData.value as List<Data>).isNotEmpty()) {
                        isLoading = false
                        val galleryDiffUtil = GalleryDiffUtil(
                            adapter.getMedia(),
                            viewModel.successLiveData.value as List<Data>
                        )
                        val galleryDiffResult = DiffUtil.calculateDiff(galleryDiffUtil)
                        adapter.addMedia(viewModel.successLiveData.value as List<Data>)
                        galleryDiffResult.dispatchUpdatesTo(adapter)
                        isLastPage = false
                        binding?.recyclerView?.visibility = View.VISIBLE
                        binding?.progressBar?.visibility = View.GONE
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                        isLastPage = true
                    }
                }

                Status.ERROR -> {
                    isLoading = false
                    binding?.recyclerView?.visibility = View.VISIBLE
                    //progressBar.visibility = View.GONE
                    viewModel.errorLiveData.value?.error?.let { error ->
                        Log.d(
                            "GALLERY_FRAGMENT: ",
                            error.message
                        )
                    }
                    requireActivity().onBackPressed()
                }

                Status.LOADING -> {
                    isLoading = true
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                else -> Log.d(TAG, "some error")
            }
        })

        viewModel.getGallery(token, "")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onItemClick(data: Data, imageView: ImageView) {
        requireActivity().supportFragmentManager.commit {
            val detailFragment = DetailFragment(data, imageView.transitionName)
            replace(R.id.container, detailFragment)
            setReorderingAllowed(true)
            ViewCompat.getTransitionName(imageView)?.let { addSharedElement(imageView, it) }
            addToBackStack("detail")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}