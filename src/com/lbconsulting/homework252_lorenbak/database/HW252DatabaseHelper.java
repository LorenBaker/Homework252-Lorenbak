package com.lbconsulting.homework252_lorenbak.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lbconsulting.homework252_lorenbak.MyLog;

public class HW252DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "HW252.db";
	private static final int DATABASE_VERSION = 1;

	public HW252DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		MyLog.i("HW252DatabaseHelper", "onCreate");
		TasksTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		MyLog.i("HW252DatabaseHelper", "onUpgrade");
		TasksTable.onUpgrade(database, oldVersion, newVersion);
	}

}
