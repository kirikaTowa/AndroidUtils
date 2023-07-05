package com.kakusummer.sample

import android.Manifest
import android.media.MediaPlayer
import android.util.Log
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.assistant.bases.BaseActivity
import com.assistant.utils.FileUtils
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
import java.io.IOException

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"
     var taskId: Long=0
    private val mPlayer = MediaPlayer()
    override fun initView() {
        super.initView()

//        binding.also {
//
//        }
/*        var tipUserDialog:TipUserPoliceDialog?=null
        tipUserDialog= TipUserPoliceDialog(this@MainActivity) {
            if (it) {
                tipUserDialog?.dismiss()
            } else {
                finish()
                tipUserDialog?.dismiss()
            }
        }
        tipUserDialog.show()*/


        Aria.download(this@MainActivity).register()
        Log.d("yeTest", "initMediaPlayer: " + filesDir.absoluteFile)
        FileUtils.createOrExistsDir(filesDir.absoluteFile.toString() + "/voiceAnnouncements")

        //不声明权限搞不下来
        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    try {
                        taskId= Aria.download(this@MainActivity)
                            .load("http://queue.uat.yuantutech.com:8081/tts/92ca4140-2728-4470-a018-dd539a13d7c5.wav")
                            .setFilePath(filesDir.absoluteFile.toString() + "/voiceAnnouncements" + "/music.wav")
                            .create()
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

    @Download.onTaskRunning
    protected fun running(task: DownloadTask) {
        val p = task.percent //任务进度百分比
        val speed = task.convertSpeed //转换单位后的下载速度，单位转换需要在配置文件中打开
        val speed1: Long = task.speed //原始byte长度速度
        Log.d("yeTest", "running: "+speed1)
    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask?) {
        //在这里处理任务完成的状态
        Log.d("yeTest", "running: "+task)
    }

    @Download.onTaskStart
    fun taskonTaskStart(task: DownloadTask?) {
        //在这里处理任务完成的状态
        Log.d("yeTest", "下载失败taskComplete: ")
    }

    @Download.onTaskStop
    fun taskStop(task: DownloadTask?) {
        //在这里处理任务完成的状态
        Log.d("yeTest", "下载失败taskComplete: ")
    }



    @Download.onTaskFail
    fun taskFailed(task: DownloadTask?) {
        //在这里处理任务完成的状态
        Log.d("yeTest", "下载失败taskComplete: ")
    }

    override fun initListener() {
        super.initListener()

        //可以覆盖掉父监听
     /*   onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("yeTest", "handleOnBackPressed cover: ")
            }
        })*/
    }
}