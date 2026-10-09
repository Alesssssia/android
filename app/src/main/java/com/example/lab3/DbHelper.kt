package com.example.lab3
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.KeyFactory
import java.sql.SQLData

class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "app", factory, 3) {
    override fun onCreate(db: SQLiteDatabase?) {

        val query = "CREATE TABLE users(id INT PRIMARY KEY, login TEXT, email TEXT, phone TEXT, pass Text)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {

        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("phone", user.phone)
        values.put("pass", user.pass)

        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }


}