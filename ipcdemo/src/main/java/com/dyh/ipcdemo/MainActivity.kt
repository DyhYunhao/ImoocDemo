package com.dyh.ipcdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dyh.ipcdemo.entity.Message

class MainActivity : AppCompatActivity() {

    private lateinit var mBtnConnect: Button
    private lateinit var mBtnDisConnect: Button
    private lateinit var mBtnIsConnected: Button
    private lateinit var mBtnSendMesage: Button
    private lateinit var mBtnRegisterListener: Button
    private lateinit var mBtnUnRegisterListener: Button

    private lateinit var connectionServiceProxy: IConnectionService
    private lateinit var messageServiceProxy: IMessageService
    private lateinit var serviceMangerProxy: IServiceManager

    private var messageReceiveListener = object: MessageReceiveListener.Stub(){
        override fun onReceiveMessage(message: Message?) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this@MainActivity, message?.content, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initView
        mBtnConnect = findViewById(R.id.btn_connect)
        mBtnDisConnect = findViewById(R.id.btn_disconnect)
        mBtnIsConnected = findViewById(R.id.btn_isConnected)
        mBtnSendMesage = findViewById(R.id.btn_send_mesage)
        mBtnRegisterListener = findViewById(R.id.btn_register_listener)
        mBtnUnRegisterListener = findViewById(R.id.btn_unRegister_listener)
        //--------------------------------------------------

        //业务
        val intent: Intent = Intent(this, RemoteService::class.java)
        bindService(intent, object : ServiceConnection {
            override fun onServiceDisconnected(p0: ComponentName?) {
            }

            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                serviceMangerProxy = IServiceManager.Stub.asInterface(p1)
                connectionServiceProxy = IConnectionService.Stub.asInterface(
                                serviceMangerProxy.getService(IConnectionService::class.java.simpleName))
                messageServiceProxy = IMessageService.Stub.asInterface(
                                serviceMangerProxy.getService(IMessageService::class.java.simpleName))
            }

        }, Context.BIND_AUTO_CREATE)

        //点击
        mBtnConnect.setOnClickListener {
            connectionServiceProxy.connect()
        }
        mBtnDisConnect.setOnClickListener {
            connectionServiceProxy.disconnect()
        }
        mBtnIsConnected.setOnClickListener {
            var isConnected = connectionServiceProxy.isConnected()
            Toast.makeText(this, isConnected.toString(), Toast.LENGTH_SHORT).show()
        }
        mBtnSendMesage.setOnClickListener {
            var message = Message()
            message.content = "message send from main"
            messageServiceProxy.sendMessage(message)
            Log.d("MainActivity", message.isSendSuccess.toString())
        }
        mBtnRegisterListener.setOnClickListener {
            messageServiceProxy.registerMessageReceiveListener(messageReceiveListener)
        }
        mBtnUnRegisterListener.setOnClickListener {
            messageServiceProxy.unRegisterMessageReceiveListener(messageReceiveListener)
        }
    }

}