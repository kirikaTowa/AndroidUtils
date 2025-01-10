package com.kakusummer.sample

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.ImageView
import com.assistant.bases.BaseActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityMainBinding
import com.kakusummer.sample.action.AlarmReceiver
import java.util.Calendar
import android.provider.Settings

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = "TAG_MainActivity"

    override fun initView() {
        super.initView()
        binding.apply {

            setAlarmButton.setOnClickListener {
                setAlarm()
            }
        }
    }

    private fun setAlarm() {
        // 设置闹钟的触发时间（比如设置为10秒后触发）
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 3)

        // 获取 AlarmManager 系统服务
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 创建一个 Intent 来触发 BroadcastReceiver
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE )

        // 设置闹钟
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun initListener() {
        super.initListener()
    }

    override fun onResume() {
        super.onResume()

    }



}