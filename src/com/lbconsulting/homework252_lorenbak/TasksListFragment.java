package com.lbconsulting.homework252_lorenbak;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.homework252_lorenbak.R;
import com.lbconsulting.homework252_lorenbak.adapters.TasksListAdapter;
import com.lbconsulting.homework252_lorenbak.database.TasksTable;

public class TasksListFragment extends ListFragment
		implements LoaderManager.LoaderCallbacks<Cursor> {
	OnTaskSelectedListener mCallback;
	private static final int TASKS_LIST_LOADER_ID = 1;

	private LoaderManager mLoaderManager = null;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mTasksListCallbacks;
	private TasksListAdapter mTasksListAdapter;

	private final int SORT_ORDER_ALPHABETICAL = 0;
	private final int SORT_ORDER_LAST_MODIFIED = 1;

	private int mTaskListSortOrder = SORT_ORDER_ALPHABETICAL;
	private int mProposedTaskListSortOrder = mTaskListSortOrder;

	// Container Activity must implement this interface
	public interface OnTaskSelectedListener {
		public void onTaskSelected(long Id);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MyLog.i("TasksListFragment", "onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.task_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MyLog.i("TasksListFragment", "onOptionsItemSelected");
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_newTask:
			Toast.makeText(getActivity(), "action_newTask", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_sortTaskList:
			ShowSortListDialog();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void ShowSortListDialog() {
		// Sort order options.
		String[] sortOrderOptions = getResources().getStringArray(R.array.list_sort_options);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.sortListDialogTitle));
		builder.setSingleChoiceItems(sortOrderOptions, mTaskListSortOrder,
				new DialogInterface.OnClickListener() {
					// When you click the radio button
					@Override
					public void onClick(DialogInterface dialog, int sortOrder) {
						mProposedTaskListSortOrder = sortOrder;
					}
				});

		builder.setPositiveButton(getString(R.string.btn_apply_text),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						if (mTaskListSortOrder != mProposedTaskListSortOrder) {
							mTaskListSortOrder = mProposedTaskListSortOrder;
							mLoaderManager.restartLoader(TASKS_LIST_LOADER_ID, null, mTasksListCallbacks);
						}
					}
				});

		builder.show();
	}

	@Override
	public void onAttach(Activity activity) {
		MyLog.i("TasksListFragment", "onAttach");
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnTaskSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTaskSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.i("TasksListFragment", "onCreate");

		setHasOptionsMenu(true);

		// set up tasks list adapter and loader
		mLoaderManager = getLoaderManager();

		mTasksListAdapter = new TasksListAdapter(getActivity(), null, 0);
		setListAdapter(mTasksListAdapter);

		mTasksListCallbacks = this;
		// Note: using null for the cursor. The masterList and listTitle cursors
		// loaded via onLoadFinished.
		mLoaderManager.initLoader(TASKS_LIST_LOADER_ID, null, mTasksListCallbacks);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//return super.onCreateView(inflater, container, savedInstanceState);
		MyLog.i("TasksListFragment", "onCreateView");
		View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);
		return view;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.i("TasksListFragment", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		MyLog.i("TasksListFragment", "onListItemClick - pos = " + position + " Id = " + id);
		mCallback.onTaskSelected(id);

		super.onListItemClick(listView, view, position, id);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		MyLog.i("TasksListFragment", "onCreateLoader - id = " + id);

		CursorLoader cursorLoader = null;

		switch (id) {
		case TASKS_LIST_LOADER_ID:
			switch (this.mTaskListSortOrder) {
			case SORT_ORDER_ALPHABETICAL:
				cursorLoader = TasksTable.getAllTaskItems(getActivity(), TasksTable.SORT_ORDER_TASK_NAME);
				break;

			case SORT_ORDER_LAST_MODIFIED:
				cursorLoader = TasksTable.getAllTaskItems(getActivity(), TasksTable.SORT_ORDER_LAST_MODIFIED);
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		MyLog.i("TasksListFragment", "onLoadFinished - loader id = " + loader.getId());

		// The asynchronous load is complete and the newCursor is now available for use. 
		// Update the masterListAdapter to show the changed data.
		switch (loader.getId()) {
		case TASKS_LIST_LOADER_ID:
			mTasksListAdapter.swapCursor(newCursor);
			break;
		default:
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		MyLog.i("TasksListFragment", "onLoaderReset - loader id = " + loader.getId());
		// For whatever reason, the Loader's data is now unavailable.
		// Remove any references to the old data by replacing it with a null Cursor.
		switch (loader.getId()) {
		case TASKS_LIST_LOADER_ID:
			mTasksListAdapter.swapCursor(null);
			break;
		default:
			break;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyLog.i("TasksListFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		MyLog.i("TasksListFragment", "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		MyLog.i("TasksListFragment", "onResume");
		super.onResume();
		// Get the between instance stored values
		SharedPreferences applicationStates = getActivity().getSharedPreferences("HW252", Context.MODE_PRIVATE);
		// Set application states
		mTaskListSortOrder = applicationStates.getInt("TaskListSortOrder", SORT_ORDER_ALPHABETICAL);
		mLoaderManager.restartLoader(TASKS_LIST_LOADER_ID, null, mTasksListCallbacks);
	}

	@Override
	public void onPause() {
		MyLog.i("TasksListFragment", "onPause");
		super.onPause();

		SharedPreferences preferences = getActivity().getSharedPreferences("HW252", Context.MODE_PRIVATE);
		SharedPreferences.Editor applicationStates = preferences.edit();
		applicationStates.putInt("TaskListSortOrder", mTaskListSortOrder);
		// Commit to storage
		applicationStates.commit();
	}

	@Override
	public void onStop() {
		MyLog.i("TasksListFragment", "onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		MyLog.i("TasksListFragment", "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		MyLog.i("TasksListFragment", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		MyLog.i("TasksListFragment", "onDetach");
		super.onDetach();
	}

}
