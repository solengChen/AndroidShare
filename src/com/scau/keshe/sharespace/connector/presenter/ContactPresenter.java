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
	 * ���ұ������ݿ����ϵ����Ϣ
	 */
	public void findFriendsInfoOnLocation() {

	}

	/**
	 * isLocation = trueʱ�����������ȡ��ϵ����Ϣ��ͷ�񲢷���
	 * isLocation = falseʱ���ڱ������ݿ��ȡ��ϵ����Ϣ��ͷ��
	 * userId, start, limitֻ����isLocationΪfalseʱ��Ч
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
			//TODO �ӱ������ݻ�ȡ
		}
		
		isSuccess = mView.initFragmentListInfo(data, portrait);
	}

}
