package com.lbconsulting.homework252_lorenbak.database.content_provider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.lbconsulting.homework252_lorenbak.MyLog;
import com.lbconsulting.homework252_lorenbak.database.HW252DatabaseHelper;
import com.lbconsulting.homework252_lorenbak.database.TasksTable;

public class HW252ContentProvider extends ContentProvider {

	// AList database
	private HW252DatabaseHelper database = null;

	// UriMatcher switch constants
	private static final int TASK_LIST_MULTI_ROWS = 10;
	private static final int TASK_LIST_SINGLE_ROW = 11;

	public static final String AUTHORITY = "com.lbconsulting.HW252.contentprovider";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, TasksTable.CONTENT_PATH, TASK_LIST_MULTI_ROWS);
		sURIMatcher.addURI(AUTHORITY, TasksTable.CONTENT_PATH + "/#", TASK_LIST_SINGLE_ROW);
	}

	@Override
	public boolean onCreate() {
		MyLog.i("HW252ContentProvider", "onCreate");
		// Construct the underlying database
		// Defer opening the database until you need to perform
		// a query or other transaction.
		database = new HW252DatabaseHelper(getContext());
		return true;
	}

	/*A content provider is created when its hosting process is created, 
	 * and remains around for as long as the process does, so there is 
	 * no need to close the database -- it will get closed as part of the 
	 * kernel cleaning up the process's resources when the process is killed.
	 */

	@SuppressWarnings("resource")
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String rowID = null;
		int deleteCount = 0;

		// Open a WritableDatabase database to support the delete transaction
		SQLiteDatabase db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TASK_LIST_MULTI_ROWS:
			// To return the number of deleted items you must specify a where clause.
			// To delete all rows and return a value pass in "1".
			if (selection == null) {
				selection = "1";
			}
			// Perform the deletion
			deleteCount = db.delete(TasksTable.TABLE_TASKS, selection, selectionArgs);
			break;

		case TASK_LIST_SINGLE_ROW:
			// Limit deletion to a single row
			rowID = uri.getLastPathSegment();
			selection = TasksTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the deletion
			deleteCount = db.delete(TasksTable.TABLE_TASKS, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Method delete: Unknown URI: " + uri);
		}

		// Notify and observers of the change in the database.
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TASK_LIST_MULTI_ROWS:
			return TasksTable.CONTENT_TYPE;
		case TASK_LIST_SINGLE_ROW:
			return TasksTable.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Method getType. Unknown URI: " + uri);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public Uri insert(Uri uri, ContentValues values) {

		SQLiteDatabase db = null;
		long newRowId = 0;
		String nullColumnHack = null;

		// Open a WritableDatabase database to support the insert transaction
		db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TASK_LIST_MULTI_ROWS:
			newRowId = db.insertOrThrow(TasksTable.TABLE_TASKS, nullColumnHack, values);
			if (newRowId > 0) {
				// Construct and return the URI of the newly inserted row.
				Uri newRowUri = ContentUris.withAppendedId(TasksTable.CONTENT_URI, newRowId);
				// Notify and observers of the change in the database.
				getContext().getContentResolver().notifyChange(TasksTable.CONTENT_URI, null);
				return newRowUri;
			} else {
				return null;
			}

		case TASK_LIST_SINGLE_ROW:
			throw new IllegalArgumentException(
					"Method insert: Cannon insert a new row with a single row URI. Illegal URI: " + uri);

		default:
			throw new IllegalArgumentException("Method insert: Unknown URI: " + uri);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		// Using SQLiteQueryBuilder
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TASK_LIST_MULTI_ROWS:
			queryBuilder.setTables(TasksTable.TABLE_TASKS);
			checkTasksTableColumnNames(projection);
			break;

		case TASK_LIST_SINGLE_ROW:
			queryBuilder.setTables(TasksTable.TABLE_TASKS);
			checkTasksTableColumnNames(projection);
			queryBuilder.appendWhere(TasksTable.COL_ID + "=" + uri.getLastPathSegment());
			break;

		default:
			throw new IllegalArgumentException("Method query. Unknown URI: " + uri);
		}

		// Execute the query on the database
		SQLiteDatabase db = null;
		try {
			db = database.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = database.getReadableDatabase();
		}

		if (null != db) {
			String groupBy = null;
			String having = null;
			Cursor cursor = null;
			try {
				cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
			} catch (Exception e) {
				MyLog.e("Exception error in HW252ContentProvider:query. ", e.toString());
			}

			if (null != cursor) {
				cursor.moveToFirst();
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
			}
			return cursor;
		} else {
			return null;
		}
	}

	@SuppressWarnings("resource")
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		String rowID = null;
		int updateCount = 0;

		// Open a WritableDatabase database to support the update transaction
		SQLiteDatabase db = database.getWritableDatabase();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TASK_LIST_MULTI_ROWS:
			// Perform the update
			updateCount = db.update(TasksTable.TABLE_TASKS, values, selection, selectionArgs);
			break;

		case TASK_LIST_SINGLE_ROW:
			// Limit update to a single row
			rowID = uri.getLastPathSegment();
			selection = TasksTable.COL_ID + "=" + rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
			// Perform the update
			updateCount = db.update(TasksTable.TABLE_TASKS, values, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Method update: Unknown URI: " + uri);
		}

		// Notify and observers of the change in the database.
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}

	private void checkTasksTableColumnNames(String[] projection) {
		// Check if the caller has requested a column that does not exist
		if (projection != null) {
			HashSet<String> requstedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(TasksTable.PROJECTION_ALL));

			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requstedColumns)) {
				throw new IllegalArgumentException(
						"Method checkTasksTableColumnNames: Unknown MasterListTasksTable column name!");
			}
		}
	}

	/**
	 * A test package can call this to get a handle to the database underlying
	 * HW252ContentProvider, so it can insert test data into the database. The
	 * test case class is responsible for instantiating the provider in a test
	 * context; {@link android.test.ProviderTestCase2} does this during the call
	 * to setUp()
	 * 
	 * @return a handle to the database helper object for the provider's data.
	 */
	public HW252DatabaseHelper getOpenHelperForTest() {
		return database;
	}
}
