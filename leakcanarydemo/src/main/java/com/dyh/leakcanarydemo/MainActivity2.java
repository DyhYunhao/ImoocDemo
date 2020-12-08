package com.dyh.leakcanarydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {

    //tatic这个关键字使一个变量变为只和这个类相关的类变量，和实例无关。
    // 他的生命周期是很长的，贯穿于app的启动到关闭。因此只要用一个static引用一个大对象，就可以泄漏了
    static Context leakContextRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        leakThread();
        leakRef();
    }

    void leakThread() {
        new Thread() {
            @Override
            public void run() {
                while (true) {

                }
            }
        }.start();
    }

    void leakRef() {
        leakContextRef = this;
    }

}