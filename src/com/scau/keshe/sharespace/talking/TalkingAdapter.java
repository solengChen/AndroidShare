package com.scau.keshe.sharespace.talking;

import java.util.List;

import com.scau.keshe.sharespace.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TalkingAdapter extends BaseAdapter {

	private Context context;
	
	private List<Talker> talkers;
	
	private LayoutInflater inflater;
	
	public TalkingAdapter(Context context, List<Talker> talkers) {
		this.context = context;
		this.talkers = talkers;
		
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(talkers != null) {
			return talkers.size();
		}
		
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(talkers != null && position < talkers.size()) {
			return talkers.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewholder = null;
		
		Talker talker = null;
		if(talkers != null && talkers.size() > position) {
			talker = talkers.get(position);
			if(talker == null) {
				return null;
			}
		}else {
			return null;
		}
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.talking_part, null);
			viewholder = new ViewHolder();
			viewholder.vhportrat = (ImageView) convertView.findViewById(R.id.talking_part_portrat);
			viewholder.vhname = (TextView) convertView.findViewById(R.id.talking_part_friend_name);
			viewholder.vhinfo = (TextView) convertView.findViewById(R.id.talking_part_friend_textinfo);
			viewholder.vhdate = (TextView) convertView.findViewById(R.id.talking_part_datetxt);
			
			convertView.setTag(viewholder);
		}
		else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		viewholder.vhportrat.setImageBitmap(talker.getPortrat());
		viewholder.vhname.setText(talker.getUserName());
		viewholder.vhinfo.setText(talker.getInfo());
		viewholder.vhdate.setText(talker.getDate());
		
		return convertView;
	}
	
	private class ViewHolder {
		ImageView vhportrat;
		TextView vhname,
				vhinfo,
				vhdate;
	}
}
