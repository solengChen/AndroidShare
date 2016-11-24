package com.scau.keshe.sharespace.connector;

import java.util.ArrayList;
import java.util.List;

import com.scau.keshe.sharespace.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {

	private List<Contacts> data = new ArrayList<Contacts>();

	private Context context;

	private LayoutInflater inflater;

	public ContactAdapter() {
		// TODO Auto-generated constructor stub
	}

	public ContactAdapter(Context context, List<Contacts> data) {
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (data != null) {
			return data.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (data != null) {
			return data.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Contacts friendInfo = null;

		if (data != null && data.size() != 0 && position < data.size()) {
			friendInfo = data.get(position);
		} else {
			Log.e("ConnectAdapter Error---------->",
					"data is null or data size is smaller than position !");

			return null;
		}

		ViewHolder viewholder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.connecter_part, null);

			viewholder = new ViewHolder();

			viewholder.vhimageView = (ImageView) convertView
					.findViewById(R.id.connect_part_portrat);
			viewholder.vhtextViwe = (TextView) convertView
					.findViewById(R.id.connect_part_name);

			convertView.setTag(viewholder);
		}

		else {
			viewholder = (ViewHolder) convertView.getTag();
		}

		if (friendInfo != null) {
			viewholder.vhimageView.setImageBitmap(BitmapFactory
					.decodeFile(friendInfo.getPortrat()));

			viewholder.vhtextViwe.setText(friendInfo.getName());
		} else {
			Log.e("ConnectAdapter Error----------->", "friendInfo is null !");
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView vhtextViwe;
		private ImageView vhimageView;
	}
}
