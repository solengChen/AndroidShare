package com.scau.keshe.sharespace.connector.presenter;

import java.lang.reflect.Array;
import java.util.List;

import android.graphics.Bitmap;

import com.scau.keshe.sharespace.bean.FriendsBean;
import com.scau.keshe.sharespace.connector.contract.ContactContract.*;
import com.scau.keshe.sharespace.connector.model.ContactModel;

public class ContactPresenter implements Presenter<View, ContactModel> {

	private View mView;
	private ContactModel mModel;

	private boolean isSuccess = true;

	public ContactPresenter() {

	}

	public ContactPresenter(View view, ContactModel model) {
		this.mModel = model;
		this.mView = view;
	}

	@Override
	public void showFailMsg() {
		mView.showFailMsg(mModel.getFailMsg());
	}

	@Override
	public void getFragmentListInfo() {
		// TODO Auto-generated method stub

	}

	/**
	 * 查找本地数据库的联系人信息
	 */
	public void findFriendsInfoOnLocation() {

	}

	/**
	 * isLocation = true时，发送请求获取联系人信息和头像并返回
	 * isLocation = false时，在本地数据库获取联系人信息和头像
	 * userId, start, limit只有在isLocation为false时有效
	 */
	@Override
	public void toGetFriendsInfo(boolean isLocation, int userId, int start,
			int limit) {
		
		List<FriendsBean> data = null;
		List<Bitmap> portrait = null;
		
		if(! isLocation) {
			int[] imagesId;
			data = mModel.getFriends(isLocation,
					userId, start, limit);
			int imgNum = data.size();
			
			imagesId = new int[imgNum];
			for(int i = 0; i < imgNum; i++) {
				imagesId[i] = data.get(i).getHeadImageId();
			}
			portrait = mModel.getPictures(isLocation, true, imagesId);
		}
		else {
			//TODO 从本地数据获取
		}
		
		isSuccess = mView.initFragmentListInfo(data, portrait);
	}

}
