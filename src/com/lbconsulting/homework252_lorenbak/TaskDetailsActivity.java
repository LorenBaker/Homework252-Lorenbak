package com.lbconsulting.homework252_lorenbak;

import android.app.Activity;
import android.os.Bundle;

public class TaskDetailsActivity extends Activity {
	private int DETAILS_ACTIVITY = 2;
	private int mLastPortraitActivty = DETAILS_ACTIVITY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.i("TaskDetailsACTIVITY", "onCreate");
		super.onCreate(savedInstanceState);
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
	}

	@Override
	protected void onPause() {
		MyLog.i("TaskDetailsACTIVITY", "onPause");
		super.onPause();
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
