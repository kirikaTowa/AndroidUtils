package com.assistant.bases


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


/*
* 继承Fragment 所有Fragment基类
* */
abstract class BaseFragment : Fragment(){
    //初始化
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()//此方法并非实例具体要实现
    }

    //显示一个布局
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView()
    }

    //让子类传参 获取布局view 直接变抽象方法就可以现在不设置
    abstract fun initView(): View?

    //fragment初始化
    protected fun init() {

    }

    //adapter与数据加载
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //adapter与 数据的加载
        initListener()
        initData()
    }

    //数据的初始化
    protected open fun initData() {
    }

    //adapter与listener间的操作
    protected open fun initListener() {
    }
}
