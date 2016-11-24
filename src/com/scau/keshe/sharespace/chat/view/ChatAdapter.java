package com.scau.keshe.sharespace.chat.view;

import java.util.List;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.chat.model.ChatMsgEntity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter{

	private Context mcontext;
	private List<ChatMsgEntity> data;
	private LayoutInflater inflater;
	
	public ChatAdapter() {
		// TODO Auto-generated constructor stub
	}

	public ChatAdapter(Context context, List<ChatMsgEntity> data) {
		this.mcontext = context;
		this.data = data;
		inflater = LayoutInflater.from(mcontext);
	}
	
	@Override
	public int getCount() {
		if(data == null) {
			return 0;
		}
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ChatMsgEntity entity = null;
		
		if(data != null && data.size() != 0 && position < data.size()) {
			entity = data.get(position);
			if(entity == null) {
				return null;
			}
		}
		else {
			Log.i("ChatAdapter Error------------>", "data or entity is null !");
			return null;
		}
		
		
		boolean isComMsg = entity.isComMsg();
		
		
		
		ViewHolder vh = null;
		
		if(convertView == null) {
			if(isComMsg) {
				convertView = inflater.inflate(R.layout.friends_dialog, null);
			}
			else {
				convertView = inflater.inflate(R.layout.myself_dialog, null);
			}
			
			vh = new ViewHolder();
			vh.isComMsg = isComMsg;
			vh.tvContent = (TextView) convertView.findViewById(R.id.content_text);
			vh.tvPortrat = (ImageView) convertView.findViewById(R.id.user_image);
			
			
//			vh.portrat.setImageBitmap(bm);
			
			convertView.setTag(vh);
		}
		else {
			vh = (ViewHolder) convertView.getTag();
			if(vh.isComMsg != isComMsg) {
				
				
				
				if(isComMsg) {
					convertView = inflater.inflate(R.layout.friends_dialog, null);
				}
				else {
					convertView = inflater.inflate(R.layout.myself_dialog, null);
				}
				
				vh = new ViewHolder();
				vh.isComMsg = isComMsg;
				vh.tvContent = (TextView) convertView.findViewById(R.id.content_text);
				vh.tvPortrat = (ImageView) convertView.findViewById(R.id.user_image);
				
				
//				vh.portrat.setImageBitmap(bm);
				
				convertView.setTag(vh);
				
				
			}
		}
		
		if(entity != null) {
			vh.tvContent.setText(entity.getText());
			//TODO
			vh.tvPortrat.setBackgroundResource(R.drawable.portrat);
		}
		
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView tvSendTime;
		TextView tvUserName;
		TextView tvContent;
		ImageView tvPortrat;
		boolean isComMsg = true;
	}
}
