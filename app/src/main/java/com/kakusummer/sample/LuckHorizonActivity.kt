package com.kakusummer.sample

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakusummer.androidutils.R
import com.kakusummer.sample.adapter.PrizeAdapter


//可循环滚动
//自动横向滚动半成品
class LuckyHorizonActivity : AppCompatActivity() {

    val LOOP_COUNT: Int = 4 // 循环次数
    val SCROLL_DURATION: Int = 500 // 每圈滚动时间（毫秒）
    private val totalItems = 7 // 总项目数


    private var recyclerView: RecyclerView? = null
    private var btnSpin: Button? = null
    private var adapter: PrizeAdapter? = null

    private var currentPosition = 0 // 跟踪当前滚动位置
    val prizes: List<String> = mutableListOf("奖品1", "奖品2", "奖品3", "奖品4", "奖品5", "奖品6", "奖品7")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lucky_horizon)


        // 数据源是原始数据的两倍，形成循环
        val loopedData: MutableList<String> = ArrayList<String>(prizes)
        loopedData.addAll(prizes) // 复制一份

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        btnSpin = findViewById<Button>(R.id.btnSpin)


        adapter = PrizeAdapter(loopedData)


        // 设置 RecyclerView 为横向滚动并初始化数据
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.setLayoutManager(layoutManager)

        recyclerView?.setAdapter(adapter)
        setupScrollListener(layoutManager);


        btnSpin?.setOnClickListener { v ->
            // 这里实现滚动效果
            startScrolling();
        }
    }

    private fun setupScrollListener(layoutManager: LinearLayoutManager) {
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // 检查是否滚动到最前或最后
                if (firstVisibleItemPosition == 0) {
                    // 如果滑动到第一个 item
                    recyclerView.scrollToPosition(totalItems - 1)
                } else if (lastVisibleItemPosition == totalItems - 1) {
                    // 如果滑动到最后一个 item
                    recyclerView.scrollToPosition(0)
                }
            }
        })
    }

    //自动滚动核心代码
    private fun startScrolling() {
        val handler = Handler()

        // 每个滚动的时间间隔
        val interval: Int = SCROLL_DURATION / totalItems

        for (i in 0 until LOOP_COUNT * totalItems) {
            val position: Int = i % totalItems // 计算目标位置

            handler.postDelayed({
                recyclerView!!.smoothScrollToPosition(position)
            }, (i * interval).toLong())
        }

        // 最后停在第6个位置
        handler.postDelayed({
            recyclerView!!.smoothScrollToPosition(5) // 停在第6个位置，索引从0开始
        }, ((LOOP_COUNT * totalItems * interval).toLong())) // 设置停留时间
    }

}