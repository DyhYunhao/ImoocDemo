package com.example.databinderdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinderdemo1.bean.ObservableGoodsBean
import com.example.databinderdemo1.databinding.Demo5Binding

/**
 * 双向数据绑定
 * 双向绑定的意思即为当数据改变时同时使视图刷新，而视图改变时也可以同时改变数据
 */
class DemoActivity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: Demo5Binding = DataBindingUtil.setContentView(this, R.layout.activity_demo5)

        val goods = ObservableGoodsBean("code", "coding", 23F)
        binding.goods = goods
    }
}