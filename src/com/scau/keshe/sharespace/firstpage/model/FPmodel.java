package com.scau.keshe.sharespace.firstpage.model;

import java.util.List;

import com.scau.keshe.sharespace.bean.ShareBean;
import com.scau.keshe.sharespace.bean.ShareListBean;
import com.scau.keshe.sharespace.firstpage.FPCallback;
import com.scau.keshe.sharespace.firstpage.contract.PageContract.Model;
import com.scau.keshe.sharespace.requestnet.ClientBehavior;
import com.scau.keshe.sharespace.util.ErrorMessage;

public class FPmodel implements Model {

	private ClientBehavior requestShareBehavior;

	public FPmodel() {
		requestShareBehavior = ClientBehavior.getInstance();
	}

	@Override
	public void showFragmentListInfo() {
		// TODO ��������������󣬻�ȡ������Ϣ��
	}

	@Override
	public String getFailMsg() {
		return ErrorMessage.Handler(requestShareBehavior.getErrorCode());
	}

	/**
	 * ��ʼʱ�ȴ����ݿ��������ȡ�����������
	 * 
	 * @return
	 */
	@Override
	public List<ShareBean> getLocationShares() {
		List<ShareBean> shares = null;
		// TODO ���ݲ���
		return shares;
	}

	/**
	 * �������·�������,��activity ��onStopʱ����
	 * 
	 * @param shares
	 * @return
	 */
	@Override
	public void saveShares(List<ShareBean> shares) {
		// TODO �������ݲ���

	}

	@Override
	public List<ShareListBean> toResponse(String keyword, int userId,
			long datetime, int orderColumn, int order, int start, int limit,
			boolean deleted) {
		return requestShareBehavior.getShares(keyword, userId, datetime,
				orderColumn, order, start, limit, deleted);
	}

	@Override
	public void toSortListView() {
		// TODO
	}

}
