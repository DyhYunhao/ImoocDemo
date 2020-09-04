package com.example.autoupdate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.autoupdate.update.UpdateService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_view.setOnClickListener(View.OnClickListener {
            checkVersion()
        })
//        checkVersion()
    }

    /**
     * 检查是否有新版本可用
     */
    private fun checkVersion() {
        var intent: Intent = Intent(this, UpdateService::class.java)
        intent.putExtra("lastVersion", "4.2.2")
        startService(intent)
    }
}
