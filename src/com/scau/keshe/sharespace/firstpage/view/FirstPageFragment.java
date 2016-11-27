package com.scau.keshe.sharespace.firstpage.view;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.bean.ShareBean;
import com.scau.keshe.sharespace.bean.ShareListBean;
import com.scau.keshe.sharespace.firstpage.SearchEditText;
import com.scau.keshe.sharespace.firstpage.contract.PageContract;
import com.scau.keshe.sharespace.firstpage.model.FPmodel;
import com.scau.keshe.sharespace.firstpage.model.ShareAdapter;
import com.scau.keshe.sharespace.firstpage.presenter.FPpresenter;
import com.scau.keshe.sharespace.welcome.MainActivity;
import com.scau.keshe.sharespace.welcome.MainActivity.FragmentListener;

public class FirstPageFragment extends Fragment implements
		View.OnClickListener, PageContract.View,
		SearchEditText.showPopSortItemListener, FPlistView.ReflashListener,
		FragmentListener {

	private Context context;
	private SearchEditText editText;
	private LinearLayout poply;
	private FPlistView listview;

	private TextView orderByMonth;
	private TextView orderByWeek;
	private TextView orderByStar;
	private TextView orderByComment;

	/**
	 * MVP模式，presenter与mModel
	 */
	private FPpresenter mPresenter;
	private FPmodel mModel;

	private boolean isAtBegin;

	private ShareAdapter shareAdapter;
	private Handler UIhandler;

	private static int start = 0;

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.fragment_first_page, null);
		findView(view);
		setEvent();
		Log.i("FirstPageFragment onCreateView-----", "oncreate");
		return view;
	}

	private void findView(View view) {
		editText = (SearchEditText) view.findViewById(R.id.page_searchView);
		editText.setShowPopSortItemListener(this);
		editText.setPressed(false);
		// 排序标签总
		poply = (LinearLayout) view.findViewById(R.id.fpage_pop_order);
		poply.setVisibility(View.GONE);
		poply.setFocusable(false);

		listview = (FPlistView) view.findViewById(R.id.fpage_share_contents);

		orderByMonth = (TextView) view.findViewById(R.id.fpage_popitem_month);
		orderByWeek = (TextView) view.findViewById(R.id.fpage_popitem_week);
		orderByStar = (TextView) view.findViewById(R.id.fpage_popitem_star);
		orderByComment = (TextView) view
				.findViewById(R.id.fpage_popitem_comment);

		shareAdapter = new ShareAdapter(context, MainActivity.bufShareData);

		// **************************************************************
		// 实例化presenter和model，后续业务操作都是通过桥梁presenter完成
		// **************************************************************
		mModel = new FPmodel();
		mPresenter = new FPpresenter(this, mModel);

	}

	/*
	 * private void initShareData() { shareData = new
	 * ArrayList<ShareListBean>();
	 * 
	 * }
	 */

	private void setEvent() {
		orderByComment.setOnClickListener(this);
		orderByStar.setOnClickListener(this);
		orderByWeek.setOnClickListener(this);
		orderByMonth.setOnClickListener(this);

		initBufData();
		listview.setListScrollStatusListener(shareAdapter);
		listview.setAdapter(shareAdapter);
		listview.setReflashListener(this);

		UIhandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0x111:
					shareAdapter.notifyDataSetChanged();
					listview.reflashComplete();
					Log.i("消息0x111被接收？", "是的");
					break;

				case 0x110:
					mPresenter.showFailMsg();
					listview.reflashComplete();
					Log.i("消息0x110被接收？？", "是的");
					break;
				}
			};
		};
	}

	/**
	 * 初始化缓存数据，存入欢迎内容
	 */
	private void initBufData() {
		// Log.i("shareData的长度为 = ", "" + MainActivity.bufShareData.size());
		if (MainActivity.bufShareData.size() == 0) {
			ShareListBean welcome = new ShareListBean();
			welcome.setShare(new ShareBean(-1, "巨信", new Date().getTime(), -2,
					-1, -1, null, "欢迎使用本应用！"));
			MainActivity.bufShareData.add(welcome);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fpage_popitem_month:

			break;

		case R.id.fpage_popitem_week:

			break;

		case R.id.fpage_popitem_star:

			break;

		case R.id.fpage_popitem_comment:

			break;
		}
	}

	/**
	 * ========================================================================
	 * ====
	 * 
	 * 完成实现的接口方法
	 * 
	 * ========================================================================
	 * ====
	 */

	@Override
	public void showFailMsg(String error) {
		if (error != null) {
			Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean initFragmentListInfo(List<ShareListBean> data) {
		if (data == null || data.size() == 0) {
			UIhandler.sendEmptyMessage(0x110);
			return false;
		} else {
			if (MainActivity.bufShareData == null) {
				MainActivity.bufShareData = data;
			} else {
				MainActivity.bufShareData.addAll(0, data);
			}
			start += data.size();
			UIhandler.sendEmptyMessage(0x111);
		}

		// adapter.notify TODO
		return true;
	}

	private boolean isShowing = false;

	@Override
	public void show() {
		if (!isShowing) {
			poply.setVisibility(View.VISIBLE);
			poply.setFocusable(true);
			isShowing = true;
		}
	}

	@Override
	public void dismiss() {
		if (isShowing) {
			poply.setVisibility(View.GONE);
			poply.setFocusable(false);
			isShowing = false;
		}

	}

	/**
	 * 实现listView下拉刷新接口方法 下拉刷新，请求网络数据
	 */
	@Override
	public void onReflash() {
		// 添加数据 TODO
		MainActivity.threadPool.execute(new Runnable() {

			@Override
			public void run() {
				if (mPresenter == null || listview == null) {
					Log.i("FirstPageFragment onReflash-----", "null");
					return;
				} else {
					// userid = -1表示获取所有用户分享???????
					mPresenter.sendRequest("", -1, -1, 0, 0, start,
							(start + 20), false);
				}
			}
		});

	}

	@Override
	public boolean dealSharesWithSQL(Option option) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSharesBySQL(List<ShareBean> shares) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ShareBean> saveShares() {
		// TODO Auto-generated method stub
		// List<ShareBean> shares
		return null;
	}

	@Override
	public void mainRequestReflash() {
		onReflash();
	}

	@Override
	public void popTabDismiss() {
		this.dismiss();
		// 关闭输入法，阻止布局被改变，应该是有点卵用
		InputMethodManager inputMethod = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputMethod.hideSoftInputFromWindow(editText.getWindowToken(), 0);

		/*
		 * if (inputMethod.isActive()) {
		 * inputMethod.hideSoftInputFromInputMethod(this.getActivity()
		 * .getCurrentFocus().getWindowToken(),
		 * InputMethodManager.HIDE_NOT_ALWAYS); }
		 */
		editText.setCursorVisible(false);
		editText.clearFocus();

	}

}
