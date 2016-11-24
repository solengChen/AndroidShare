package com.scau.keshe.sharespace.myself;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.R.layout;
import com.scau.keshe.sharespace.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PersonalShareActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_share);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personal_share, menu);
		return true;
	}

}
