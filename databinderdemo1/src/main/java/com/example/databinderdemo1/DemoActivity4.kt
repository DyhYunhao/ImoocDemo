package com.example.databinderdemo1

import android.database.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableList
import com.example.databinderdemo1.databinding.Demo4Binding
import kotlin.random.Random

/**
 * DataBinding 也提供了包装类用于替代原生的 List 和 Map，
 * 分别是 ObservableList 和 ObservableMap,当其包含的数据发生变化时，绑定的视图也会随之进行刷新
 */
class DemoActivity4 : AppCompatActivity() {

    private val map = ObservableArrayMap<String, String>().apply {
        put("name", "code")
        put("age", "25")
    }

    private val list = ObservableArrayList<String>().apply {
        add("hello")
        add("world")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: Demo4Binding = DataBindingUtil.setContentView(this, R.layout.activity_demo4)
        binding.map = map
        binding.list = list
        binding.index = 0
        binding.key = "name"
    }

    fun onClick(view: View) {
        map["name"] = "code," + Random.nextInt(100)
    }
}