package com.lbconsulting.homework252_lorenbak.adapters;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.homework252_lorenbak.R;
import com.lbconsulting.homework252_lorenbak.database.TasksTable;

public class TasksListAdapter extends CursorAdapter {
	public Context context;
	public LayoutInflater inflater;

	public TasksListAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context c, Cursor cursor) {
		TextView tvTaskName = (TextView) view.findViewById(R.id.tvTaskName);
		if (tvTaskName != null) {
			tvTaskName.setText(cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_NAME)));
		}

		TextView tvTaskLastModified = (TextView) view.findViewById(R.id.tvTaskLastModified);
		if (tvTaskLastModified != null) {

			Calendar dateLastModified = Calendar.getInstance();
			long dateCreated = cursor.getLong(cursor.getColumnIndexOrThrow(TasksTable.COL_DATE_CREATED));
			dateLastModified.setTimeInMillis(dateCreated);

			SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy: h:mm:ss a", java.util.Locale.getDefault());
			String formatedDateLastModified = sdf.format(dateLastModified.getTime());

			tvTaskLastModified.setText(formatedDateLastModified);
		}
	}

	@Override
	public View newView(Context c, Cursor cursor, ViewGroup parent) {
		LayoutInflater i = LayoutInflater.from(c);
		View v = i.inflate(R.layout.tasks_list_row, parent, false);
		return v;
	}

}
