package com.lbconsulting.homework252_lorenbak;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.homework252_lorenbak.R;

public class TaskDetailsActivity extends Activity {
	private int DETAILS_ACTIVITY = 2;
	private TaskDetailsFragment mTaskDetailsFragment;
	private long NO_SELECTED_TASK_ID = 0;
	private long mSelectedTaskID = NO_SELECTED_TASK_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("TaskDetailsACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}

		setContentView(R.layout.activity_task_details);

		// get the selected task id
		Intent intent = getIntent();
		mSelectedTaskID = intent.getLongExtra("SelectedTaskID", NO_SELECTED_TASK_ID);

		// create the task details fragment
		mTaskDetailsFragment = new TaskDetailsFragment();
		// add the fragment
		this.getFragmentManager().beginTransaction()
				.add(R.id.activityTasksDetailsPlaceholder, mTaskDetailsFragment, "TaskDetailsFragment")
				.commit();
		MyLog.i("TaskDetailsACTIVITY", "onCreate. TaskDetailsFragment add.");
	}

	@Override
	protected void onStart() {
		MyLog.i("TaskDetailsACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("TaskDetailsACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("TaskDetailsACTIVITY", "onResume");
		super.onResume();
		mTaskDetailsFragment.ShowDetails(mSelectedTaskID);
	}

	@Override
	protected void onPause() {
		MyLog.i("TaskDetailsACTIVITY", "onPause");
		super.onPause();
		SharedPreferences preferences = getSharedPreferences("HW252", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		// indicates that the last portrait activity was this details activity.
		applicationStates.putInt("LastPortraitActivty", DETAILS_ACTIVITY);
		applicationStates.commit();
	}

	@Override
	protected void onStop() {
		MyLog.i("TaskDetailsACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		MyLog.i("TaskDetailsACTIVITY", "onDestroy");
		super.onDestroy();
	}
}
