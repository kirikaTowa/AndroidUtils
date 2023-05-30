package com.assistant.utils

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File

object PathUtils{

        private val SEP = File.separatorChar

        /**
         * Join the path.
         *
         * @param parent The parent of path.
         * @param child  The child path.
         * @return the path
         */
        fun join(parent: String?, child: String): String? {
            var parent = parent
            if (TextUtils.isEmpty(child)) return parent
            if (parent == null) {
                parent = ""
            }
            val len = parent.length
            val legalSegment = getLegalSegment(child)
            val newPath: String
            newPath = if (len == 0) {
                SEP.toString() + legalSegment
            } else if (parent.get(len - 1) == SEP) {
                parent + legalSegment
            } else {
                parent + SEP + legalSegment
            }
            return newPath
        }

        private fun getLegalSegment(segment: String): String {
            var st = -1
            var end = -1
            val charArray = segment.toCharArray()
            for (i in charArray.indices) {
                val c = charArray[i]
                if (c != SEP) {
                    if (st == -1) {
                        st = i
                    }
                    end = i
                }
            }
            if (st >= 0 && end >= st) {
                return segment.substring(st, end + 1)
            }
            throw IllegalArgumentException("segment of <$segment> is illegal")
        }

        /**
         * Return the path of /system.
         *
         * @return the path of /system
         */
        val rootPathTarget: String
            get() = getAbsolutePath(Environment.getRootDirectory())

        /**
         * Return the path of /data.
         *
         * @return the path of /data
         */
        val dataPath: String
            get() = getAbsolutePath(Environment.getDataDirectory())

        /**
         * Return the path of /cache.
         *
         * @return the path of /cache
         */
        val downloadCachePath: String
            get() = getAbsolutePath(Environment.getDownloadCacheDirectory())

        /**
         * Return the path of /data/data/package.
         *
         * @return the path of /data/data/package
         */
        val internalAppDataPath: String
            get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                UtilsBridge.app.applicationInfo.dataDir
            } else getAbsolutePath(UtilsBridge.app.dataDir)

        /**
         * Return the path of /data/data/package/code_cache.
         *
         * @return the path of /data/data/package/code_cache
         */
        val internalAppCodeCacheDir: String
            get() {
                return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    UtilsBridge.app.applicationInfo.dataDir + "/code_cache"
                } else getAbsolutePath(UtilsBridge.app.codeCacheDir)
            }

        /**
         * Return the path of /data/data/package/cache.
         *
         * @return the path of /data/data/package/cache
         */
        val internalAppCachePath: String
            get() = getAbsolutePath(UtilsBridge.app.cacheDir)

        /**
         * Return the path of /data/data/package/databases.
         *
         * @return the path of /data/data/package/databases
         */
        val internalAppDbsPath: String
            get() = UtilsBridge.app.applicationInfo.dataDir + "/databases"

        /**
         * Return the path of /data/data/package/databases/name.
         *
         * @param name The name of database.
         * @return the path of /data/data/package/databases/name
         */
        fun getInternalAppDbPath(name: String?): String {
            return getAbsolutePath(UtilsBridge.app.getDatabasePath(name))
        }

        /**
         * Return the path of /data/data/package/files.
         *
         * @return the path of /data/data/package/files
         */
        val internalAppFilesPath: String
            get() = getAbsolutePath(UtilsBridge.app.filesDir)

        /**
         * Return the path of /data/data/package/shared_prefs.
         *
         * @return the path of /data/data/package/shared_prefs
         */
        val internalAppSpPath: String
            get() = UtilsBridge.app.applicationInfo.dataDir + "/shared_prefs"

        /**
         * Return the path of /data/data/package/no_backup.
         *
         * @return the path of /data/data/package/no_backup
         */
        val internalAppNoBackupFilesPath: String
            get() {
                return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    UtilsBridge.app.applicationInfo.dataDir + "/no_backup"
                } else getAbsolutePath(UtilsBridge.app.noBackupFilesDir)
            }

        /**
         * Return the path of /storage/emulated/0.
         *
         * @return the path of /storage/emulated/0
         */
        val externalStoragePath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStorageDirectory()
                )
            }

        /**
         * Return the path of /storage/emulated/0/Music.
         *
         * @return the path of /storage/emulated/0/Music
         */
        val externalMusicPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Podcasts.
         *
         * @return the path of /storage/emulated/0/Podcasts
         */
        val externalPodcastsPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Ringtones.
         *
         * @return the path of /storage/emulated/0/Ringtones
         */
        val externalRingtonesPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Alarms.
         *
         * @return the path of /storage/emulated/0/Alarms
         */
        val externalAlarmsPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Notifications.
         *
         * @return the path of /storage/emulated/0/Notifications
         */
        val externalNotificationsPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Pictures.
         *
         * @return the path of /storage/emulated/0/Pictures
         */
        val externalPicturesPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Movies.
         *
         * @return the path of /storage/emulated/0/Movies
         */
        val externalMoviesPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Download.
         *
         * @return the path of /storage/emulated/0/Download
         */
        val externalDownloadsPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                )
            }

        /**
         * Return the path of /storage/emulated/0/DCIM.
         *
         * @return the path of /storage/emulated/0/DCIM
         */
        val externalDcimPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Documents.
         *
         * @return the path of /storage/emulated/0/Documents
         */
        val externalDocumentsPath: String
            get() {
                if (!UtilsBridge.isSDCardEnableByEnvironment) return ""
                return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    getAbsolutePath(Environment.getExternalStorageDirectory()) + "/Documents"
                } else getAbsolutePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS))
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package.
         *
         * @return the path of /storage/emulated/0/Android/data/package
         */
        val externalAppDataPath: String
            get() {
                if (!UtilsBridge.isSDCardEnableByEnvironment) return ""
                val externalCacheDir = UtilsBridge.app.externalCacheDir ?: return ""
                return getAbsolutePath(externalCacheDir.parentFile)
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/cache.
         *
         * @return the path of /storage/emulated/0/Android/data/package/cache
         */
        val externalAppCachePath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.externalCacheDir
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files
         */
        val externalAppFilesPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(null)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Music.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Music
         */
        val externalAppMusicPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(
                        Environment.DIRECTORY_MUSIC
                    )
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Podcasts.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Podcasts
         */
        val externalAppPodcastsPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(
                        Environment.DIRECTORY_PODCASTS
                    )
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Ringtones.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Ringtones
         */
        val externalAppRingtonesPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(
                        Environment.DIRECTORY_RINGTONES
                    )
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Alarms.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Alarms
         */
        val externalAppAlarmsPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(
                        Environment.DIRECTORY_ALARMS
                    )
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Notifications.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Notifications
         */
        val externalAppNotificationsPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(
                        Environment.DIRECTORY_NOTIFICATIONS
                    )
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Pictures.
         *
         * @return path of /storage/emulated/0/Android/data/package/files/Pictures
         */
        val externalAppPicturesPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(
                        Environment.DIRECTORY_PICTURES
                    )
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Movies.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Movies
         */
        val externalAppMoviesPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(
                        Environment.DIRECTORY_MOVIES
                    )
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Download.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Download
         */
        val externalAppDownloadPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(
                        Environment.DIRECTORY_DOWNLOADS
                    )
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/DCIM.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/DCIM
         */
        val externalAppDcimPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(
                        Environment.DIRECTORY_DCIM
                    )
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/data/package/files/Documents.
         *
         * @return the path of /storage/emulated/0/Android/data/package/files/Documents
         */
        val externalAppDocumentsPath: String
            get() {
                if (!UtilsBridge.isSDCardEnableByEnvironment) return ""
                return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    getAbsolutePath(UtilsBridge.app.getExternalFilesDir(null)) + "/Documents"
                } else getAbsolutePath(
                    UtilsBridge.app.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                )
            }

        /**
         * Return the path of /storage/emulated/0/Android/obb/package.
         *
         * @return the path of /storage/emulated/0/Android/obb/package
         */
        val externalAppObbPath: String
            get() {
                return if (!UtilsBridge.isSDCardEnableByEnvironment) "" else getAbsolutePath(
                    UtilsBridge.app.obbDir
                )
            }
        val rootPathExternalFirst: String?
            get() {
                var rootPath: String =
                    externalStoragePath
                if (TextUtils.isEmpty(rootPath)) {
                    rootPath = rootPathTarget
                }
                return rootPath
            }
        val appDataPathExternalFirst: String?
            get() {
                var appDataPath: String =
                    externalAppDataPath
                if (TextUtils.isEmpty(appDataPath)) {
                    appDataPath = internalAppDataPath
                }
                return appDataPath
            }
        val filesPathExternalFirst: String?
            get() {
                var filePath: String =
                    externalAppFilesPath
                if (TextUtils.isEmpty(filePath)) {
                    filePath = internalAppFilesPath
                }
                return filePath
            }
        val cachePathExternalFirst: String?
            get() {
                var appPath: String =
                    externalAppCachePath
                if (TextUtils.isEmpty(appPath)) {
                    appPath = internalAppCachePath
                }
                return appPath
            }

        private fun getAbsolutePath(file: File?): String {
            return if (file == null) "" else file.absolutePath
        }
}