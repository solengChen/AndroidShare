package com.scau.keshe.sharespace.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> list = new ArrayList<Fragment>();
	
	public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.list = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public int getCount() {
		if(list == null) {
			return 0;
		}
		return list.size();
	}

}