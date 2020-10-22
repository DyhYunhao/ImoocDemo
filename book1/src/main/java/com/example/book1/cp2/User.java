package com.example.book1.cp2;

import java.io.PipedReader;
import java.io.Serializable;
import java.security.PublicKey;

/*********************************************
 * @author daiyh
 * 创建日期：2020-10-20
 * 描述：序列化
 *********************************************
 */
public class User implements Serializable {

    private static final long serialVersionUID = 8934902996925029313L;

    public int userId;
    public String userName;
    public boolean isMale;

}
