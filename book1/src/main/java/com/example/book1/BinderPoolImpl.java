package com.example.book1;

import android.os.IBinder;
import android.os.RemoteException;

/*********************************************
 * @author daiyh
 * 创建日期：2020-10-22
 * 描述：
 *********************************************
 */
public class BinderPoolImpl extends IBinderPool.Stub {

    public BinderPoolImpl() {
        super();
    }

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        switch (binderCode) {
            case 0:
                binder = new SecurityCenterImpl();
                break;
            case 1:
                binder = new ComptureImple();
                break;
            default:
                break;
        }
        return binder;
    }
}
