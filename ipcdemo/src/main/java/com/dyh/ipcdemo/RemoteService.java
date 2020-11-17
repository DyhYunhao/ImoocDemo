package com.dyh.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dyh.ipcdemo.entity.Message;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*********************************************
 * @author daiyh
 * 创建日期：2020-11-13
 * 描述：管理和提供子进程的链接和消息服务
 *********************************************
 */
public class RemoteService extends Service {

    private boolean isConnected = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private RemoteCallbackList<MessageReceiveListener> messageReceiveListenerRemoteCallbackList = new RemoteCallbackList<>();
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ScheduledFuture scheduledFuture;

    private IConnectionService connectionService = new IConnectionService.Stub() {
        @Override
        public void connect() throws RemoteException {
            try {
                Thread.sleep(5000);
                isConnected = true;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RemoteService.this, "connect", Toast.LENGTH_SHORT).show();
                    }
                });
                scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        int size = messageReceiveListenerRemoteCallbackList.beginBroadcast();
                        for (int i = 0; i < size; i ++) {
                            Message message = new Message();
                            message.setContent("this message is from remote");
                            try {
                                messageReceiveListenerRemoteCallbackList.getBroadcastItem(i).onReceiveMessage(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        messageReceiveListenerRemoteCallbackList.finishBroadcast();
                    }
                }, 5000, 5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void disconnect() throws RemoteException {
            isConnected = false;
            scheduledFuture.cancel(true);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "disconnect", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public boolean isConnected() throws RemoteException {
            return isConnected;
        }
    };

    private IMessageService messageService = new IMessageService.Stub() {
        @Override
        public void sendMessage(Message message) throws RemoteException {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(RemoteService.this, message.getContent(), Toast.LENGTH_SHORT).show();
//                }
//            });
            Log.d("RemoteService", message.getContent());

            if (isConnected) {
                message.setSendSuccess(true);
            } else {
                message.setSendSuccess(false);
            }
        }

        @Override
        public void registerMessageReceiveListener(MessageReceiveListener messageReceiveLisetener) throws RemoteException {
            if (messageReceiveLisetener != null) {
                messageReceiveListenerRemoteCallbackList.register(messageReceiveLisetener);
            }
        }

        @Override
        public void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiveLisetener) throws RemoteException {
            if (messageReceiveLisetener != null) {
                messageReceiveListenerRemoteCallbackList.unregister(messageReceiveLisetener);
            }
        }
    };

    private IServiceManager serviceManager = new IServiceManager.Stub() {

        @Override
        public IBinder getService(String serviceName) throws RemoteException {
            if (IConnectionService.class.getSimpleName().equals(serviceName)) {
                return connectionService.asBinder();
            } else if (IMessageService.class.getSimpleName().equals(serviceName)) {
                return messageService.asBinder();
            } else {
                return null;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceManager.asBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }
}
