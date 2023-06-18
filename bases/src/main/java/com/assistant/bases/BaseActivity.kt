package com.assistant.bases

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.assistant.resources.R

//所有Activity基类
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
    private lateinit var binding: VB
    abstract val layoutId: Int
    abstract val TAG: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this, layoutId)
        initView()
        initDate()//数据支持
        initListener()//子类中并非一定要实现，创建一个function即可
        ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary).init()
    }

    protected open fun initView() {}

    //    初始化数据,非私有加入open关键字才能复写
    protected open fun initDate() {}


    //    adapter listener
    protected open fun initListener() {
        //onBackPressed被弃用  代码中主动调需这样：onBackPressedDispatcher.onBackPressed()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("yeTest", "handleOnBackPressed: ")
                finish()
            }
        })
    }

    fun closeContent(view: View) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    fun isPermissionGranted(vararg permission: String): Boolean {
        val result = permission.filter {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        return result.size == permission.size - 1
    }

    open fun onShowPermissionRationale(permissions: Array<out String>) { //需要给出提示(业务逻辑是被禁止时给出就行)
    }

    private fun shouldShowPermissionRationale(vararg permission: String): Boolean { //未申请 或 被禁止(拒绝+不再提示)，返回false;只有被拒绝后返回true
        val result = permission.filter {
            !ActivityCompat.shouldShowRequestPermissionRationale(this, it)
        }
        return result.size == permission.size
    }

    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionResults ->
            val granted = permissionResults.filter { it.value }
            val isGranted = granted.size == permissionResults.size - 1//9.0前无前景 9.0后无Log
            permissionCallback?.invoke(isGranted)
            val permissions = permissionResults.keys.toTypedArray()
            if (!isGranted && shouldShowPermissionRationale(*permissions)) {
                onShowPermissionRationale(permissions)
            }
        }

    private var permissionCallback: ((hasGrant: Boolean) -> Unit)? = null

    fun startRequestPermission(
        vararg permissions: String,
        callback: ((hasGrant: Boolean) -> Unit)
    ) {
        if (permissions.isEmpty()) return
        permissionCallback = callback
        permissionLauncher.launch(permissions as Array<String>?)
    }

    override fun finish() {
        super.finish()
        //overridePendingTransition(0,R.anim.anim_out_down )
    }
}