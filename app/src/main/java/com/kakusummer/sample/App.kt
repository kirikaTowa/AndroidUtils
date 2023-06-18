package com.kakusummer.sample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import com.assistant.utils.UtilsBridge





class App : Application() {

    companion object {
        lateinit var originHandler: Handler
            private set
        @SuppressLint("StaticFieldLeak")
        lateinit var originContext: Context
           private set
    }

    override fun onCreate() {
        super.onCreate()
        originContext = applicationContext//=this应该也行
        originHandler = Handler()
        //val rootDir = MMKV.initialize(this)
        UtilsBridge.initApp(this)
    }
}

