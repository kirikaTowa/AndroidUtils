package com.kakusummer.sample.viewCustom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.assistant.bases.BaseActivity
import com.assistant.viewcustom.switchbutton.SwitchButton
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivitySwitchButtonBinding

class SwitchButtonActivity : BaseActivity<ActivitySwitchButtonBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_switch_button
    override val TAG: String
        get() = "TAG_SwitchButtonActivity"

    override fun initListener() {
        super.initListener()
        binding.switchButton.apply {
            //switchButton.isChecked = true
            toggle() //switch state
            toggle(false) //switch without animation
            setShadowEffect(true) //disable shadow effect
            isEnabled = false //disable button
            setEnableEffect(false) //disable the switch animation
            setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
                override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                    //TODO do your job
                }
            })
        }
    }

}