package com.paypal.yambalab;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.yambalab.YambaContract.Timeline;

public class TimelineFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	
	public interface OnSelectListener {
		public void onSelect(String user, String timestamp, String message);
	}
	
	public static final String YAMBA_REFRESH = "com.paypal.yambalabs.REFRESH";

	private static final int STATUS_LOADER = 100;
	private static final IntentFilter INTENT_YAMBA_REFRESH = new IntentFilter(YAMBA_REFRESH);
	
	private SimpleCursorAdapter mAdapter;
	private RefreshReceiver mRefreshReceiver;
	private OnSelectListener mOnSelectListener;

	private static final String[] PROJS = new String[] { 
		YambaContract.Timeline.Column.USER,
		YambaContract.Timeline.Column.STATUS,
		YambaContract.Timeline.Column.TIMESTAMP
	};

	private static final int[] IDS = { 
			R.id.main_list_user, 
			R.id.main_list_message,
			R.id.main_list_timestamp
	};
	
	private class TimestampViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() == R.id.main_list_timestamp) {
				TextView v = (TextView) view;
				v.setText( DateUtils.getRelativeTimeSpanString(cursor.getLong(columnIndex)) );
				return true;
			}

			return false;
		}
	}
	
	private class RefreshReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			getActivity().getLoaderManager().restartLoader(STATUS_LOADER, null, TimelineFragment.this);
			Toast.makeText(context, "Main refresh complete!",Toast.LENGTH_SHORT).show();
		}
		
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (mOnSelectListener != null) {
			Cursor c = (Cursor) l.getItemAtPosition(position);
			mOnSelectListener.onSelect(
					c.getString(c.getColumnIndex(Timeline.Column.USER)),
					DateUtils.getRelativeTimeSpanString(c.getLong(c.getColumnIndex(Timeline.Column.TIMESTAMP))).toString(),
					c.getString(c.getColumnIndex(Timeline.Column.STATUS)) );
		}
		super.onListItemClick(l, v, position, id);
	}


	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// create receiver
		mRefreshReceiver = new RefreshReceiver();

        // set list adapter
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.status_list, null, PROJS, IDS, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mAdapter.setViewBinder(new TimestampViewBinder());
        setListAdapter(mAdapter);
//        getListView().setAdapter(mAdapter);
//        setListShown(true);
		getLoaderManager().initLoader(STATUS_LOADER, null, this);
	}

    @Override
	public void onResume() {
		super.onResume();

		// setup listener on resume
		getActivity().registerReceiver(mRefreshReceiver, INTENT_YAMBA_REFRESH);

		// restart the loader
		getLoaderManager().restartLoader(STATUS_LOADER, null, this);
	}

	@Override
	public void onPause() {
		super.onPause();
		
		getActivity().unregisterReceiver(mRefreshReceiver);
	}
    
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cl = new CursorLoader(getActivity(), YambaContract.Timeline.URI, new String[] {
				YambaContract.Timeline.Column.ID,
				YambaContract.Timeline.Column.STATUS,
				YambaContract.Timeline.Column.TIMESTAMP,
				YambaContract.Timeline.Column.USER,
		}, null, null, YambaContract.Timeline.Column.TIMESTAMP + " desc");
		return cl;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		mAdapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
    
}

