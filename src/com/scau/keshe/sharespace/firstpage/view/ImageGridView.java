package com.scau.keshe.sharespace.firstpage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ImageGridView extends GridView implements android.widget.AdapterView.OnItemClickListener{

	private Context context;
	public ImageGridView(Context context) {
		this(context, null);
	}

	public ImageGridView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.gridViewStyle);
	}

	public ImageGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	private void initView() {
		this.setOnItemClickListener(this);

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//重写onMeasure里面方法，使界面撑到最大，这样能使gridView自适应
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
//		int count = getChildCount();
//		if(count > 0) {
//			int childWidth = getChildAt(0).getLayoutParams().width;
//			if(count > 3) {
//				heightMeasureSpec = childWidth * 2 + 20; 
//			}
//			else {
//				heightMeasureSpec = childWidth + 10;
//			}
//		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(context, "item " + position + " is clicked", Toast.LENGTH_SHORT).show();
	}

	
}
