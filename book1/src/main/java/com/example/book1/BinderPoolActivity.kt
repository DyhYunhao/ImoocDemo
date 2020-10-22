package com.example.book1

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class BinderPoolActivity : AppCompatActivity() {
    private val TAG = "BinderPoolActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binder_pool)
        enc()
    }

    fun enc() {
        Thread {
            var iBinderPool = BinderPool.getInstance(this)
            var securityBinder = iBinderPool.queryBinder(0)
            var securityCenter = ISecurityCenter.Stub.asInterface(securityBinder)
            Log.d(TAG, "enc: visit SecurityCenter")
            var msg = "hello-android"
            println("content: $msg")
            try {
                var password = securityCenter.encrypt(msg)
                println("encrypt；$password")
                println("decrypt；" + securityCenter.decrypt(password))
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

            Log.d(TAG, "enc: visit Compute")
            var comptureBinder = iBinderPool.queryBinder(1)
            var compute = ComptureImple.asInterface(comptureBinder)
            try {
                println("3 + 5 =；" + compute.add(3, 5))
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }.start()
    }
}