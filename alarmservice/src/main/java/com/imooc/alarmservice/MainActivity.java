package com.imooc.alarmservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, MyService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Log.d("dyh", "onCreate: at " + new Date().toString());
//        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 5 * 1000, pendingIntent);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("dyh", "onDestroy: at " + new Date().toString());
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 5 * 1000, pendingIntent);
        super.onDestroy();

    }
}
