package com.example.mmkvdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tencent.mmkv.MMKV

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var rootDir = MMKV.initialize(this)
        Log.d(TAG, "onCreate: mmkv root: $rootDir")

        var kv = MMKV.defaultMMKV()
        kv.encode("bool", true)
        Log.d(TAG, "onCreate: bool: ${kv.decodeBool("bool")}" )

        kv.encode("int", Int.MAX_VALUE)
        Log.d(TAG, "onCreate: int: ${kv.decodeInt("int")}")

        kv.encode("long", Long.MAX_VALUE)
        Log.d(TAG, "onCreate: long: ${kv.decodeLong("long")}")

        kv.removeValueForKey("bool")
        Log.d(TAG, "onCreate: remove bool: ${kv.decodeBool("bool")}" )

        
    }
}