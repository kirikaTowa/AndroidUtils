package com.kakusummer.sample

import android.view.View
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityCalendarBinding
import com.kakusummer.androidutils.databinding.ActivityCenterWheelBinding
import com.kakusummer.androidutils.databinding.ActivityTorchBinding
import com.kakusummer.sample.adapter.WheelCenterAdapter
import com.kakusummer.sample.adapter.layoutManager.GalleryLayoutManager
import java.util.Calendar
import kotlin.math.abs

/**
 * desc：若干日历控件Demo
 */

class CenterWheelActivity : BaseActivity<ActivityCenterWheelBinding>() {


    override val layoutId: Int
        get() = R.layout.activity_center_wheel
    override val TAG: String
        get() = "TAG_CENTER_WHEEL"

    private val wheelCenterAdapter by lazy { WheelCenterAdapter().apply { submitList((10..98).map { (it / 10.0).toString() }.toList()) } }
    private val wheelCenterManager by lazy { GalleryLayoutManager(GalleryLayoutManager.VERTICAL) }

    override fun initDate() {
        super.initDate()

    }

    override fun initView() {
        super.initView()

        wheelCenterManager.apply {
            setItemTransformer(ScaleTransformer())
            setOnItemSelectedListener(object :GalleryLayoutManager.OnItemSelectedListener{
                override fun onItemSelected(recyclerView: RecyclerView?, item: View?, position: Int) {
                    val bloodSugar = wheelCenterAdapter.currentList[position].toDouble()
                    setTextData(bloodSugar)
                }
            })
        }
        binding.apply {
            wheelCenterManager.attach(rvCenter)
            binding.rvCenter.adapter = wheelCenterAdapter
            //rvCenter.addItemDecoration(Decoration.LinearDecoration(5, bottom = 5))
        }


    }

    private fun setTextData(bloodSugar: Double) {
        //这种传参玩的花
        binding.siView.setData(bloodSugar = bloodSugar)
    }

    override fun initListener() {

    }

    class ScaleTransformer : GalleryLayoutManager.ItemTransformer {
        override fun transformItem(layoutManager: GalleryLayoutManager?, item: View?, fraction: Float) {
            item!!.pivotX = item.width / 2f
            item.pivotY = item.height / 2f
            val scale = 1 - 0.3f * abs(fraction)
            item.scaleX = scale
            item.scaleY = scale
        }
    }
}