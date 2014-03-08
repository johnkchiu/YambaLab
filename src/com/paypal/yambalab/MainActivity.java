package com.paypal.yambalab;

import com.paypal.yambalab.TimelineFragment.OnSelectListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements OnSelectListener {
	
	private TimelineFragment mTimelineFragment;
	private DetailFragment mDetailFragment;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get reference to two fragments
        mTimelineFragment = (TimelineFragment) getFragmentManager().findFragmentById(R.id.fragment_timeline);
        mDetailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.fragment_detail);

        // register listener
        if (mTimelineFragment != null) {
        	mTimelineFragment.setOnSelectListener(this);
        }
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
	public void onSelect(String user, String timestamp, String message) {
		if ((mDetailFragment != null) && (mDetailFragment.isVisible())) {
			mDetailFragment.setDetails(user, timestamp, message);
		}
	}
}