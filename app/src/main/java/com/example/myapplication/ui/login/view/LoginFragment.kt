package com.example.myapplication.ui.login.view

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.ui.gallery.view.GalleryFragment
import com.example.myapplication.ui.login.viewmodel.LoginViewModel
import com.example.myapplication.utils.Const.Companion.APP_PREFS_TOKEN
import com.example.myapplication.utils.Status
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()
    private lateinit var binding: FragmentLoginBinding
    private val preferences: SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        activity?.title = getString(R.string.txt_login)

        setupWebView()
        binding.buttonLogin.setOnClickListener{
            binding.progressBar.visibility=View.VISIBLE
            binding.buttonLogin.visibility=View.GONE
            binding.webView.loadUrl("https://api.instagram.com/oauth/authorize?client_id=1057098534783422&redirect_uri=https%3A%2F%2Fsocialsizzle.heroku.com%2Fauth%2F&scope=user_profile%2Cuser_media&response_type=code")
        }

        return binding.root
    }

    private fun setupWebView(){
        binding.webView.settings.javaScriptEnabled=true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                val uri: Uri = Uri.parse(url)
                val chapter = uri.getQueryParameter("code")
                if (chapter!=null){
                    binding.progressBar.visibility=View.VISIBLE

                    setupObservers(getString(R.string.instagram_app_id).toLong(),
                        "2fcbe9b28fda739eee21ba4e8ac3db23",
                        "authorization_code",
                        "https://socialsizzle.heroku.com/auth/",
                        chapter)
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility=View.GONE
            }
        }
    }

    private fun setupObservers(
        clientId: Long,
        client_secret: String,
        grant_type: String,
        redirect_uri: String,
        code: String
    ) {
        viewModel.getToken(clientId, client_secret, grant_type, redirect_uri, code).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { token ->
                            val editor = preferences.edit()
                            editor.putString(APP_PREFS_TOKEN,token.access_token)
                            editor.apply()
                            requireActivity().supportFragmentManager.commit {
                                replace<GalleryFragment>(R.id.container)
                                setReorderingAllowed(true)
                                addToBackStack("login")
                            }
                        }
                    }
                    Status.ERROR -> {
                        Log.d("LOGIN_ACTIVITY: ", it.message.toString())
                    }
                    Status.LOADING -> {
                        //progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}