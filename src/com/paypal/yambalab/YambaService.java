package com.paypal.yambalab;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class YambaService extends Service {
	private static final String TAG = YambaService.class.getSimpleName();

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
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand!");
		return super.onStartCommand(intent, flags, startId);
	}
	
	
}
