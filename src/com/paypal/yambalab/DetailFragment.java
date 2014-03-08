package com.paypal.yambalab;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {
	
	private TextView mUserTextView;
	private TextView mTimestampTextView;
	private TextView mMessageTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_details, container, false);
		
		// locate the text view
		mUserTextView = (TextView) view.findViewById(R.id.detail_user_text_view);
		mTimestampTextView = (TextView) view.findViewById(R.id.detail_timestamp_text_view);
		mMessageTextView = (TextView) view.findViewById(R.id.detail_message_text_view);

		return view;
	}

	public void setDetails(String user, String timestamp, String message) {
		mUserTextView.setText(user);
		mTimestampTextView.setText(timestamp);
		mMessageTextView.setText(message);
	}
	
}
