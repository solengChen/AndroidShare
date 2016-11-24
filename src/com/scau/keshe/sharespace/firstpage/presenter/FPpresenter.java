package com.scau.keshe.sharespace.firstpage.presenter;

import java.util.List;

import com.scau.keshe.sharespace.bean.ShareBean;
import com.scau.keshe.sharespace.bean.ShareListBean;
import com.scau.keshe.sharespace.bean.UserBean;
import com.scau.keshe.sharespace.firstpage.contract.PageContract.*;
import com.scau.keshe.sharespace.firstpage.model.FPmodel;

public class FPpresenter implements Presenter<View, FPmodel> {

	private View mView;
	private FPmodel mModel;

	public FPpresenter() {
	}

	public FPpresenter(View view, FPmodel model) {
		this.mModel = model;
		this.mView = view;
	}

	@Override
	public void getFragmentListInfo() {
		// TODO 将model获得的关于首页分享列表信息传给view，并通知显示

	}

	@Override
	public void showFailMsg() {
		// TODO 信息获取失败时，model将失败信息转给view显示

		mView.showFailMsg(mModel.getFailMsg());

	}

	/**
	 * 将获得的分享列表信息交给view显示
	 */
	@Override
	public void sendRequest(String keyword, int userId, long datetime, int orderColumn,
			int order, int start, int limit, boolean deleted) {
		List<ShareListBean> shareslist = mModel.toResponse(keyword, userId, datetime,
				orderColumn, order, start, limit, deleted);

		mView.initFragmentListInfo(shareslist);

	}

	/**
	 * 排序列表
	 */
	public void sortList() {
		// TODO
	}

	/**
	 * 从本地数据库中取分享的数据，或者是保存数据
	 * 
	 * @param <Option>
	 */
	@Override
	public void dealShares(View.Option option) {
		if (View.Option.GETSHARES == option) {
			mView.getSharesBySQL(mModel.getLocationShares());
		} else if (View.Option.SAVESHARES == option) {
			mModel.saveShares(mView.saveShares());
		}
	}
}
