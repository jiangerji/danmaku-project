package com.wanke.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wanke.db.DBHelper;
import com.wanke.model.HistoryInfo;

public class HistoryDao {
    private final static String TAG = "history";

    private DBHelper mDBHelper;

    public HistoryDao(Context context) {
        mDBHelper = new DBHelper(context);
    }

    //    /**
    //     * 添加一条记录到数据
    //     * 
    //     * @param path
    //     * @param number
    //     * @param name
    //     */
    //    public boolean add(
    //            int number, String name, String videoname, String gamename) {
    //        SQLiteDatabase db = mDBHelper.getWritableDatabase();
    //        ContentValues values = new ContentValues();
    //        values.put("number", number);
    //        values.put("name", name);
    //        values.put("videoname", videoname);
    //        values.put("gamename", gamename);
    //        long result = db.insert("history", null, values);
    //        db.close();
    //        if (result > 0) {
    //            return true;
    //        } else {
    //            return false;
    //        }
    //    }

    /**
     * 向历史记录添加
     * 
     * @param roomId
     * @param roomName
     * @param gameName
     * @param ownerNickname
     * @param fans
     * @return
     */
    public boolean add(
            int roomId, String roomName, String gameName, String ownerNickname,
            int fans) {
        SQLiteDatabase db = null;

        long result = -1;
        try {
            db = mDBHelper.getWritableDatabase();

            // 先删除
            String[] args = { String.valueOf(roomId) };
            try {
                db.delete("history", DBHelper.ROOM_ID + "=?", args);
            } catch (Exception e) {

            }

            ContentValues values = new ContentValues();
            values.put(DBHelper.ROOM_ID, "" + roomId);

            values.put(DBHelper.ROOM_NAME, roomName);
            values.put(DBHelper.GAME_NAME, gameName);
            values.put(DBHelper.OWNER_NICKNAME, ownerNickname);
            values.put(DBHelper.FANS, fans);
            result = db.insert("history", null, values);
        } catch (Exception e) {
            Log.d(TAG, "add exception:" + e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }

        if (result >= 0) {
            return true;
        } else {
            return false;
        }
    }

    //    /**
    //     * 查找一条记录是否在数据库
    //     */
    //    public boolean find(String name) {
    //        boolean result = false;
    //        SQLiteDatabase db = mDBHelper.getReadableDatabase();
    //        Cursor cursor = db.rawQuery("select id from history where name=?",
    //                new String[] { name });
    //        if (cursor.moveToFirst()) {
    //            result = true;
    //        }
    //        cursor.close();
    //        db.close();
    //        return result;
    //    }

    //    /**
    //     * 移除一条记录.
    //     * 
    //     * @param number
    //     * @return
    //     */
    //    public boolean delete(String name) {
    //        SQLiteDatabase db = mDBHelper.getWritableDatabase();
    //        int result = db.delete("history", "name=?", new String[] { name });
    //        db.close();
    //        if (result > 0) {
    //            return true;
    //        } else {
    //            return false;
    //        }
    //    }

    /**
     * 删除全部记录
     * 
     * @return
     */
    public void deleteAll() {
        SQLiteDatabase db = null;
        try {
            db = mDBHelper.getWritableDatabase();
            db.delete("history", null, null);
        } catch (Exception e) {
            Log.d(TAG, "delete all exception:" + e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 查询全部的信息
     * 
     * @return
     */
    public List<HistoryInfo> findAll() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<HistoryInfo> mHistoryInfo = new ArrayList<HistoryInfo>();

        try {
            db = mDBHelper.getReadableDatabase();

            cursor = db.query(DBHelper.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DBHelper.ID + " desc");

            while (cursor.moveToNext()) {
                HistoryInfo info = new HistoryInfo();
                info.setRoomId(cursor.getString(cursor.getColumnIndex(DBHelper.ROOM_ID)));
                info.setRoomName(cursor.getString(cursor.getColumnIndex(DBHelper.ROOM_NAME)));
                info.setGameName(cursor.getString(cursor.getColumnIndex(DBHelper.GAME_NAME)));
                info.setOwnerNickname(cursor.getString(cursor.getColumnIndex(DBHelper.OWNER_NICKNAME)));
                info.setFans(cursor.getInt(cursor.getColumnIndex(DBHelper.FANS)));
                mHistoryInfo.add(info);
            }
        } catch (Exception e) {
            Log.d(TAG, "find all exception:" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (db != null) {
                db.close();
            }
        }

        return mHistoryInfo;
    }

    //    /**
    //     * 查询部分信息
    //     * 
    //     * @param startIndex
    //     *            从第几条开始获取数据
    //     * @param maxNumber
    //     *            最多获取多少条数据
    //     */
    //    public List<HistoryInfo>
    //            findPartHistoryInfos(int startIndex, int maxNumber) {
    //        SQLiteDatabase db = mDBHelper.getReadableDatabase();
    //        List<HistoryInfo> mHistoryInfo = new ArrayList<HistoryInfo>();
    //        Cursor cursor = db.rawQuery(
    //                "select number,name,gamename,videoname, from history limit ? offset ?",
    //                new String[] { maxNumber + "", startIndex + "" });
    //        try {
    //            Thread.sleep(1000);
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //        while (cursor.moveToNext()) {
    //            String number = cursor.getString(0);
    //            String name = cursor.getString(1);
    //            String gamename = cursor.getString(2);
    //            String videoname = cursor.getString(3);
    //            HistoryInfo info = new HistoryInfo(number,
    //                    name,
    //                    gamename,
    //                    videoname);
    //            mHistoryInfo.add(info);
    //        }
    //        db.close();
    //        return mHistoryInfo;
    //    }
    //
    //    /**
    //     * 按页返回数据.
    //     */
    //    public List<HistoryInfo> findHistoryInfosByPage(
    //            int pageNumber, int pageSize) {
    //        SQLiteDatabase db = mDBHelper.getReadableDatabase();
    //        List<HistoryInfo> mHistoryInfo = new ArrayList<HistoryInfo>();
    //        // 第 1页 0 ~ 19 数据
    //        // 第2 页 20 ~ 39 数据.
    //        // 第n页 (pagenumber-1)*pageSize ~ pagenumber* pagesize-1
    //        int startIndex = (pageNumber - 1) * pageSize;
    //        Cursor cursor = db
    //                .rawQuery(
    //                        "select number,name,gamename,videoname from history order by  id desc limit ? offset ? ",
    //                        new String[] { pageSize + "", startIndex + "" });
    //        try {
    //            Thread.sleep(1000);
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //        while (cursor.moveToNext()) {
    //            String number = cursor.getString(0);
    //            String name = cursor.getString(1);
    //            String gamename = cursor.getString(2);
    //            String videoname = cursor.getString(3);
    //            HistoryInfo info = new HistoryInfo(number,
    //                    name,
    //                    gamename,
    //                    videoname);
    //            mHistoryInfo.add(info);
    //        }
    //        db.close();
    //        return mHistoryInfo;
    //    }

    /**
     * 返回数据库 里面的内容 可以显示多少页
     * 
     * @param pageSize
     *            每一页显示多少条数据
     */
    public int getTotalPageNumber(int pageSize) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from history", null);
        // 获取数据库一共有多少条记录
        int total = cursor.getCount();
        cursor.close();
        db.close();
        if (total % pageSize == 0) {
            return total / pageSize;
        } else {
            return total / pageSize + 1;
        }
    }
}
