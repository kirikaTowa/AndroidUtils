package com.kakusummer.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityViewStubBinding
import com.kakusummer.androidutils.databinding.ViewStubDemoBinding

class ViewStubActivity : BaseActivity<ActivityViewStubBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_view_stub
    override val TAG: String
        get() = "TAG_VIEW_STUB"
    lateinit var vsDemoBinding:ViewStubDemoBinding

    override fun initDate() {
        super.initDate()
        inflateViewStub()

        vsDemoBinding.tvTextOne.text="你好"
    }

    private fun inflateViewStub() {
        if (!binding.vsDemo.isInflated) {
            val vsInflate: View? =  binding.vsDemo.viewStub?.inflate()
            vsDemoBinding= vsInflate?.let {
                DataBindingUtil.getBinding(it)
            }!!
        }
    }
}