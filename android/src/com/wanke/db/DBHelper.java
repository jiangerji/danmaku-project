package com.wanke.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "info.db";

    public static final String TABLE_NAME = "history";

    public static final String ID = "_id";
    public static final String ROOM_ID = "roomId";
    public static final String ROOM_NAME = "roomName";
    public static final String GAME_NAME = "gameName";
    public static final String OWNER_NICKNAME = "ownerNickname";
    public static final String FANS = "fans";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE  TABLE  IF NOT EXISTS history ("
                + ID + " INTEGER PRIMARY KEY  NOT NULL  UNIQUE , "
                + ROOM_ID + " TEXT NOT NULL , "
                + ROOM_NAME + " TEXT NOT NULL , "
                + GAME_NAME + " TEXT NOT NULL , "
                + OWNER_NICKNAME + " TEXT NOT NULL , "
                + FANS + " INTEGER NOT NULL )");
        // db.execSQL("create table about (id integer primary key autoincrement, number varchar(20), name varchar(2), path varchar(200) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
