package com.kakusummer.sample.action

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.kakusummer.androidutils.R

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    //自测用，没兼容低版本
    override fun onReceive(context: Context, intent: Intent) {

//        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
//            Log.d("yeTest", "重启: ")
//        }
        //可打印log 但是切后台就无效了，onReceive都不会执行，再次进入才行
        Log.d("yeTest", "onReceive: ")
//        // 在这里添加闹钟响铃的操作，例如播放音频或显示通知
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channelId = "default_channel"
//            val channelName = "Default Channel"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//
//            val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
//                description = "Default Channel for app notifications"
//            }
//
//            // 获取 NotificationManager 服务
//            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//        // 创建通知
//        val notification = NotificationCompat.Builder(context, "default_channel")
//            .setContentTitle("闹钟提醒")
//            .setContentText("时间到了！")
//            .setSmallIcon(R.drawable.ic_1) // 确保该图标存在
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .build()
//
//        // 发送通知
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(0, notification)
    }
}
