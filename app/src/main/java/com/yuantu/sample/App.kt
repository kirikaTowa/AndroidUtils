package com.yuantu.sample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import com.assistant.utils.UtilsBridge


class App : Application() {

    companion object {
        lateinit var MHANDLER: Handler
            private set
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: Context
           private set
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext//=this应该也行
        MHANDLER = Handler()
        //val rootDir = MMKV.initialize(this)
        UtilsBridge.initApp(this)
    }
}

