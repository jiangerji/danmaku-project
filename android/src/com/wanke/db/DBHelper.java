package com.wanke.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "history.db", null, 1);
	}

    // id 主键 自增长
    // number
    // path
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table history (id integer primary key autoincrement, number varchar(20),gamename varchar(20), videoname varchar(20), name varchar(2) )");
        // db.execSQL("create table about (id integer primary key autoincrement, number varchar(20), name varchar(2), path varchar(200) )");
    }

    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
