package com.assistant.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import java.io.File

object CleanUtils  {

        fun cleanInternalCache(): Boolean {
            return UtilsBridge.deleteAllInDir(UtilsBridge.app.cacheDir)
        }

        /**
         * Clean the internal files.
         *
         * directory: /data/data/package/files
         *
         * @return `true`: success<br></br>`false`: fail
         */
        fun cleanInternalFiles(): Boolean {
            return UtilsBridge.deleteAllInDir(UtilsBridge.app.filesDir)
        }

        /**
         * Clean the internal databases.
         *
         * directory: /data/data/package/databases
         *
         * @return `true`: success<br></br>`false`: fail
         */
        fun cleanInternalDbs(): Boolean {
            return UtilsBridge.deleteAllInDir(
                File(
                    UtilsBridge.app.filesDir.parent,
                    "databases"
                )
            )
        }

        /**
         * Clean the internal database by name.
         *
         * directory: /data/data/package/databases/dbName
         *
         * @param dbName The name of database.
         * @return `true`: success<br></br>`false`: fail
         */
        fun cleanInternalDbByName(dbName: String?): Boolean {
            return UtilsBridge.app.deleteDatabase(dbName)
        }

        /**
         * Clean the internal shared preferences.
         *
         * directory: /data/data/package/shared_prefs
         *
         * @return `true`: success<br></br>`false`: fail
         */
        fun cleanInternalSp(): Boolean {
            return UtilsBridge.deleteAllInDir(
                File(
                    UtilsBridge.app.filesDir.parent,
                    "shared_prefs"
                )
            )
        }

        /**
         * Clean the external cache.
         *
         * directory: /storage/emulated/0/android/data/package/cache
         *
         * @return `true`: success<br></br>`false`: fail
         */
        fun cleanExternalCache(): Boolean {
            return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() && UtilsBridge.deleteAllInDir(
                UtilsBridge.app.externalCacheDir
            )
        }

        /**
         * Clean the custom directory.
         *
         * @param dirPath The path of directory.
         * @return `true`: success<br></br>`false`: fail
         */
        fun cleanCustomDir(dirPath: String?): Boolean {
            return UtilsBridge.deleteAllInDir(UtilsBridge.getFileByPath(dirPath))
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun cleanAppUserData() {
            val am =
                UtilsBridge.app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.clearApplicationUserData()
        }

}