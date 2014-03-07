package com.paypal.yambalab;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	private static final int ALARM_RC = 1001;

	@Override
	public void onReceive(Context context, Intent intent) {
		create(context);
	}
	
	private void create(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.setInexactRepeating(AlarmManager.RTC, 1000, 5000, getIntent(context));
	}
	
	private void remove(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(getIntent(context));
	}
	
	private PendingIntent getIntent(Context context) {
		Intent refreshIntent = new Intent(context, YambaIntentService.class);
		PendingIntent operation = PendingIntent.getService(context, ALARM_RC, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		return operation;
	}

}
