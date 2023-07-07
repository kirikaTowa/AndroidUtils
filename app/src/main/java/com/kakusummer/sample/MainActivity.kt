package com.kakusummer.sample

import android.media.MediaPlayer
import android.util.Log
import com.assistant.bases.BaseActivity
import com.assistant.utils.FileUtils
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import java.io.File
import java.io.IOException


class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    private val mPlayer = MediaPlayer()
    override fun initView() {
        super.initView()
//        var file=File(filesDir.absoluteFile.toString() + "/voiceAnnouncements" + "/KKK.wav")
//        FileUtils.createOrExistsFile(file)
        //操纵内部私有目录 无需声明任何权限
        FileUtils.createOrExistsDir(filesDir.absoluteFile.toString() + "/voiceAnnouncements")
        FileUtils.delete(FileUtils.getFileByPath(filesDir.absoluteFile.toString() + "/voiceAnnouncements" + "/music.wav"))
        FileDownloader.setup(this@MainActivity)
        try {
            FileDownloader.getImpl()
                .create("https://www.cambridgeenglish.org/images/506891-a2-key-for-schools-listening-sample-test.mp3")
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
                        Log.d("yeTest", "completed: " + Thread.currentThread())
                        initMediaPlayer()
                    }

                    override fun paused(
                        task: BaseDownloadTask,
                        soFarBytes: Int,
                        totalBytes: Int
                    ) {
                        Log.d("yeTest", "paused: ")
                    }

                    override fun error(task: BaseDownloadTask, e: Throwable) {
                        Log.d("yeTest", "error: " +e)
                    }
                    override fun warn(task: BaseDownloadTask) {
                        Log.d("yeTest", "error: ")
                    }
                }).start()


        } catch (e: Exception) {
            Log.d("yeTest", "onCreate: $e")
        }
//                }
//            }

    }

    override fun initListener() {
        super.initListener()
        //手动测试删除
        binding.tvText.setOnClickListener {
            try {
                FileUtils.delete(FileUtils.getFileByPath(filesDir.absoluteFile.toString() + "/voiceAnnouncements" + "/music.wav"))
            }catch (e:Exception){
                Log.d(TAG, "删除文件错误")
            }

        }
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