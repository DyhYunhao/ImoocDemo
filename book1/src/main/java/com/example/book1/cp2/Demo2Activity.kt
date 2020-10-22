package com.example.book1.cp2

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.book1.R
import kotlin.math.log

class Demo2Activity : AppCompatActivity() {

    val TAG = "Demo2Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo2)

//        var uri = Uri.parse("content://com.example.book1.cp2")
//        contentResolver.query(uri, null, null, null, null)
//        contentResolver.query(uri, null, null, null, null)
//        contentResolver.query(uri, null, null, null, null)

        var bookUri = Uri.parse("content://com.example.book1.cp2/book")
        var values: ContentValues = ContentValues()
        values.put("_id", 6)
        values.put("name", "c++")
        contentResolver.insert(bookUri, values)
        var bookCursor = contentResolver.query(bookUri, arrayOf("_id", "name"), null, null, null)
        while (bookCursor!!.moveToNext()) {
            var book: Book = Book(bookCursor.getInt(0), bookCursor.getString(1))
            Log.d(TAG, "query book: $book")
        }
        bookCursor.close()

        var userUri = Uri.parse("content://com.example.book1.cp2/user")
        values.put("_id", 6)
        values.put("name", "c++")
        contentResolver.insert(bookUri, values)
        var userCursor = contentResolver.query(userUri, arrayOf("_id", "name", "sex"), null, null, null)
        while (userCursor!!.moveToNext()) {
            var user: User2 = User2(userCursor.getInt(0), userCursor.getString(1), userCursor.getInt(2))
            Log.d(TAG, "query book: $user")
        }
        userCursor.close()
    }
}