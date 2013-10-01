package com.willhaley.android.mobiledoppler.controller;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
	}

	@Override
	public void onStart() {
		super.onStart();
		Application.getGaTracker().send(MapBuilder.createAppView().build());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

}
