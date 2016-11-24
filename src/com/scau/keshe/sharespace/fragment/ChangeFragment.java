package com.scau.keshe.sharespace.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment") public class ChangeFragment extends Fragment {

	private int id;
	
	public ChangeFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public ChangeFragment(int id) {
		this.id = id;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(id, container, false);
	}
}
