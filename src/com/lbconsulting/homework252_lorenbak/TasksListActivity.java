package com.lbconsulting.homework252_lorenbak;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.homework252_lorenbak.R;

public class TasksListActivity extends Activity
		implements TasksListFragment.OnTaskSelectedListener {

	private TasksListFragment mTasksListFragment;
	private TaskDetailsFragment mTaskDetailsFragment;
	private Boolean mTwoFragmentLayout = false;

	private long NO_SELECTED_TASK_ID = 0;
	private long mSelectedTaskID = NO_SELECTED_TASK_ID;

	private int LIST_ACTIVITY = 1;
	private int DETAILS_ACTIVITY = 2;
	private int mLastPortraitActivity = LIST_ACTIVITY;
	private boolean mReturningFromLandscapeActivity = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.i("TasksListACTIVITY", "onCreate");
		setContentView(R.layout.activity_task_list);

		// Add TasksListFragment
		mTasksListFragment = (TasksListFragment) this.getFragmentManager().findFragmentByTag("TasksListFragment");
		if (mTasksListFragment == null) {
			// create the fragment
			mTasksListFragment = new TasksListFragment();
			MyLog.i("TasksListACTIVITY", "onCreate. New TasksListFragment created.");
			// add the fragment to the Activity
			this.getFragmentManager().beginTransaction()
					.add(R.id.fragmentTasksListPlaceholder, mTasksListFragment, "TasksListFragment")
					.commit();
			MyLog.i("TasksListACTIVITY", "onCreate. TasksListFragment add.");
		}
		mTwoFragmentLayout = isTwoFragmentLayout();

		if (mTwoFragmentLayout) {

			mTaskDetailsFragment = (TaskDetailsFragment) this.getFragmentManager().findFragmentByTag(
					"TaskDetailsFragment");
			if (mTaskDetailsFragment == null) {
				// create the fragment
				mTaskDetailsFragment = new TaskDetailsFragment();
				MyLog.i("TasksListACTIVITY", "onCreate. New TaskDetailsFragment created.");
				// add the fragment to the Activity
				this.getFragmentManager().beginTransaction()
						.add(R.id.fragmentTasksDetailsPlaceholder, mTaskDetailsFragment, "TaskDetailsFragment")
						.commit();
				MyLog.i("TasksListACTIVITY", "onCreate. TaskDetailsFragment add.");
			}
		} // NOTE: handle starting TaskDetailsActivity at onResume
	}

	private void startDetailsActivity() {
		MyLog.i("TasksListACTIVITY", "startDetailsActivity");

		Intent taskDetailsIntent = new Intent(this, TaskDetailsActivity.class);
		taskDetailsIntent.putExtra("SelectedTaskID", mSelectedTaskID);
		startActivity(taskDetailsIntent);
	}

	private Boolean isTwoFragmentLayout() {
		View fragmentTasksDetailsPlaceholder = this.findViewById(R.id.fragmentTasksDetailsPlaceholder);
		return fragmentTasksDetailsPlaceholder != null
				&& fragmentTasksDetailsPlaceholder.getVisibility() == View.VISIBLE;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		MyLog.i("TasksListACTIVITY", "onPostCreate");
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onTaskSelected(long Id) {
		MyLog.i("TasksListACTIVITY", "onTaskSelected: Id = " + Id);
		mSelectedTaskID = Id;
		if (mTwoFragmentLayout) {
			if (mTaskDetailsFragment != null) {
				mTaskDetailsFragment.ShowDetails(Id);
			}
		} else {
			startDetailsActivity();
		}

	}

	@Override
	protected void onStart() {
		MyLog.i("TasksListACTIVITY", "onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		MyLog.i("TasksListACTIVITY", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyLog.i("TasksListACTIVITY", "onResume");
		super.onResume();
		// Get the between instance stored values
		SharedPreferences applicationStates = getSharedPreferences("HW252", MODE_PRIVATE);
		mSelectedTaskID = applicationStates.getLong("SelectedTaskID", NO_SELECTED_TASK_ID);
		mLastPortraitActivity = applicationStates.getInt("LastPortraitActivty", LIST_ACTIVITY);
		mReturningFromLandscapeActivity = applicationStates.getBoolean("ReturningFromLandscapeActivity", false);

		if (mTwoFragmentLayout) {
			if (mSelectedTaskID > NO_SELECTED_TASK_ID) {
				if (mTaskDetailsFragment != null) {
					mTaskDetailsFragment.ShowDetails(mSelectedTaskID);
				}
			}

		} else {
			// single "List Fragment" layout
			if (mLastPortraitActivity == DETAILS_ACTIVITY && mReturningFromLandscapeActivity) {
				startDetailsActivity();
			}
		}
	}

	@Override
	protected void onPostResume() {
		MyLog.i("TasksListACTIVITY", "onPostResume");
		super.onPostResume();
	}

	@Override
	protected void onPause() {
		MyLog.i("TasksListACTIVITY", "onPause");
		super.onPause();
		SharedPreferences preferences = getSharedPreferences("HW252", MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putLong("SelectedTaskID", mSelectedTaskID);
		if (mTwoFragmentLayout) {
			applicationStates.putBoolean("ReturningFromLandscapeActivity", true);
		} else {
			applicationStates.putInt("LastPortraitActivty", LIST_ACTIVITY);
			applicationStates.putBoolean("ReturningFromLandscapeActivity", false);
		}
		// Commit to storage
		applicationStates.commit();
	}

	@Override
	protected void onStop() {
		MyLog.i("TasksListACTIVITY", "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		MyLog.i("TasksListACTIVITY", "onDestroy");
		super.onDestroy();
	}

}
