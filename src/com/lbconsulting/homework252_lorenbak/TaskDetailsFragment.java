package com.lbconsulting.homework252_lorenbak;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homework252_lorenbak.R;
import com.lbconsulting.homework252_lorenbak.database.TasksTable;

public class TaskDetailsFragment extends Fragment {

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);
		MyLog.i("TaskDetailsFragment", "onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.task_details, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MyLog.i("TaskDetailsFragment", "onOptionsItemSelected");
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_editTask:
			Toast.makeText(getActivity(), "action_editTask", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_deleteTask:
			Toast.makeText(getActivity(), "action_deleteTask", Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private long NO_SELECTED_TASK_ID = 0;
	private long mSelectedTaskID = NO_SELECTED_TASK_ID;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("TaskDetailsFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("TaskDetailsFragment", "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.i("TaskDetailsFragment", "onCreate");
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	public long getShownTaskID() {
		return mSelectedTaskID;
	}

	public void ShowDetails(long taskID) {
		if (taskID > NO_SELECTED_TASK_ID) {
			TextView tvTaskDetailTitle = (TextView) getView().findViewById(R.id.tvTaskDetailTitle);
			TextView tvTaskDetail = (TextView) getView().findViewById(R.id.tvTaskDetail);
			if (tvTaskDetailTitle != null && tvTaskDetail != null) {
				Cursor cursor = TasksTable.getTaskItem(getActivity(), taskID);
				if (cursor != null) {
					tvTaskDetailTitle.setText("TITLE: "
							+ cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_NAME))
							+ " Detail");
					tvTaskDetail.setText(cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_DETAIL)));
					cursor.close();
					mSelectedTaskID = taskID;
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.i("TaskDetailsFragment", "onCreateView");

		View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
		return view;
	}

	@Override
	public void onDestroy() {
		MyLog.i("TaskDetailsFragment", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		MyLog.i("TaskDetailsFragment", "onDetach");
		super.onDetach();
	}

	@Override
	public void onPause() {
		MyLog.i("TaskDetailsFragment", "onPause");
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		MyLog.i("TaskDetailsFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		MyLog.i("TaskDetailsFragment", "onResume");
		super.onResume();
		long selectedTaskID = getShownTaskID();
		ShowDetails(selectedTaskID);
	}

	@Override
	public void onStart() {
		MyLog.i("TaskDetailsFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		MyLog.i("TaskDetailsFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.i("TaskDetailsFragment", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

}
