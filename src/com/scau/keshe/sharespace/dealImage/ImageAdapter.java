package com.scau.keshe.sharespace.dealImage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.scau.keshe.sharespace.R;

public class ImageAdapter extends BaseAdapter {
	/**
	 * 保存被选择的图片路径
	 */
	private static List<String> selectedImg = new ArrayList<String>();

	private List<String> datas;
	// private Context context;
	private String dirPath;
	private LayoutInflater inflater;

	private int screenWidth;

	private Context context;

	private Handler UIhandler;

	// 图片名与文件夹路径分开存，可减少存储空间的消耗
	public ImageAdapter(Context context, List<String> datas, String dirPath) {
		// this.context = context;
		this.datas = datas;
		this.dirPath = dirPath;

		this.context = context;
		this.inflater = LayoutInflater.from(context);

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;

		UIhandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0x007) {
					Bundle data = msg.getData();
					if (data != null) {
						String imgPath = data.getString("imgPath");
						ViewHolder holder = (ViewHolder) msg.obj;
						ImageLoader.getInstance().loadImage(true, imgPath, null,-1,
								holder.vhImage);
					}
				}
			};
		};

	}

	@Override
	public int getCount() {
		if (datas != null) {
			return datas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (datas != null) {
			return datas.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.item_gridview, parent,
					false);
			viewHolder = new ViewHolder();

			viewHolder.vhImage = (ImageView) convertView
					.findViewById(R.id.id_item_image);
			viewHolder.vhButton = (ImageButton) convertView
					.findViewById(R.id.id_item_select);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 图片状态重置
		viewHolder.vhImage.setImageResource(R.drawable.picture_no);
		viewHolder.vhButton.setImageResource(R.drawable.picture_unselected);
		viewHolder.vhImage.setColorFilter(null);

		viewHolder.vhImage.setMaxWidth(screenWidth / 3);

		final String imgPath = dirPath + "/" + datas.get(position);

		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putString("imgPath", imgPath);
		msg.obj = viewHolder;
		msg.setData(data);
		msg.what = 0x007;
		UIhandler.sendMessage(msg);
		// ImageLoader.getInstance().loadImage(true, imgPath, null, -1,
		// viewHolder.vhImage);

		viewHolder.vhImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 已经被选择
				if (selectedImg.contains(imgPath)) {
					selectedImg.remove(imgPath);
					viewHolder.vhImage.setColorFilter(null);
					viewHolder.vhButton
							.setImageResource(R.drawable.picture_unselected);
				}
				// 未被选择
				else {
					if (selectedImg.size() >= 6) {
						Toast.makeText(context, "最多只能分享六张图片!",
								Toast.LENGTH_SHORT).show();
					} else {
						selectedImg.add(imgPath);
						viewHolder.vhImage.setColorFilter(Color
								.parseColor("#77000000"));
						viewHolder.vhButton
								.setImageResource(R.drawable.picture_selected);
					}
				}
				// notify会出现闪屏
				// notifyDataSetChanged();
			}
		});

		if (selectedImg.contains(imgPath)) {
			viewHolder.vhImage.setColorFilter(Color.parseColor("#77000000"));
			viewHolder.vhButton.setImageResource(R.drawable.picture_selected);
		}

		return convertView;
	}

	/**
	 * 获取标记的图片的绝对路径
	 * 
	 * @return
	 */
	public static List<String> getSelectedImg() {
		return selectedImg;
	}

	private class ViewHolder {
		ImageView vhImage;
		ImageButton vhButton;
	}

}