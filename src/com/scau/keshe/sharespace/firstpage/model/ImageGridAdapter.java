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
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

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
		} else if (netDatas != null && netDatas.size() > position) {
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
				&& (netDatas == null || netDatas.size() == 0)) {
			// ImageView img = new ImageView(context);
			// img.setImageResource(R.drawable.ic_launcher);
			return convertView;
		}

		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.image_gird_view_pic_item, parent, false);

			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.image_grid_pictrue);
			if (datas != null && dirPath != null && dirPath.length() != 0) {
				holder.picPath = dirPath + "/" + datas.get(position);
			} else if (netDatas.get(position) != null) {
				holder.imageId = netDatas.get(position).getId();
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		//重置图片
		holder.imageId = -1;
		holder.imageView.setImageResource(R.drawable.picture_no);
		holder.picPath = null;
		
		// 加载本地图片数据
		if (datas != null && datas.size() != 0) {

		}

		// 网络数据加载图片(包括缓存，因格式都是byte[]）
		if (netDatas != null && netDatas.size() > 0) {
			PictureBean data = netDatas.get(position);
			
			int picNum = this.netDatas.size();
			if ((picNum % 2 == 0 || picNum == 1) && picNum != 6) { // 1、2、4则设为2列时图片宽度
				col = 2;
			} else if (picNum % 3 == 0 || picNum == 5) { // 3、5、6则设为3列时图片宽度
				col = 3;
			}
			int vWidth = (screenWidth * 3 / 4 - 5 * (col + 1)) / col; // 左侧头像占1/4
																		// 屏宽
			int vHeight = vWidth;
			holder.imageId = data.getId();
			holder.imageView.setImageResource(R.drawable.picture_no);

			// 不能直接new params！
			convertView.setLayoutParams(new AbsListView.LayoutParams(vWidth,
					vHeight));
			holder.imageView.setMaxWidth(vWidth);
			holder.imageView.setMaxHeight(vHeight);

			ImageLoader.getInstance().loadImage(true, holder.picPath,
					netDatas.get(position).getBytes(), holder.imageId,
					holder.imageView);
		}

		return convertView;
	}

	private class ViewHolder {
		String picPath;
		int imageId;
		ImageView imageView;
	}
	
	/**
	 * 存分享id，复用时可用此来判断是否可用还是另外新建adapter
	 * @param id
	 */
	private int flagShareId = -3;
	
	public void setShareId(int id) {
		this.flagShareId = id;
	}
	
	public int getShareId() {
		return flagShareId;
	}
}
