package com.kakusummer.sample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import com.kakusummer.androidutils.databinding.ActivityWebViewDemoBinding

class WebViewDemoActivity :  BaseActivity<ActivityWebViewDemoBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_web_view_demo
    override val TAG: String
        get() = "TAG_WebViewDemo"

    override fun initView() {
        super.initView()

//        ImmersionBar.with(this).statusBarView(R.id.view_top)
//            .init()
        binding.apply {
            val url = intent.getStringExtra(WebViewDemoActivity.EXTRA_URL) ?: ""


            wbContainer.also {
                it.loadUrl(url)

                it.settings.apply {
                    javaScriptEnabled = true // 设置支持javascript脚本
                    textSize = WebSettings.TextSize.NORMAL
                    requestFocus()
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    loadsImagesAutomatically = false //是否加载图片
                    blockNetworkImage = false //把图片加载放在最后来加载渲染
                    blockNetworkLoads = false
                    loadWithOverviewMode = true //自适应屏幕
                    domStorageEnabled = true //开启 DOM storage API 功能
                    useWideViewPort = true //双击缩放屏幕
                }


                webChromeClient = object : WebChromeClient() {
                    override fun onReceivedTitle(view: WebView, title: String) {
                        super.onReceivedTitle(view, title)
                    }
                }
                wvView.webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        view?.loadUrl(request?.url?.toString() ?: "")
                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                    }
                }
            }
        }

    }


    override fun initListener() {
//        super.initListener()
//        binding.apply {
//            ivBack.setOnClickListener {
//                finish()
//            }
//        }
    }
    


    override fun onDestroy() {
        super.onDestroy()
        binding.wbContainer.also {
            clearCache(true)
            clearHistory()
            clearFormData()
        }

    }

    companion object {
        const val EXTRA_URL = "url"
        fun launch(context: Activity, url: String) {
            context.startActivity(
                Intent(context, WebViewDemoActivity::class.java).putExtra(
                    EXTRA_URL,
                    url
                )
            )
        }
    }

}


