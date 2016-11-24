package com.scau.keshe.sharespace.firstpage.model;

import java.util.List;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.bean.PictureBean;
import com.scau.keshe.sharespace.dealImage.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageGridAdapter extends BaseAdapter {

	private List<String> datas; // 本地数据
	private List<PictureBean> netDatas; // 网络数据
	private String dirPath;
	private Context context;

	private int screenWidth; // 屏幕宽度
	private int col = 2;

	/**
	 * 加载本地图片时调用该方法
	 * 
	 * @param context
	 * @param datas
	 * @param dirPath
	 */
	public ImageGridAdapter(Context context, List<String> datas, String dirPath) {
		this.datas = datas;
		this.dirPath = dirPath;
		initAdapter(context);
	}

	/**
	 * 直接获取网络图片并加载此方法
	 * 
	 * @param context
	 * @param datas
	 */
	public ImageGridAdapter(Context context, List<PictureBean> datas) {
		this.netDatas = datas;
		initAdapter(context);
	}

	/**
	 * 设置本地数据内容，网络数据将置空
	 * 
	 * @param datas
	 * @param path
	 */
	public void setLocalData(List<String> datas, String path) {
		this.netDatas = null;
		this.datas = datas;
		this.dirPath = path;
	}

	/**
	 * 设置网络数据内容，本地数据将置空
	 * 
	 * @param netDatas
	 */
	public void setNetData(List<PictureBean> netDatas) {
		this.datas = null;
		this.dirPath = null;
		this.netDatas = netDatas;

		if (((this.netDatas.size() + 1) % 2) == 0) {
			col = 2;
		} else if ((this.netDatas.size() + 1) % 3 == 0) {
			col = 3;
		}
	}

	private void initAdapter(Context context) {
		this.context = context;

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;

	}

	@Override
	public int getCount() {
		if (datas != null && dirPath != null && dirPath.length() != 0) {
			return datas.size();
		} else if (netDatas != null) {
			return netDatas.size();
		}
		return -1;
	}

	@Override
	public Object getItem(int position) {
		if (datas != null && dirPath != null && dirPath.length() != 0) {
			return datas.get(position);
		} else if (netDatas != null) {
			return netDatas.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if ((datas == null || datas.size() == 0 || dirPath == null)
				&& (netDatas == null || netDatas.size() == 0
						|| netDatas.get(position) == null
						|| netDatas.get(position).getBytes() == null || netDatas
						.get(position).getId() == -1)) {
			ImageView img = new ImageView(context);
			img.setImageResource(R.drawable.ic_launcher);
			return img;
		}

		final ViewHolder holder = new ViewHolder();
		if (datas != null && dirPath != null && dirPath.length() != 0) {
			holder.picPath = dirPath + "/" + datas.get(position);
		} else  {
			holder.imageData = netDatas.get(position).getBytes();
			holder.imageId = netDatas.get(position).getId();
		}
		holder.imageView = new ImageView(context);
		holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

		int width = (screenWidth * 3 / 4) / col;	//左侧头像占1/4 屏宽
		int height = width; // 正方形显示方式,可能会有变形问题，保证宽度就行
		holder.imageView.setLayoutParams(new AbsListView.LayoutParams(width,
				height));

		holder.imageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					holder.imageView.setColorFilter(Color
							.parseColor("#77000000"));
					break;

				case MotionEvent.ACTION_UP:
					holder.imageView.setColorFilter(null);
					break;
				}

				return false;
			}
		});

		ImageLoader.getInstance().loadImage(true, holder.picPath,
				holder.imageData, holder.imageId, holder.imageView);

		return holder.imageView;
	}

	private class ViewHolder {
		String picPath;
		byte[] imageData;
		int imageId;
		ImageView imageView;
	}
}
