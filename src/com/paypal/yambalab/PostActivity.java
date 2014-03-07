package com.paypal.yambalab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class PostActivity extends Activity implements OnClickListener, TextWatcher {

	public static final String TAG = PostActivity.class.getSimpleName();

	private EditText mPostEditText;
	private PostTask mPostTask;
	private TextView mPostCounterText;
	private Button mPostButton;
	
	/**
	 * {@link AsyncTask} for posting to http://yamba.marakana.com/
	 */
	private static class PostTask extends AsyncTask<String, Void, Integer> {
		private ProgressDialog mProgressDialog;
		private Context mContext;
		
		public PostTask(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setTitle(R.string.post_progress_title);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.show();
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			YambaClient yc = new YambaClient("student", "password");
			try {
				yc.postStatus(params[0]);
				return R.string.post_success;
			} catch (YambaClientException e) {
				Log.d(TAG, "YambaClient Error: " + e);
				return R.string.post_failure;
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			Toast.makeText(mContext, "Posted!", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		
		// setup button listener
		findViewById(R.id.post_botton).setOnClickListener(this);
		
		// setup text view
		mPostEditText = (EditText) findViewById(R.id.post_edit_text);
		mPostEditText.addTextChangedListener(this);
		
		// setup counter text
		mPostCounterText = (TextView) findViewById(R.id.post_default_counter);
		
		// setup post button
		mPostButton = (Button) findViewById(R.id.post_botton);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		// cancel before destory
		if (mPostTask != null) {
			mPostTask.cancel(true);
		}
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.post_botton:
				mPostTask = new PostTask(this);
				mPostTask.execute(mPostEditText.getText().toString());
				break;
			default:
				break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		int len = mPostEditText.getText().length();
		int size = 140 - len;

		if (len <= 0) {
			mPostCounterText.setTextColor(0xffff0000);
		} else if (len < 10) {
			mPostCounterText.setTextColor(0xffffff00);
		} else {
			mPostCounterText.setTextColor(0xff00ff00);
		}
		
		mPostCounterText.setText(String.valueOf(size));
		mPostButton.setEnabled(len > 0);
	}

}
