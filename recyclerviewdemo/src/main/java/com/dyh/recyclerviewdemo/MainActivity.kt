package com.dyh.recyclerviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var mRcvDemo1: RecyclerView
    private lateinit var mDemo1Adapter: Demo1Adapter
    var colors = arrayOf(android.R.color.holo_red_dark,
                        android.R.color.holo_blue_dark,
                        android.R.color.holo_orange_dark)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initData()

    }

    fun initView() {
        mRcvDemo1 = findViewById(R.id.rcv_demo1)
        mRcvDemo1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mDemo1Adapter = Demo1Adapter(this)
        mRcvDemo1.adapter = mDemo1Adapter
    }

    fun initData() {
        var list = ArrayList<DataModel>()
        for (i in 0 .. 20) {
            val type = (Math.random() * 3 + 1).toInt()
            val data = DataModel()
            data.avatarColor = colors[type - 1]
            data.type = type
            data.name = "name: $i"
            data.content = "content: $i"
            data.contentColor = colors[(type + 1) % 3]
            list.add(data)
        }
        mDemo1Adapter.addList(list)
        mDemo1Adapter.notifyDataSetChanged()
    }
}