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
		// TODO ��model��õĹ�����ҳ�����б���Ϣ����view����֪ͨ��ʾ

	}

	@Override
	public void showFailMsg() {
		// TODO ��Ϣ��ȡʧ��ʱ��model��ʧ����Ϣת��view��ʾ

		mView.showFailMsg(mModel.getFailMsg());

	}

	/**
	 * ����õķ����б���Ϣ����view��ʾ
	 */
	@Override
	public void sendRequest(String keyword, int userId, long datetime, int orderColumn,
			int order, int start, int limit, boolean deleted) {
		List<ShareListBean> shareslist = mModel.toResponse(keyword, userId, datetime,
				orderColumn, order, start, limit, deleted);

		mView.initFragmentListInfo(shareslist);

	}

	/**
	 * �����б�
	 */
	public void sortList() {
		// TODO
	}

	/**
	 * �ӱ������ݿ���ȡ��������ݣ������Ǳ�������
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
