package com.example.databinderdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import com.example.databinderdemo1.bean.GoodsBean
import com.example.databinderdemo1.databinding.Demo2Binding
import kotlin.math.log
import kotlin.random.Random

class DemoActivity2 : AppCompatActivity() {

    private final val TAG = "DemoActivity2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: Demo2Binding = DataBindingUtil.setContentView(this, R.layout.activity_demo2)

        val goods = GoodsBean()
        goods.name = "code"
        goods.details = "xyz"
        goods.price = 25F

        binding.goods = goods
        binding.goodsHandler = GoodsHandler(goods)

        goods.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                when (propertyId) {
                    BR.name -> {
                        Log.d(TAG, "BR.name: ")
                    }
                    BR.details -> {
                        Log.d(TAG, "BR.details")
                    }
                    BR._all -> {
                        Log.d(TAG, "BR._all")
                    }
                    else -> {
                        Log.d(TAG, "未知")
                    }
                }
            }

        })
    }

    class GoodsHandler(private var goodsBean: GoodsBean) {
        fun changeGoodsName(){
            goodsBean.name = "code" + Random.nextInt()
        }

        fun changeGoodsDetails() {
            goodsBean.price = Random.nextFloat()
            goodsBean.details = "xyz" + Random.nextInt()
        }
    }
}