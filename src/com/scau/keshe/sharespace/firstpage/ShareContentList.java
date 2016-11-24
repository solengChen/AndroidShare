package com.scau.keshe.sharespace.firstpage;

import com.scau.keshe.sharespace.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;;

public class ShareContentList extends ListView implements OnScrollListener{

	private int totalitemCount;
	private int lastVisibleItem;
	private int firstVisibleItem;
	
	private boolean isPopViewShow = true;
	private boolean isLoading = false;
	
	private View footer;
	private View processbar;
	
	public ShareContentList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initFooter(context);
	}

	public ShareContentList(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initFooter(context);
	}

	public ShareContentList(Context context) {
		this(context, null);
	}
	
	private void initFooter(Context context) {
		footer = LayoutInflater.from(context).inflate(R.layout.list_footer, null);
		processbar = footer.findViewById(R.id.list_layout_footer);
		processbar.setVisibility(View.GONE);
		
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}
	/**
	 * 滑动停止，到开头则显示popView
	 * 到结尾则刷新
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == this.SCREEN_STATE_OFF) {
			//下拉刷新
			if(isLoading && lastVisibleItem == totalitemCount) {
				processbar.setVisibility(View.VISIBLE);
				isLoading = true;
				mListener.onLoad();
			}
			//开头显示popView
			//TODO
			if(isPopViewShow) {
				
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalitemCount = totalItemCount;
		
		
	}

	public interface shareContentListener {
		public void onLoad();
		public void popViewChanged();
	}
	
	private shareContentListener mListener;
	
	public void setInterface(shareContentListener listener) {
		this.mListener = listener;
	}
	
	public void onLoadComplete() {
		
	}
	
}
