package com.lbconsulting.homework252_lorenbak;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
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
	private int mLastPortraitActivty = LIST_ACTIVITY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.i("TasksListACTIVITY", "onCreate");
		setContentView(R.layout.activity_task_list);

		// Add TitleListFragment
		mTasksListFragment = (TasksListFragment) this.getFragmentManager().findFragmentByTag("TasksListFragment");
		if (mTasksListFragment == null) {
			// create the fragment
			mTasksListFragment = new TasksListFragment();
			MyLog.i("TasksListACTIVITY", "onCreate. New TasksListFragment created.");
		}
		// replace the fragment
		this.getFragmentManager().beginTransaction()
				.replace(R.id.fragmentTasksListPlaceholder, mTasksListFragment, "TasksListFragment")
				.commit();
		MyLog.i("TasksListACTIVITY", "onCreate. TasksListFragment replace.");

		mTwoFragmentLayout = isTwoFragmentLayout();

		if (mTwoFragmentLayout) {
			if (mSelectedTaskID > NO_SELECTED_TASK_ID) {
				mTaskDetailsFragment = (TaskDetailsFragment) this.getFragmentManager().findFragmentByTag(
						"TaskDetailsFragment");
				if (mTaskDetailsFragment == null) {
					// create the fragment
					mTaskDetailsFragment = new TaskDetailsFragment();
					MyLog.i("TasksListACTIVITY", "onCreate. New TaskDetailsFragment created.");
				}
				// replace the fragment
				this.getFragmentManager().beginTransaction()
						.replace(R.id.fragmentTasksDetailsPlaceholder, mTaskDetailsFragment, "TaskDetailsFragment")
						.commit();
			}

		} else {
			if (mSelectedTaskID > NO_SELECTED_TASK_ID) {
				// start Details activity
				startDetailsActivity();
			}

		}
	}

	private void startDetailsActivity() {
		// TODO Auto-generated method stub

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.i("TasksListACTIVITY", "onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_list, menu);
		return true;
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
		mLastPortraitActivty = applicationStates.getInt("LastPortraitActivty", LIST_ACTIVITY);

		if (mTwoFragmentLayout) {
			if (mSelectedTaskID > NO_SELECTED_TASK_ID) {
				mTaskDetailsFragment.ShowDetails(mSelectedTaskID);
			}

		} else {
			// single "List Fragment" layout

			if (mLastPortraitActivty == DETAILS_ACTIVITY) {
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
		if (!mTwoFragmentLayout) {
			applicationStates.putInt("LastPortraitActivty", LIST_ACTIVITY);
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
