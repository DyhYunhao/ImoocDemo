package com.example.databinderdemo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_dan_1.setOnClickListener {
            startActivity(Intent(this, DemoActivity1::class.java))
        }

        btn_ui_2.setOnClickListener {
            startActivity(Intent(this, DemoActivity2::class.java))
        }

        btn_ui_3.setOnClickListener {
            startActivity(Intent(this, DemoActivity3::class.java))
        }

        btn_observableCollection.setOnClickListener {
            startActivity(Intent(this, DemoActivity4::class.java))
        }

        btn_demo_5.setOnClickListener {
            startActivity(Intent(this, DemoActivity5::class.java))
        }

        btn_demo_6.setOnClickListener {
            startActivity(Intent(this, DemoActivity6::class.java))
        }

        btn_demo_7.setOnClickListener {
            startActivity(Intent(this, DemoActivity7::class.java))
        }

        btn_demo_8.setOnClickListener {
            startActivity(Intent(this, DemoActivity8::class.java))
        }
    }
}
