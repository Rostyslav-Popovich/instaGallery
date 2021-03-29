package com.example.myapplication.ui.login.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.ui.gallery.view.GalleryFragment
import com.example.myapplication.ui.login.viewmodel.LoginViewModel
import com.example.myapplication.utils.Status
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()
    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        (activity as AppCompatActivity?)!!.findViewById<TextView>(R.id.title).text =
            getString(R.string.txt_login)
        setupWebView()
        binding.buttonLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonLogin.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
            binding.webView.loadUrl("https://api.instagram.com/oauth/authorize?client_id=${BuildConfig.INSTAGRAM_APP_ID}&redirect_uri=https%3A%2F%2Fwww.google.com%2F&scope=user_profile%2Cuser_media&response_type=code")
        }

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                val uri: Uri = Uri.parse(url)
                val chapter = uri.getQueryParameter("code")
                if (chapter != null) {
                    binding.progressBar.visibility = View.VISIBLE

                    setupObservers(
                        chapter
                    )
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupObservers(
        code: String
    ) {
        viewModel.getToken(
            BuildConfig.INSTAGRAM_APP_ID.toLong(),
            BuildConfig.CLIENT_SECRET,
            "authorization_code",
            "https://www.google.com/",
            code
        )

        viewModel.liveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    requireActivity().supportFragmentManager.commit {
                        replace<GalleryFragment>(R.id.container)
                        setReorderingAllowed(true)
                        addToBackStack("login")
                    }
                }
                Status.ERROR -> {
                    Log.d("LOGIN_ACTIVITY: ", it.message.toString())
                    binding.webView.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}