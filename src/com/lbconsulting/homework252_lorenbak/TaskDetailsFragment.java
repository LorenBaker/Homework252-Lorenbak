package com.lbconsulting.homework252_lorenbak;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.homework252_lorenbak.R;
import com.lbconsulting.homework252_lorenbak.database.TasksTable;

public class TaskDetailsFragment extends Fragment {

	private long NO_SELECTED_TASK_ID = 0;
	private long mSelectedTaskID = NO_SELECTED_TASK_ID;
	private String mTaskName = "";

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
			ShowEditTaskDialog();
			//Toast.makeText(getActivity(), "action_editTask", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_deleteTask:
			ShowDeleteTaskDialog();
			//Toast.makeText(getActivity(), "action_deleteTask", Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void ShowEditTaskDialog() {
		if (mSelectedTaskID > NO_SELECTED_TASK_ID) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getString(R.string.editTaskDialogTitle));

			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View editTaskView = inflater.inflate(R.layout.dialog_add_new_task, null);
			if (editTaskView != null) {
				final EditText txtTaskName = (EditText) editTaskView.findViewById(R.id.txtTaskName);
				final EditText txtTaskDetail = (EditText) editTaskView.findViewById(R.id.txtTaskDetails);

				Cursor cursor = TasksTable.getTaskItem(getActivity(), mSelectedTaskID);
				if (cursor != null) {
					String taskName = cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_NAME));
					String taskDetail = cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_DETAIL));
					txtTaskName.setText(taskName);
					txtTaskDetail.setText(taskDetail);
					cursor.close();
				}

				builder.setView(editTaskView);
				builder.setPositiveButton(getString(R.string.btn_apply_text),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int item) {
								TasksTable.setTaskName(getActivity(), mSelectedTaskID,
										txtTaskName.getText().toString().trim());

								TasksTable.setTaskDetail(getActivity(), mSelectedTaskID,
										txtTaskDetail.getText().toString().trim());
								ShowDetails(mSelectedTaskID);
							}
						});

				builder.setNegativeButton(getString(R.string.btn_cancel_text), null);

				builder.show();
			}
		}
	}

	private void ShowDeleteTaskDialog() {
		if (mSelectedTaskID > NO_SELECTED_TASK_ID) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			String lineSep = System.getProperty("line.separator");
			String deleteTaskTitle = getString(R.string.deleteTaskDialogTitle) + lineSep;
			//final String taskName;
			Cursor cursor = TasksTable.getTaskItem(getActivity(), mSelectedTaskID);
			if (cursor != null) {
				mTaskName = cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_NAME));
				deleteTaskTitle = deleteTaskTitle + mTaskName + " ?";
				cursor.close();
			}
			builder.setTitle(deleteTaskTitle);

			builder.setPositiveButton(getString(R.string.btn_yes_text),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							TasksTable.DeleteTask(getActivity(), mSelectedTaskID);
							mSelectedTaskID = NO_SELECTED_TASK_ID;
							ShowTaskDeletedMessage(mTaskName);
						}
					});

			builder.setNegativeButton(getString(R.string.btn_no_text), null);

			builder.show();
		}
	}

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
					tvTaskDetailTitle.setText("TASK: "
							+ cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_NAME)));
					tvTaskDetail.setText(cursor.getString(cursor.getColumnIndexOrThrow(TasksTable.COL_TASK_DETAIL)));
					cursor.close();
					mSelectedTaskID = taskID;
				}
			}
		}
	}

	private void ShowTaskDeletedMessage(String taskName) {
		TextView tvTaskDetailTitle = (TextView) getView().findViewById(R.id.tvTaskDetailTitle);
		TextView tvTaskDetail = (TextView) getView().findViewById(R.id.tvTaskDetail);
		if (tvTaskDetailTitle != null && tvTaskDetail != null) {
			tvTaskDetailTitle.setText("");
			tvTaskDetail.setText(taskName + " DELETED.");
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
