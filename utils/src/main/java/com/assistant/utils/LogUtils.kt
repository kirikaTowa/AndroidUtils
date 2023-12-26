package com.assistant.utils

import android.util.Log

object LogUtils {

    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5

    //一级是全打
    //Nothing 6级全不大
    private const val NOTHING = 6//上线后将log的level改为nothing

    private var level = BuildConfig.LogLevel


/*    private fun getLevels(): Int {
        return if (BuildConfig.DEBUG){
            level
        }else{
            NOTHING
        }
    }*/

    fun v(tag: String, msg: String) {
        if (level <= VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (level <= DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (level <= INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (level <= WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (level <= ERROR) {
            Log.e(tag, msg)
        }
    }

}
