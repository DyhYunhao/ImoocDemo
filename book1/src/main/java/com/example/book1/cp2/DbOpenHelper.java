package com.example.book1.cp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.security.PublicKey;
import java.sql.SQLClientInfoException;

/*********************************************
 * @author daiyh
 * 创建日期：2020-10-21
 * 描述：
 *********************************************
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAEM = "book_provider.db";
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";
    private static final int DB_VERSION = 1;

    //创建表
    private String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " + BOOK_TABLE_NAME
            + "(_id INTEGER PRIMARY KEY," + "name TEXT)";
    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME
            + "(_id INTEGER PRIMARY KEY," + "name TEXT, sex INT)";

    public DbOpenHelper(@Nullable Context context) {
        super(context, DB_NAEM, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
