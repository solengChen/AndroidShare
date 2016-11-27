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

	private List<String> datas; // ��������
	private List<PictureBean> netDatas; // ��������
	private String dirPath;
	private Context context;

	private int screenWidth; // ��Ļ���
	private int col = 2;
	/**
	 * ���ر���ͼƬʱ���ø÷���
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
	 * ֱ�ӻ�ȡ����ͼƬ�����ش˷���
	 * 
	 * @param context
	 * @param datas
	 */
	public ImageGridAdapter(Context context, List<PictureBean> datas) {
		this.netDatas = datas;
		initAdapter(context);
	}

	/**
	 * ���ñ����������ݣ��������ݽ��ÿ�
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
	 * ���������������ݣ��������ݽ��ÿ�
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
		
		//����ͼƬ
		holder.imageId = -1;
		holder.imageView.setImageResource(R.drawable.picture_no);
		holder.picPath = null;
		
		// ���ر���ͼƬ����
		if (datas != null && datas.size() != 0) {

		}

		// �������ݼ���ͼƬ(�������棬���ʽ����byte[]��
		if (netDatas != null && netDatas.size() > 0) {
			PictureBean data = netDatas.get(position);
			
			int picNum = this.netDatas.size();
			if ((picNum % 2 == 0 || picNum == 1) && picNum != 6) { // 1��2��4����Ϊ2��ʱͼƬ���
				col = 2;
			} else if (picNum % 3 == 0 || picNum == 5) { // 3��5��6����Ϊ3��ʱͼƬ���
				col = 3;
			}
			int vWidth = (screenWidth * 3 / 4 - 5 * (col + 1)) / col; // ���ͷ��ռ1/4
																		// ����
			int vHeight = vWidth;
			holder.imageId = data.getId();
			holder.imageView.setImageResource(R.drawable.picture_no);

			// ����ֱ��new params��
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
	 * �����id������ʱ���ô����ж��Ƿ���û��������½�adapter
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
