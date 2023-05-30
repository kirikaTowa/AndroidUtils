package com.kakusummer.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakusummer.androidutils.R
import com.assistant.constants.MemoryConstant

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("yeTest", "onCreate: "+ MemoryConstant.BYTE)
    }

}