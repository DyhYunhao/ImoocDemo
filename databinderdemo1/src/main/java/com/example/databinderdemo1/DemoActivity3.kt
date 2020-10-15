package com.example.databinderdemo1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.databinderdemo1.bean.ObservableGoodsBean
import com.example.databinderdemo1.databinding.Demo3Binding
import kotlin.random.Random

class DemoActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: Demo3Binding =
            DataBindingUtil.setContentView(this, R.layout.activity_demo3)
        val observableGoodsBean = ObservableGoodsBean("code", "hello", 25F)
        binding.observableGoods = observableGoodsBean
        binding.observableGoodsHandler = ObservableGoodsHandler(observableGoodsBean)
    }

    class ObservableGoodsHandler(private var observableGoodsBean: ObservableGoodsBean) {
        fun changeGoodsName() {
            observableGoodsBean.name.set("code" + Random.nextInt(100))
        }

        fun changeGoodsDetails() {
            observableGoodsBean.price.set(Random.nextFloat())
            observableGoodsBean.details.set("xyz" + Random.nextInt(100))
        }
    }
}