package com.kakusummer.sample

import android.view.View
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityCalendarBinding
import com.kakusummer.androidutils.databinding.ActivityTorchBinding
import java.util.Calendar


class CalendarActivity : BaseActivity<ActivityCalendarBinding>() {

    //定义五个当前时间的变量
    private var year: Int = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0


    override val layoutId: Int
        get() = R.layout.activity_calendar
    override val TAG: String
        get() = "TAG_VIEW_STUB"

    override fun initDate() {
        super.initDate()

    }

    override fun initView() {
        super.initView()


        val datePicker = findViewById<View>(R.id.datePicker) as DatePicker
        val timePicker = findViewById<View>(R.id.timePicker) as TimePicker
        //获取当前日期/时间
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        hour = calendar[Calendar.HOUR]
        minute = calendar[Calendar.MINUTE]
        //为DatePicker添加监听事件
        datePicker.init(year, month, day) { view, year, monthOfYear, dayOfMonth ->
            this@CalendarActivity.year = year
            this@CalendarActivity.month = monthOfYear
            this@CalendarActivity.day = dayOfMonth
            //显示用户选择的 日期 和 时间
            Toast.makeText(
                this@CalendarActivity,
                ((((year.toString() + "年" + month).toString() + "月" + day).toString() + "日" + hour).toString() + "时" + minute).toString() + "分",
                Toast.LENGTH_SHORT
            ).show()
        }
        //TimePicker选择监听器
        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            this@CalendarActivity.hour = hourOfDay
            this@CalendarActivity.minute = minute
        }
        val calendarview: CalendarView = findViewById<View>(R.id.calendarview) as CalendarView
        calendarview.setOnDateChangeListener { p0, year, month, dayOfMonth ->
            //month是偏移量，从0开始
            Toast.makeText(
                this@CalendarActivity,
                "您选择的时间是：" + year + "年" + month.plus(1) + "月" + dayOfMonth + "日",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    override fun initListener() {

    }


}