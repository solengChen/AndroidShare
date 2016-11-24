package com.scau.keshe.sharespace.myself;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.welcome.MainActivity;
import com.scau.keshe.sharespace.welcome.MainActivity.FragmentListener;
import com.scau.keshe.sharespace.welcome.view.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MyselfFragment extends Fragment implements OnClickListener,
		OnTouchListener, FragmentListener {

	private LinearLayout portratBut;
	private LinearLayout collectionBut;
	private LinearLayout shareBut;
	private LinearLayout settingBut;

	private Context context;

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_myself, null);

		portratBut = (LinearLayout) view
				.findViewById(R.id.myself_fragment_infobut);
		collectionBut = (LinearLayout) view
				.findViewById(R.id.myself_fragment_collectbut);
		shareBut = (LinearLayout) view
				.findViewById(R.id.myself_fragment_checksharebut);
		settingBut = (LinearLayout) view
				.findViewById(R.id.myself_fragment_settingbut);

		portratBut.setOnClickListener(this);
		portratBut.setOnTouchListener(this);
		collectionBut.setOnClickListener(this);
		collectionBut.setOnTouchListener(this);
		shareBut.setOnClickListener(this);
		shareBut.setOnTouchListener(this);
		settingBut.setOnClickListener(this);
		settingBut.setOnTouchListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		if (MainActivity.userId == -1) {

			MainActivity.mainIntent.setClass(context, LoginActivity.class);
			MainActivity.mainIntent.putExtra("jump-from", MainActivity.JUMP_FROM_MYSELF_PART);
			getActivity().startActivityForResult(MainActivity.mainIntent, MainActivity.LOGIN);
		} else {
			switch (v.getId()) {
			case R.id.myself_fragment_infobut:
				MainActivity.mainIntent.setClass(context, PersonalInfoActivity.class);
				break;

			case R.id.myself_fragment_collectbut:
				MainActivity.mainIntent.setClass(context, PersonalCollectionActivity.class);
				break;

			case R.id.myself_fragment_checksharebut:
				MainActivity.mainIntent.setClass(context, PersonalShareActivity.class);
				break;

			case R.id.myself_fragment_settingbut:
				MainActivity.mainIntent.setClass(context, PersonalSettingActivity.class);
				break;
			}
			startActivity(MainActivity.mainIntent);
		}	
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			v.setBackgroundResource(R.drawable.pressdown);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {

			switch (v.getId()) {
			case R.id.myself_fragment_infobut:
				v.setBackgroundResource(0);
				break;

			case R.id.myself_fragment_collectbut:
				v.setBackgroundResource(R.drawable.myself_fragment_collection);
				break;

			case R.id.myself_fragment_checksharebut:
				v.setBackgroundResource(R.drawable.myself_fragment_share);
				break;

			case R.id.myself_fragment_settingbut:
				v.setBackgroundResource(R.drawable.myself_fragment_setting);
				break;
			}

		}

		return false;
	}

	@Override
	public void mainRequestReflash() {
		// TODO Auto-generated method stub
		
	}

}
