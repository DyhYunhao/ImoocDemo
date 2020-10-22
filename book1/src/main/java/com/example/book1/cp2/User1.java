package com.example.book1.cp2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.loader.content.Loader;

/*********************************************
 * @author daiyh
 * 创建日期：2020-10-20
 * 描述：
 *********************************************
 */
public class User1 implements Parcelable {

    public int userId;
    public String userName;
    public boolean isMale;

    public User1(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    public static final Creator<User1> CREATOR = new Creator<User1>() {
        @Override
        public User1 createFromParcel(Parcel in) {
            return new User1(in);
        }

        @Override
        public User1[] newArray(int size) {
            return new User1[size];
        }
    };

    private User1(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userId);
        parcel.writeString(userName);
        parcel.writeInt(isMale ? 1 : 0);
    }
}
