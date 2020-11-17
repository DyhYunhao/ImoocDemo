// IMessageService.aidl
package com.dyh.ipcdemo;
import com.dyh.ipcdemo.entity.Message;
import com.dyh.ipcdemo.MessageReceiveListener;

// 消息服务
interface IMessageService {

    void sendMessage(in Message message);

    void registerMessageReceiveListener(MessageReceiveListener messageReceiveLisetener);

    void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiveLisetener);

}