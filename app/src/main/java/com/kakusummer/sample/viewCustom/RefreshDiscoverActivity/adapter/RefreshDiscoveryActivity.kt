package com.kakusummer.sample.viewCustom.RefreshDiscoverActivity.adapter

import android.R.layout
import android.graphics.Point
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assistant.bases.BaseActivity
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ActivityRefreshDiscoveryBinding
import com.kakusummer.sample.scwang.smart.refresh.header.StoreHouseHeader
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class RefreshDiscoveryActivity : BaseActivity<ActivityRefreshDiscoveryBinding>(),
    AdapterView.OnItemClickListener, OnRefreshLoadMoreListener {

    override val layoutId: Int
        get() = R.layout.activity_refresh_discovery
    override val TAG: String
        get() = "TAG_SwitchButtonActivity"

    private enum class Item(@param:StringRes var nameId: Int) {
        显示图标(R.string.item_style_store_house_icon), 显示中文(
            R.string.item_style_store_house_chinese
        ),
        显示英文(R.string.item_style_store_house_english),

    }

    private var isFirstEnter = false

    override fun initView() {
        super.initView()


        if (isFirstEnter) {
            isFirstEnter = false
            binding.refreshLayout.autoRefresh() //第一次进入触发自动刷新，演示效果
        }

        val view = findViewById<View>(R.id.recyclerView)
        if (view is RecyclerView) {
            val recyclerView = view
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.itemAnimator = DefaultItemAnimator()
            val items: MutableList<Item> = ArrayList<Item>()
            items.addAll(Item.values())
            items.addAll(Item.values())
            recyclerView.setAdapter(object : BaseRecyclerAdapter<Item?>(
                items as Collection<Item?>?,
                    layout.simple_list_item_2,
                    this@RefreshDiscoveryActivity
                ) {

                override fun onBindViewHolder(
                    holder: SmartViewHolder?,
                    model: Item?,
                    position: Int
                ) {
                    holder?.text(android.R.id.text1, model?.name)
                    model?.nameId?.let { holder?.text(android.R.id.text2, it) }
                    holder?.textColorId(android.R.id.text2, R.color.black)
                }
            })
        }
    }

    override fun initListener() {
        super.initListener()
        binding.refreshLayout.setOnRefreshLoadMoreListener(this@RefreshDiscoveryActivity)
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (Item.values()
            .get(position % Item.values().size)) {
            Item.显示中文 -> {
                val refreshHeader: RefreshHeader? =  binding.refreshLayout.getRefreshHeader()
                if (refreshHeader is StoreHouseHeader) {
                    (refreshHeader as StoreHouseHeader).initWithString("Discover")
                }
            }

            Item.显示英文 -> {
                val refreshHeader: RefreshHeader? = binding.refreshLayout.getRefreshHeader()
                if (refreshHeader is StoreHouseHeader) {
                    (refreshHeader as StoreHouseHeader).initWithString("Discover")
                }
            }

            Item.显示图标 -> {
                val refreshHeader: RefreshHeader? = binding.refreshLayout.getRefreshHeader()
                if (refreshHeader is StoreHouseHeader) {
                    (refreshHeader as StoreHouseHeader).initWithString("Discover")
                }
            }
        }
        binding.refreshLayout.autoRefresh()
    }

    private fun getPointList(): List<FloatArray>? {
        // this point is taken from https://github.com/cloay/CRefreshLayout
        val startPoints: MutableList<Point> = java.util.ArrayList()
        startPoints.add(Point(240, 80))
        startPoints.add(Point(270, 80))
        startPoints.add(Point(265, 103))
        startPoints.add(Point(255, 65))
        startPoints.add(Point(275, 80))
        startPoints.add(Point(275, 80))
        startPoints.add(Point(302, 80))
        startPoints.add(Point(275, 107))
        startPoints.add(Point(320, 70))
        startPoints.add(Point(313, 80))
        startPoints.add(Point(330, 63))
        startPoints.add(Point(315, 87))
        startPoints.add(Point(330, 80))
        startPoints.add(Point(315, 100))
        startPoints.add(Point(330, 90))
        startPoints.add(Point(315, 110))
        startPoints.add(Point(345, 65))
        startPoints.add(Point(357, 67))
        startPoints.add(Point(363, 103))
        startPoints.add(Point(375, 80))
        startPoints.add(Point(375, 80))
        startPoints.add(Point(425, 80))
        startPoints.add(Point(380, 95))
        startPoints.add(Point(400, 63))
        val endPoints: MutableList<Point> = java.util.ArrayList()
        endPoints.add(Point(270, 80))
        endPoints.add(Point(270, 110))
        endPoints.add(Point(270, 110))
        endPoints.add(Point(250, 110))
        endPoints.add(Point(275, 107))
        endPoints.add(Point(302, 80))
        endPoints.add(Point(302, 107))
        endPoints.add(Point(302, 107))
        endPoints.add(Point(340, 70))
        endPoints.add(Point(360, 80))
        endPoints.add(Point(330, 80))
        endPoints.add(Point(340, 87))
        endPoints.add(Point(315, 100))
        endPoints.add(Point(345, 98))
        endPoints.add(Point(330, 120))
        endPoints.add(Point(345, 108))
        endPoints.add(Point(360, 120))
        endPoints.add(Point(363, 75))
        endPoints.add(Point(345, 117))
        endPoints.add(Point(380, 95))
        endPoints.add(Point(425, 80))
        endPoints.add(Point(420, 95))
        endPoints.add(Point(420, 95))
        endPoints.add(Point(400, 120))
        val list: MutableList<FloatArray> = java.util.ArrayList()
        var offsetX = Int.MAX_VALUE
        var offsetY = Int.MAX_VALUE
        for (i in startPoints.indices) {
            offsetX = Math.min(startPoints[i].x, offsetX)
            offsetY = Math.min(startPoints[i].y, offsetY)
        }
        for (i in endPoints.indices) {
            val point = FloatArray(4)
            point[0] = (startPoints[i].x - offsetX).toFloat()
            point[1] = (startPoints[i].y - offsetY).toFloat()
            point[2] = (endPoints[i].x - offsetX).toFloat()
            point[3] = (endPoints[i].y - offsetY).toFloat()
            list.add(point)
        }
        return list
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        Log.d("yeTest", "onRefresh: ")
    }

    //看起来是给footer用的
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        Log.d("yeTest", "onLoadMore: ")
    }

}