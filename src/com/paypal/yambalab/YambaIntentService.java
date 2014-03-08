package com.paypal.yambalab;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class YambaIntentService extends IntentService {
	public static final String TAG = YambaIntentService.class.getSimpleName();

	private static final String[] MAX_PROJECTION = { YambaContract.Timeline.Column.MAX_TIMESTAMP };
	
	public YambaIntentService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG,  "onCreate!");
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy!");
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "onHandleIntent!");
		
		YambaClient yc = new YambaClient("student", "password");
		try {
			List<Status> statuses = yc.getTimeline(25);
			List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
			long maxTimestamp = getMaxTimestamp();
			
			for (Status status : statuses) {
				if (status.getCreatedAt().getTime() > maxTimestamp) {
					Log.d(TAG, "[" + status.getCreatedAt() + "] " + status.getUser() + ": " + status.getMessage() );
					ContentValues cv = new ContentValues();
					cv.put(YambaContract.Timeline.Column.USER, status.getUser());
					cv.put(YambaContract.Timeline.Column.STATUS, status.getMessage());
					cv.put(YambaContract.Timeline.Column.TIMESTAMP, status.getCreatedAt().getTime());
					contentValuesList.add(cv);
				}
			}
			
			getContentResolver().bulkInsert(YambaContract.Timeline.URI, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
			
			// broadcast done message
			sendBroadcast(new Intent(TimelineFragment.YAMBA_REFRESH));
			
		} catch (YambaClientException e) {
			Log.d(TAG, "YambaClientException: " + e);
		}
	}

	private long getMaxTimestamp() {
		long max = Long.MIN_VALUE;
		Cursor c = getContentResolver().query(YambaContract.Timeline.URI, MAX_PROJECTION, null, null, null);

		if (c != null) {
			try {
				if (c.moveToFirst()) {
					max = c.getLong(0);
				}
			} finally {
				c.close();
			}
			
		}
		return max;
	}
	
}
