package com.kakusummer.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.assistant.utils.UtilsBridge
import com.kakusummer.androidutils.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UtilsBridge.initApp(this.application)
    }
}