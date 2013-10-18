package com.willhaley.android.mobiledoppler.controller;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.willhaley.android.mobiledoppler.Application;
import com.willhaley.android.mobiledoppler.R;

public class AboutActivity extends Activity {

	private static final String SCREEN_NAME = "About Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		Application.getGaTracker().set(Fields.SCREEN_NAME, SCREEN_NAME);
		((TextView)findViewById(R.id.title)).setText("About");
	}

	@Override
	public void onStart() {
		super.onStart();
		Application.getGaTracker().send(MapBuilder.createAppView().build());
	}

}
