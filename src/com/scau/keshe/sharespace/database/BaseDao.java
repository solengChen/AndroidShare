package com.scau.keshe.sharespace.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseDao {
	private SQLiteDatabase db;
	private BaseUtil util;
	
	
	public BaseDao(Context context) {
		util = new BaseUtil(context);
	}
	
	public boolean excuteUpdate(String sql , String...sqlArgs) {
		String SQL = MProperties.getInstance().getBySQLName(sql);
		boolean flag = false;
		try {
			db = util.getWritableDatabase();
			if(sqlArgs != null && sqlArgs.length != 0) {
				db.execSQL(SQL, sqlArgs);
				flag = true;
			}
			else {
				db.execSQL(SQL);
				flag = true;
			}
		} catch (SQLException e) {
			throw new RuntimeException("BaseDao÷¥––sqlupdate¥ÌŒÛ");
		} finally {
			if(db != null) {
				db.close();
			}
		}
		return flag;
	}
	
	public List<HashMap<String, String>> excuteQuery(String sql, String...sqlArgs) {
		
		List<HashMap<String , String >> data = null;
		
		String SQL = MProperties.getInstance().getBySQLName(sql);
		
		try {
			db = util.getReadableDatabase();
			if(db == null) {
				return null;
			}
			
			data = new ArrayList<HashMap<String, String>>();
			Cursor cursor = db.rawQuery(SQL, sqlArgs);
			String [] cursorName = cursor.getColumnNames();
			
			HashMap<String, String> row = null;
			while(cursor.moveToNext()) {
				row = new HashMap<String, String>();
				for(String columnName : cursorName) {
					int index = cursor.getColumnIndex(columnName);
					String value = cursor.getString(index);
					row.put(columnName.toLowerCase(), value);
				}
				data.add(row);
			}
		} catch (Exception e) {
			throw new RuntimeException("BaseDao÷¥––sqlQuery¥ÌŒÛ");
		} finally { 
			if(db != null) {
				db.close();
			}
		}
		
		return data;
	}
}
