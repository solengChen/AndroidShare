package com.scau.keshe.sharespace.connector.contract;

import java.util.List;

import android.graphics.Bitmap;

import com.scau.keshe.sharespace.BaseModel;
import com.scau.keshe.sharespace.BasePresenter;
import com.scau.keshe.sharespace.BaseView;
import com.scau.keshe.sharespace.bean.FriendsBean;

/**
 * 联系人模块的契约接口类
 * @author ShouLun
 *
 */
public interface ContactContract {
	interface View extends BaseView {
		boolean initFragmentListInfo(List<FriendsBean> data, List<Bitmap> images);
		boolean setImagesList(boolean isLocation, List<Bitmap> pictures);
	}

	interface Presenter<V extends BaseView, M extends BaseModel> extends
			BasePresenter<BaseView, BaseModel> {
		void toGetFriendsInfo(boolean isLocation, int userId, int start, int limit);
	}

	interface Model extends BaseModel {
		List<FriendsBean> getFriends(boolean isLocation, int userId, int start, int limit);
	}
}
