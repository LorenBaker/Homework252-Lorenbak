package com.lbconsulting.homework252_lorenbak;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.homework252_lorenbak.R;
import com.lbconsulting.homework252_lorenbak.database.TasksTable;

public class TaskDetailsFragment extends Fragment {

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
	}

	public long getShownTaskID() {
		long taskID = NO_SELECTED_TASK_ID;
		if (mSelectedTaskID == NO_SELECTED_TASK_ID) {
			Bundle args = getArguments();
			if (args != null) {
				taskID = args.getLong("SelectedTaskID", NO_SELECTED_TASK_ID);
				mSelectedTaskID = taskID;
			}
			if (taskID <= NO_SELECTED_TASK_ID) {
				MyLog.e("TaskDetailsFragment", "getShownTaskID; Invalid Id - Id = " + String.valueOf(taskID));
			}
		} else {
			taskID = mSelectedTaskID;
		}

		return taskID;
	}

	public void ShowDetails(long taskID) {
		if (taskID > NO_SELECTED_TASK_ID) {
			TextView tvTaskDetailTitle = (TextView) getView().findViewById(R.id.tvTaskDetailTitle);
			TextView tvTaskDetail = (TextView) getView().findViewById(R.id.tvTaskDetail);
			if (tvTaskDetailTitle != null && tvTaskDetail != null) {
				Cursor cursor = TasksTable.getTaskItem(getActivity(), taskID);
				if (cursor != null) {
					tvTaskDetailTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_NAME))
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
		return super.onCreateView(inflater, container, savedInstanceState);
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
