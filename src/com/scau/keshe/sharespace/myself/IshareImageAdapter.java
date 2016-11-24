package com.scau.keshe.sharespace.myself;

import java.util.List;
import java.util.Set;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.dealImage.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

public class IshareImageAdapter extends BaseAdapter {

	private List<String> images;
	private LayoutInflater inflater;
	private int screenWidth;

	public IshareImageAdapter(Context context, List<String> images) {
		this.images = images;
		this.inflater = LayoutInflater.from(context);

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
	}

	public void setData(List<String> data) {
		this.images = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (images == null) {
			return -1;
		}
		return images.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (images == null) {
			return null;
		}
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.iwould_share_item_gridview,
					null);
			holder = new ViewHolder();

			holder.image = (ImageView) convertView
					.findViewById(R.id.iwould_share_gridview_image);
			holder.but = (ImageButton) convertView
					.findViewById(R.id.iwould_share_gridview_but);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.image.setMaxWidth(screenWidth / 3);
		/**
		 * list为null时adapter是不响应的
		 */
		if (position < images.size() && images.get(position) != null
				&& images.get(position).length() != 0) {
			ImageLoader.getInstance().loadImage(true, images.get(position),
					null, -1, holder.image);
		} else {
			holder.image.setImageResource(R.drawable.before_pressdown);
		}

		holder.but.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					holder.image.setColorFilter(Color.parseColor("#77000000"));
					break;

				case MotionEvent.ACTION_UP:
					holder.image.setColorFilter(null);
					listener.openImageShowActy();
					break;
				}
				return false;
			}
		});

		return convertView;
	}

	private class ViewHolder {
		ImageView image;
		ImageButton but;
	}

	private openImageShowListener listener;

	public void setOpenImageShowListener(openImageShowListener listener) {
		this.listener = listener;
	}

	public interface openImageShowListener {
		void openImageShowActy();
	}
}
