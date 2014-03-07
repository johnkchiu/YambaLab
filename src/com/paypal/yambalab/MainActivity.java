package com.paypal.yambalab;

import android.app.ListActivity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements OnClickListener, LoaderCallbacks<Cursor> {
	public static final String YAMBA_REFRESH = "com.paypal.yambalabs.REFRESH";

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int STATUS_LOADER = 100;
	private static final IntentFilter INTENT_YAMBA_REFRESH = new IntentFilter(YAMBA_REFRESH);
	
//    private Button mButtonStartService;
//    private Button mButtonStopService;
//    private Button mButtonStartIntentService;
//    private Button mButtonStopIntentService;
	
	
	private SimpleCursorAdapter mAdapter;
	private RefreshReceiver mRefreshReceiver;
	

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
			getLoaderManager().restartLoader(STATUS_LOADER, null, MainActivity.this);
			Toast.makeText(context, "Main refresh complete!",Toast.LENGTH_SHORT).show();
		}
		
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        
//        // bind buttons listeners
//        mButtonStartService = (Button) findViewById(R.id.button_start_service);
//        mButtonStartService.setOnClickListener(this);
//        
//        mButtonStopService = (Button) findViewById(R.id.button_stop_service);
//        mButtonStopService.setOnClickListener(this);
//        
//        mButtonStartIntentService = (Button) findViewById(R.id.button_start_intent_service);
//        mButtonStartIntentService.setOnClickListener(this);
//        
//        mButtonStopIntentService = (Button) findViewById(R.id.button_stop_intent_service);
//        mButtonStopIntentService.setOnClickListener(this);

        // create receiver
		mRefreshReceiver = new RefreshReceiver();
        
        // set list adapter
        mAdapter = new SimpleCursorAdapter(this, R.layout.status_list, null, PROJS, IDS, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mAdapter.setViewBinder(new TimestampViewBinder());
        getListView().setAdapter(mAdapter);
        getLoaderManager().initLoader(STATUS_LOADER, null, this);
    }

    @Override
	protected void onResume() {
		super.onResume();

		// setup listener on resume
		registerReceiver(mRefreshReceiver, INTENT_YAMBA_REFRESH);

		// restart the loader
		getLoaderManager().restartLoader(STATUS_LOADER, null, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		unregisterReceiver(mRefreshReceiver);
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_refresh:
			intent = new Intent(this, YambaIntentService.class);
			startService(intent);
			return true;
		case R.id.action_post:
			intent = new Intent(this, PostActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button_start_service:
			intent = new Intent("marakana.intent.action.IntentServiceDemo");
			startService(intent);
			break;
		case R.id.button_stop_service:
			intent = new Intent("marakana.intent.action.IntentServiceDemo");
			stopService(intent);
			break;
		case R.id.button_start_intent_service:
			intent = new Intent(this, YambaIntentService.class);
			startService(intent);
			break;
		case R.id.button_stop_intent_service:
			intent = new Intent(this, YambaIntentService.class);
			stopService(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cl = new CursorLoader(this, YambaContract.Timeline.URI, new String[] {
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

