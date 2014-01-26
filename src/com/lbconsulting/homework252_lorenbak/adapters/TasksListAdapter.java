package com.lbconsulting.homework252_lorenbak.adapters;

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
		TextView tvTasksListRow = null;
		tvTasksListRow = (TextView) view.findViewById(R.id.tvTasksListRow);
		tvTasksListRow.setText(cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_NAME)));
		/*tvTasksListRow.setTypeface(null, Typeface.ITALIC);
		tvTasksListRow.setTextColor("#FF000000");*/

	}

	@Override
	public View newView(Context c, Cursor cursor, ViewGroup parent) {
		LayoutInflater i = LayoutInflater.from(c);
		View v = i.inflate(R.layout.tasks_list_row, parent, false);
		return v;
	}

}
