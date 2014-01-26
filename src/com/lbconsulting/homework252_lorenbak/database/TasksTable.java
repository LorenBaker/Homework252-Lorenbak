package com.lbconsulting.homework252_lorenbak.database;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.lbconsulting.homework252_lorenbak.HW252Utilities;
import com.lbconsulting.homework252_lorenbak.MyLog;
import com.lbconsulting.homework252_lorenbak.database.content_provider.HW252ContentProvider;

public class TasksTable {

	// TasksTable data table
	public static final String TABLE_TASKS = "tblTasks";
	public static final String COL_ID = "_id";
	public static final String COL_TASK_NAME = "taskName";
	public static final String COL_TASK_DETAIL = "taskDetail";
	public static final String COL_DATE_CREATED = "dateCreated";

	public static final String[] PROJECTION_ALL = { COL_ID, COL_TASK_NAME, COL_TASK_DETAIL, COL_DATE_CREATED };

	public static final String CONTENT_PATH = TABLE_TASKS;
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_TASKS;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.lbconsulting."
			+ TABLE_TASKS;
	public static final Uri CONTENT_URI = Uri.parse("content://" + HW252ContentProvider.AUTHORITY + "/" + CONTENT_PATH);

	public static final String SORT_ORDER_TASK_NAME = COL_TASK_NAME + " ASC";
	public static final String SORT_ORDER_LAST_MODIFIED = COL_DATE_CREATED + " DESC, " + SORT_ORDER_TASK_NAME;

	// Database creation SQL statements
	private static final String CREATE_DATA_TABLE =
			"create table " + TABLE_TASKS
					+ " ("
					+ COL_ID + " integer primary key autoincrement, "
					+ COL_TASK_NAME + " text collate nocase, "
					+ COL_TASK_DETAIL + " text, "
					+ COL_DATE_CREATED + " integer"
					+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_DATA_TABLE);

		String insertProjection = "insert into "
				+ TABLE_TASKS
				+ " ("
				+ COL_ID + ", "
				+ COL_TASK_NAME + ", "
				+ COL_TASK_DETAIL + ", "
				+ COL_DATE_CREATED
				+ ") VALUES ";

		Calendar rightNow = Calendar.getInstance();
		long currentDateTimeInMillis = rightNow.getTimeInMillis();

		ArrayList<String> sqlStatements = new ArrayList<String>();

		// Task list data
		String item;
		int minValue = 1000; // 1 second
		int maxValue = 15000; // 15 seconds
		int randomMillSeconds;
		// create 20 tasks for our list
		for (int i = 0; i < 20; i++) {
			// pad task number so alphabetical sorting works properly 
			item = "Task " + String.format("%02d", i + 1);
			// add between 1 and 15 seconds to the current time
			// to provide different date created times
			randomMillSeconds = minValue + (int) (Math.random() * ((maxValue - minValue) + 1));
			sqlStatements.add(insertProjection + "(NULL, '" + item + "', 'Detail for " + item + "',"
					+ (currentDateTimeInMillis + randomMillSeconds) + ")");
		}

		HW252Utilities.execMultipleSQL(database, sqlStatements);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		MyLog.w("TasksTable", "Upgrading " + TABLE_TASKS + " from version " + oldVersion + " to version " + newVersion
				+ ".");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		onCreate(database);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Create Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static long CreateNewTask(Context context, String taskName, String taskDetail) {
		long newItemID = -1;
		if (taskName == null) {
			MyLog.e("TasksTable", "Error in CreateNewTask; taskName is Null!");
			return newItemID;
		}
		taskName = taskName.trim();
		if (taskDetail == null) {
			taskDetail = "";
		}
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;

			Calendar rightNow = Calendar.getInstance();
			long currentDateTimeInMillis = rightNow.getTimeInMillis();

			ContentValues values = new ContentValues();
			values.put(COL_TASK_NAME, taskName);
			values.put(COL_TASK_DETAIL, taskDetail);
			values.put(COL_DATE_CREATED, currentDateTimeInMillis);
			Uri newItemUri = cr.insert(uri, values);
			if (newItemUri != null) {
				newItemID = Long.parseLong(newItemUri.getLastPathSegment());
			}
		} catch (Exception e) {
			MyLog.e("Exception error in CreateNewItem. ", e.toString());
		}

		return newItemID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Read Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static CursorLoader getAllTaskItems(Context context, String sortOrder) {
		Uri uri = CONTENT_URI;
		String[] projection = PROJECTION_ALL;
		String selection = null;
		String selectionArgs[] = null;

		ContentResolver cr = context.getContentResolver();
		CursorLoader cursorLoader = null;
		try {
			cursorLoader = new CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			MyLog.e("Exception error  in ItemsTable: getAllTaskItems. ", e.toString());
		}
		return cursorLoader;
	}

	public static Cursor getTaskItem(Context context, long taskID) {
		Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(taskID));
		String[] projection = PROJECTION_ALL;
		String selection = null;
		String selectionArgs[] = null;
		String sortOrder = null;

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception e) {
			MyLog.e("Exception error  in ItemsTable: getTaskItem. ", e.toString());
		}
		return cursor;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Update Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int setTaskName(Context context, long taskID, String taskName) {
		int numberOfUpdatedRecords = 0;
		if (taskName == null) {
			MyLog.e("TasksTable", "Error in setTaskName; taskName is Null!");
			return numberOfUpdatedRecords;
		}
		taskName = taskName.trim();
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_ID + " = ?";
			String[] whereArgs = { String.valueOf(taskID) };

			Calendar rightNow = Calendar.getInstance();
			long currentDateTimeInMillis = rightNow.getTimeInMillis();

			ContentValues values = new ContentValues();
			values.put(COL_TASK_NAME, taskName);
			values.put(COL_DATE_CREATED, currentDateTimeInMillis);

			numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
		} catch (Exception e) {
			MyLog.e("Exception error in setTaskName. ", e.toString());
		}
		return numberOfUpdatedRecords;
	}

	public static int setTaskDetail(Context context, long taskID, String taskDetail) {
		int numberOfUpdatedRecords = 0;
		if (taskDetail == null) {
			MyLog.e("TasksTable", "Error in setTaskDetail; taskDetail is Null!");
			return numberOfUpdatedRecords;
		}
		taskDetail = taskDetail.trim();
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = CONTENT_URI;
			String where = COL_ID + " = ?";
			String[] whereArgs = { String.valueOf(taskID) };

			Calendar rightNow = Calendar.getInstance();
			long currentDateTimeInMillis = rightNow.getTimeInMillis();

			ContentValues values = new ContentValues();
			values.put(COL_TASK_DETAIL, taskDetail);
			values.put(COL_DATE_CREATED, currentDateTimeInMillis);

			numberOfUpdatedRecords = cr.update(uri, values, where, whereArgs);
		} catch (Exception e) {
			MyLog.e("Exception error in setTaskDetail. ", e.toString());
		}
		return numberOfUpdatedRecords;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Delete Methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void DeleteTask(Context context, long taskID) {
		if (taskID > 0) {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(taskID));
			String where = null;
			String[] selectionArgs = null;
			cr.delete(uri, where, selectionArgs);
		} else {
			MyLog.e("TasksTable", "Error in DeleteTask; taskID not greater than 0!");
		}
	}

	public static void DeleteALLTasks(Context context) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = CONTENT_URI;
		String where = null;
		String[] selectionArgs = null;
		cr.delete(uri, where, selectionArgs);
	}

}
