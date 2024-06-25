package com.kakusummer.sample

import android.app.WallpaperManager
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.assistant.bases.BaseActivity
import com.assistant.utils.ConvertUtils
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityViewStubBinding
import com.kakusummer.androidutils.databinding.ViewStubDemoBinding

class ViewStubActivity : BaseActivity<ActivityViewStubBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_view_stub
    override val TAG: String
        get() = "TAG_VIEW_STUB"
    lateinit var vsDemoBinding: ViewStubDemoBinding

    override fun initDate() {
        super.initDate()
        inflateViewStub()

        //设置壁纸功能，在Fragment中可能会报那个无参Fragment问题
        val res=AppCompatResources.getDrawable(this, R.drawable.ic_pic_2_audit_3)
        val wallpaperManager = WallpaperManager.getInstance(this)
        wallpaperManager.setBitmap(ConvertUtils.drawable2Bitmap(res))

        vsDemoBinding.tvTextOne.text = "你好"
    }

    private fun inflateViewStub() {
        if (!binding.vsDemo.isInflated) {
            val vsInflate: View? = binding.vsDemo.viewStub?.inflate()
            vsDemoBinding = vsInflate?.let {
                DataBindingUtil.getBinding(it)
            }!!
        }
    }
}