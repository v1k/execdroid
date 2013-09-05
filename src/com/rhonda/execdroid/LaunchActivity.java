package com.rhonda.execdroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent serviceIntent = new Intent(LaunchActivity.this, MainService.class);
		startService(serviceIntent);
		finish();
	}
	
}