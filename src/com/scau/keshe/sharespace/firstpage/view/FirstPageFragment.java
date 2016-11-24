package com.scau.keshe.sharespace.firstpage.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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
	 * MVPģʽ��presenter��mModel
	 */
	private FPpresenter mPresenter;
	private FPmodel mModel;

	private boolean isAtBegin;
	private ShareAdapter adapter;

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
		// �����ǩ��
		poply = (LinearLayout) view.findViewById(R.id.fpage_pop_order);
		poply.setVisibility(View.GONE);
		poply.setFocusable(false);

		listview = (FPlistView) view.findViewById(R.id.fpage_share_contents);

		orderByMonth = (TextView) view.findViewById(R.id.fpage_popitem_month);
		orderByWeek = (TextView) view.findViewById(R.id.fpage_popitem_week);
		orderByStar = (TextView) view.findViewById(R.id.fpage_popitem_star);
		orderByComment = (TextView) view
				.findViewById(R.id.fpage_popitem_comment);

		// **************************************************************
		// ʵ����presenter��model������ҵ���������ͨ������presenter���
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

		// initShareData();
		adapter = new ShareAdapter(context, MainActivity.shareData);

		listview.setAdapter(adapter);

		listview.setReflashListener(this);

		UIhandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0x111:
					listview.reflashComplete();
					adapter.notifyDataSetChanged();
					break;

				case 0x110:
					listview.reflashComplete();
					break;
				}
			};
		};
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
	 * ���ʵ�ֵĽӿڷ���
	 * 
	 * ========================================================================
	 * ====
	 */

	@Override
	public void showFailMsg(String error) {
		mPresenter.showFailMsg();

	}

	@Override
	public boolean initFragmentListInfo(List<ShareListBean> data) {
		if (data == null || data.size() == 0) {
			UIhandler.sendEmptyMessage(0x110);
			Log.i("FirstPageFragment iniFragmentListInfo--------->188",
					"list<>data���ݼ�Ϊ��");
			return false;
		} else {
			Log.i("FirstPageFragment iniFragmentListInfo---------->192",
					data.size() + "-> datasize " + MainActivity.shareData.size() + " ->shareData size");
			if (MainActivity.shareData == null) {
				MainActivity.shareData = data;
			} else {
				MainActivity.shareData.addAll(data);
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
	 * ʵ��listView����ˢ�½ӿڷ��� ����ˢ�£�������������
	 */
	@Override
	public void onReflash() {
		// ������� TODO
		MainActivity.threadPool.execute(new Runnable() {

			@Override
			public void run() {
				if (mPresenter == null || listview == null) {
					Log.i("FirstPageFragment onReflash-----", "null");
					return;
				}

				long time = new Date().getTime();
				// userid = -1��ʾ��ȡ�����û�����???????
				mPresenter.sendRequest("", -1, -1, 0, 0, start, (start + 20),
						false);
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
		// �ر����뷨����ֹ���ֱ��ı䣬Ӧ�����е�����
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
