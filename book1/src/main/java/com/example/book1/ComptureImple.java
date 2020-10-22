package com.example.book1;

import android.os.RemoteException;

/*********************************************
 * @author daiyh
 * 创建日期：2020-10-22
 * 描述：
 *********************************************
 */
public class ComptureImple extends ICompture.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
