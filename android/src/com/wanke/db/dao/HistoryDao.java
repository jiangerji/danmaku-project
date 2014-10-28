package com.wanke.db.dao;

import java.util.ArrayList;
import java.util.List;
import com.wanke.db.DBHelper;
import com.wanke.model.HistoryInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HistoryDao {

	private Context context;
	private DBHelper mDBHelper;

	public HistoryDao(Context context) {
		this.context = context;
		mDBHelper = new DBHelper(context);
	}

    /**
     * 添加一条记录到数据
     * 
     * @param path
     * @param number
     * @param name
     */
    public boolean add(int number, String name, String path) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("name", name);
        values.put("path", path);
        long result = db.insert("history", null, values);
        db.close();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

	/**
	 * 查找一条记录是否在数据库
	 */
	public boolean find(String name) {
		boolean result = false;
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select id from history where name=?",
				new String[] { name });
		if (cursor.moveToFirst()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}

	/**
	 * 移除一条记录.
	 * 
	 * @param number
	 * @return
	 */
	public boolean delete(String name) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int result = db.delete("history", "name=?", new String[] { name });
		db.close();
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询全部的信息
	 * 
	 * @return
	 */
	public List<HistoryInfo> findAll() {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<HistoryInfo> mHistoryInfo = new ArrayList<HistoryInfo>();
		Cursor cursor = db.rawQuery("select number,name,path from history",
				null);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (cursor.moveToNext()) {
			String number = cursor.getString(0);
			String name = cursor.getString(1);
			String path = cursor.getString(2);
			HistoryInfo info = new HistoryInfo(number, name, path);
			mHistoryInfo.add(info);
		}
		db.close();
		return mHistoryInfo;
	}

	/**
	 * 查询部分信息
	 * 
	 * @param startIndex
	 *            从第几条开始获取数据
	 * @param maxNumber
	 *            最多获取多少条数据
	 */
	public List<HistoryInfo> findPartHistoryInfos(int startIndex, int maxNumber) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<HistoryInfo> mHistoryInfo = new ArrayList<HistoryInfo>();
		Cursor cursor = db.rawQuery(
				"select number,name,path from history limit ? offset ?",
				new String[] { maxNumber + "", startIndex + "" });
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (cursor.moveToNext()) {
			String number = cursor.getString(0);
			String name = cursor.getString(1);
			String path = cursor.getString(2);
			HistoryInfo info = new HistoryInfo(number, name, path);
			mHistoryInfo.add(info);
		}
		db.close();
		return mHistoryInfo;
	}

	/**
	 * 按页返回数据.
	 */
	public List<HistoryInfo> findHistoryInfosByPage(int pageNumber, int pageSize) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<HistoryInfo> mHistoryInfo = new ArrayList<HistoryInfo>();
		// 第 1页 0 ~ 19 数据
		// 第2 页 20 ~ 39 数据.
		// 第n页 (pagenumber-1)*pageSize ~ pagenumber* pagesize-1
		int startIndex = (pageNumber - 1) * pageSize;
		Cursor cursor = db
				.rawQuery(
						"select number,name,path from history order by  id desc limit ? offset ? ",
						new String[] { pageSize + "", startIndex + "" });
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (cursor.moveToNext()) {
			String number = cursor.getString(0);
			String name = cursor.getString(1);
			String path = cursor.getString(2);
			HistoryInfo info = new HistoryInfo(number, name, path);
			mHistoryInfo.add(info);
		}
		db.close();
		return mHistoryInfo;
	}

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
