// IServiceManager.aidl
package com.dyh.ipcdemo;

interface IServiceManager {
    IBinder getService(String serviceName);
}