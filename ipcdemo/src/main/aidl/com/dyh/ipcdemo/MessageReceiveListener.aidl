// MessageReceiveListener.aidl
package com.dyh.ipcdemo;
import com.dyh.ipcdemo.entity.Message;

interface MessageReceiveListener {
    void onReceiveMessage(in Message message);
}