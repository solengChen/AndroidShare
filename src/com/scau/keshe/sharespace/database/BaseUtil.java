package com.scau.keshe.sharespace.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseUtil extends SQLiteOpenHelper {

	private static int version = MProperties.getInstance().getVersion();
	private static String dbName = MProperties.getInstance().getDBName();
	
	
	public BaseUtil(Context context) {
		super(context, dbName, null, version);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(MProperties.getInstance().getBySQLName("create"));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(MProperties.getInstance().getBySQLName("drop"));
		db.execSQL(MProperties.getInstance().getBySQLName("create"));
	}

}
