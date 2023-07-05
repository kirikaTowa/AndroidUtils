package com.kakusummer.sample

import android.Manifest
import android.R.attr.path
import android.media.MediaPlayer
import android.util.Log
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.assistant.bases.BaseActivity
import com.assistant.utils.FileUtils
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.permissionx.guolindev.PermissionX
import java.io.IOException


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    private val mPlayer = MediaPlayer()
    override fun initView() {
        super.initView()

        Aria.download(this@MainActivity).register()

        FileUtils.createOrExistsDir(filesDir.absoluteFile.toString() + "/voiceAnnouncements")

        FileDownloader.setup(this@MainActivity)
        //不声明权限搞不下来
        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    try {
                        FileDownloader.getImpl().create("http://queue.uat.yuantutech.com:8081/tts/92ca4140-2728-4470-a018-dd539a13d7c5.wav")
                            .setPath(filesDir.absoluteFile.toString() + "/voiceAnnouncements" + "/music.wav")
                            .setListener(object : FileDownloadListener() {
                                override fun pending(
                                    task: BaseDownloadTask,
                                    soFarBytes: Int,
                                    totalBytes: Int
                                ) {
                                    Log.d("yeTest", "pending: ")
                                }

                                override fun connected(
                                    task: BaseDownloadTask,
                                    etag: String,
                                    isContinue: Boolean,
                                    soFarBytes: Int,
                                    totalBytes: Int
                                ) {
                                    Log.d("yeTest", "connected: ")
                                }

                                override fun progress(
                                    task: BaseDownloadTask,
                                    soFarBytes: Int,
                                    totalBytes: Int
                                ) {
                                    Log.d("yeTest", "progress: ")
                                }

                                override fun blockComplete(task: BaseDownloadTask) {}
                                override fun retry(
                                    task: BaseDownloadTask,
                                    ex: Throwable,
                                    retryingTimes: Int,
                                    soFarBytes: Int
                                ) {
                                    Log.d("yeTest", "retry: ")
                                }

                                override fun completed(task: BaseDownloadTask) {
                                    Log.d("yeTest", "complete: ")
                                }
                                override fun paused(
                                    task: BaseDownloadTask,
                                    soFarBytes: Int,
                                    totalBytes: Int
                                ) {
                                    Log.d("yeTest", "paused: ")
                                }

                                override fun error(task: BaseDownloadTask, e: Throwable) {}
                                override fun warn(task: BaseDownloadTask) {}
                            }).start()



                    } catch (e: Exception) {
                        Log.d("yeTest", "onCreate: $e")
                    }
                }
            }
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        try {
            mPlayer.reset()

            //Log.d("yeTest", "播报连接: "+sid);
            mPlayer.setDataSource(filesDir.absoluteFile.toString() + "/voiceAnnouncements" + "/music.wav")
            mPlayer.isLooping = false
            mPlayer.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mPlayer.start()
        mPlayer.setOnCompletionListener {
            Log.d("yeTest", "setOnCompletionListener: ")

            //                try {
//                    PipedOutputStream out = playEntity.getOut();
//                    out.write(String.format(Constant.RESPONSE_FORMAT, playEntity.getCallback(),
//                            GsonUtil.gsonToString(new ApiResponse<SIDEntity>(true, new SIDEntity(playEntity.getSid()+"urlPlay"), Constant.CODE_OK)))
//                            .getBytes());
//                    out.flush();
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
        }
        mPlayer.setOnErrorListener { mediaPlayer, i, i1 ->
            Log.d("yeTest", "onError: ")
            false
        }
    }

}