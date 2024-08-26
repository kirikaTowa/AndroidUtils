package com.kakusummer.sample

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakusummer.androidutils.R

//联动1测试
class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)
        val getMusic:Music= intent.extras?.get("Music") as Music
        Log.d("yeTest", "onCreate: "+getMusic)
    }
}