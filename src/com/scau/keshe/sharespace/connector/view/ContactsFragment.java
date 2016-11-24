package com.scau.keshe.sharespace.connector.view;

import java.util.ArrayList;
import java.util.List;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.bean.FriendsBean;
import com.scau.keshe.sharespace.connector.ContactAdapter;
import com.scau.keshe.sharespace.connector.Contacts;
import com.scau.keshe.sharespace.connector.SideBar;
import com.scau.keshe.sharespace.connector.contract.ContactContract;
import com.scau.keshe.sharespace.welcome.MainActivity.FragmentListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsFragment extends Fragment implements ContactContract.View,
		FragmentListener {

	private TextView dialog;

	private SideBar bar;

	private ListView listview;

	private ContactAdapter conAdapter;

	private Context context;

	private List<FriendsBean> mfriends;
	private List<Bitmap> mportrait;

	public ContactsFragment() {

	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_connect, null);

		findView(view);

		return view;
	}

	private void findView(View view) {

		bar = (SideBar) view.findViewById(R.id.sidrbar);

		dialog = (TextView) view.findViewById(R.id.dialog);

		bar.setTextView(dialog);

		listview = (ListView) view.findViewById(R.id.connect_list);

		conAdapter = new ContactAdapter(context, getConnectInfo());
		listview.setAdapter(conAdapter);
	}

	/**
	 * 获取所有好友信息
	 * 
	 * @return
	 */
	private List<Contacts> getConnectInfo() {
		List<Contacts> info = new ArrayList<Contacts>();

		// TODO 1、加入本地数据
		// 2、加入网络数据

		return info;
	}

	public void flashListView() {
		conAdapter.notifyDataSetChanged();
	}

	@Override
	public void showFailMsg(String error) {
		Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean initFragmentListInfo(List<FriendsBean> data,
			List<Bitmap> portrait) {
		if (data == null || data.size() == 0) {
			return false;
		}
		mfriends = data;
		mportrait = portrait;
		return true;
	}

	/**
	 * isLocation = true时，在本地文件夹找图片 isLocation = false时，发送请求到服务端获取图片
	 * 图片获取失败返回false，成功返回true
	 */
	@Override
	public boolean setImagesList(boolean isLocation, List<Bitmap> pictures) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void mainRequestReflash() {
		// TODO Auto-generated method stub

	}

}
