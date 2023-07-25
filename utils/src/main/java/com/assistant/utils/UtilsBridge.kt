package com.assistant.utils

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import java.io.File
import java.io.InputStream
import java.lang.reflect.Type

object UtilsBridge {
    @JvmStatic
    fun initApp(App: Application) {
        app = App
    }


    @SuppressLint("StaticFieldLeak")
    lateinit var app: Application

    // BarUtils
    open fun getStatusBarHeight(): Int {
        return BarUtils.statusBarHeight
    }

    fun getNavBarHeight(): Int {
        return BarUtils.navBarHeight
    }

    // FileIOUtils
    ///////////////////////////////////////////////////////////////////////////
    fun writeFileFromBytes(
        file: File?,
        bytes: ByteArray?
    ): Boolean {
        return FileIOUtils.writeFileFromBytesByChannel(file, bytes, true)
    }

    fun readFile2Bytes(file: File?): ByteArray {
        return FileIOUtils.readFile2BytesByChannel(file)
    }

    fun writeFileFromString(filePath: String?, content: String?, append: Boolean): Boolean {
        return FileIOUtils.writeFileFromString(filePath, content, append)
    }

    @JvmStatic
    fun writeFileFromIS(filePath: String?, `is`: InputStream?): Boolean {
        return FileIOUtils.writeFileFromIS(filePath, `is`)
    }

    // FileUtils
    ///////////////////////////////////////////////////////////////////////////
    @JvmStatic
    fun isFileExists(file: File?): Boolean {
        return FileUtils.isFileExists(file)
    }

    @JvmStatic
    fun getFileByPath(filePath: String?): File {
        return FileUtils.getFileByPath(filePath)
    }

    @JvmStatic
    fun deleteAllInDir(dir: File?): Boolean {
        return FileUtils.deleteAllInDir(dir)
    }

    @JvmStatic
    fun createOrExistsFile(file: File?): Boolean {
        return FileUtils.createOrExistsFile(file)
    }

    @JvmStatic
    fun createOrExistsDir(file: File?): Boolean {
        return FileUtils.createOrExistsDir(file)
    }

    @JvmStatic
    fun createFileByDeleteOldFile(file: File?): Boolean {
        return FileUtils.createFileByDeleteOldFile(file)
    }

    @JvmStatic
    fun getFsTotalSize(path: String?): Long {
        return FileUtils.getFsTotalSize(path)
    }

    @JvmStatic
    fun getFsAvailableSize(path: String?): Long {
        return FileUtils.getFsAvailableSize(path)
    }

    @JvmStatic
    fun notifySystemToScan(file: File?) {
        FileUtils.notifySystemToScan(file)
    }

    // ImageUtils
    ///////////////////////////////////////////////////////////////////////////
    fun bitmap2Bytes(bitmap: Bitmap?): ByteArray {
        return ImageUtils.bitmap2Bytes(bitmap)
    }

    fun bitmap2Bytes(bitmap: Bitmap?, format: CompressFormat?, quality: Int): ByteArray {
        return ImageUtils.bitmap2Bytes(bitmap, format!!, quality)
    }

    fun bytes2Bitmap(bytes: ByteArray?): Bitmap {
        return ImageUtils.bytes2Bitmap(bytes)
    }

    fun drawable2Bytes(drawable: Drawable?): ByteArray {
        return ImageUtils.drawable2Bytes(drawable)
    }

    fun drawable2Bytes(drawable: Drawable?, format: CompressFormat?, quality: Int): ByteArray {
        return ImageUtils.drawable2Bytes(drawable, format, quality)
    }

    fun bytes2Drawable(bytes: ByteArray?): Drawable {
        return ImageUtils.bytes2Drawable(bytes)
    }

    @JvmStatic
    fun view2Bitmap(view: View?): Bitmap {
        return ImageUtils.view2Bitmap(view)
    }

    @JvmStatic
    fun drawable2Bitmap(drawable: Drawable?): Bitmap {
        return ImageUtils.drawable2Bitmap(drawable)
    }

    @JvmStatic
    fun bitmap2Drawable(bitmap: Bitmap?): Drawable {
        return ImageUtils.bitmap2Drawable(bitmap)
    }

    // RomUtils
    val isSamsung: Boolean
        get() = RomUtils.isSamsung

    // SDCardUtils
    @JvmStatic
    val isSDCardEnableByEnvironment: Boolean
        get() = SDCardUtils.isSDCardEnableByEnvironment

    // SizeUtils
    @JvmStatic
    fun dp2px(dpValue: Float): Int {
        return SizeUtils.dp2px(dpValue)
    }

    @JvmStatic
    fun px2dp(pxValue: Float): Int {
        return SizeUtils.px2dp(pxValue)
    }

    @JvmStatic
    fun sp2px(spValue: Float): Int {
        return SizeUtils.sp2px(spValue)
    }

    @JvmStatic
    fun px2sp(pxValue: Float): Int {
        return SizeUtils.px2sp(pxValue)
    }

    // StringUtils
    @JvmStatic
    fun isSpace(s: String?): Boolean {
        return StringUtils.isSpace(s)
    }

    @JvmStatic
    fun equals(s1: CharSequence?, s2: CharSequence?): Boolean {
        return StringUtils.equals(s1, s2)
    }

    // UriUtils
    fun file2Uri(file: File?): Uri {
        return UriUtils.file2Uri(file)
    }

    @JvmStatic
    fun uri2File(uri: Uri?): File {
        return UriUtils.uri2File(uri)
    }

    //把Json转为某个类型
    @JvmStatic
    fun <T> fromJson(json: String?, type: Type?): T {
        return GsonUtils.fromJson(json, type!!)
    }

    //把某个类型转为Json
    @JvmStatic
    fun toJson(`object`: Any?): String? {
        return GsonUtils.toJson(`object`)
    }
}