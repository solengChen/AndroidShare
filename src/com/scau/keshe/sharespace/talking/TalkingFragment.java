package com.scau.keshe.sharespace.talking;

import java.util.ArrayList;
import java.util.List;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.R.layout;
import com.scau.keshe.sharespace.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class TalkingFragment extends Fragment {

	private Context context;
	
	private LayoutInflater inflater;
	
	private ListView listview;
	
	private TextView warmInfo;
	
	private TalkingAdapter talkAdapter;
	
	private boolean isInternetable = false;
	
	private List<Talker> talkers = new ArrayList<Talker>();
	
	public TalkingFragment() {
		inflater = LayoutInflater.from(context);
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	/**
	 * 更新列表数据
	 */
	public void flashListView() {
		talkAdapter.notifyDataSetChanged();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_talking, null);
		
		listview = (ListView) view.findViewById(R.id.talking_list);
		
		warmInfo = (TextView) view.findViewById(R.id.talking_part_friend_textinfo);
		
		talkAdapter = new TalkingAdapter(context, talkers);
		
		listview.setAdapter(talkAdapter);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 显示提示信息，网络未接入时显示
	 */
	public void showWarmInfo() {
		if(isInternetable) {
			warmInfo.setVisibility(View.VISIBLE);
		}
		else {
			warmInfo.setVisibility(View.GONE);
		}
	}
}
