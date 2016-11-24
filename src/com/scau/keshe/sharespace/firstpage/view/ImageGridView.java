package com.scau.keshe.sharespace.firstpage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ImageGridView extends GridView {

	public ImageGridView(Context context) {
		this(context, null);
	}

	public ImageGridView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.gridViewStyle);
	}

	public ImageGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//重写onMeasure里面方法，使界面撑到最大
//		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
