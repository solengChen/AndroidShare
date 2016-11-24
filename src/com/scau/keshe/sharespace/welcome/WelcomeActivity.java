package com.scau.keshe.sharespace.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.scau.keshe.sharespace.R;



public class WelcomeActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		Handler handler = new Handler();
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, MainActivity.class);
				startActivityForResult(intent, 0x101);
			}
		}, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0x101) {
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
