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
		// TODO 向服务器发送请求，获取分享信息表单
	}

	@Override
	public String getFailMsg() {
		return ErrorMessage.Handler(requestShareBehavior.getErrorCode());
	}

	/**
	 * 初始时先从数据库的数据中取出保存的数据
	 * 
	 * @return
	 */
	@Override
	public List<ShareBean> getLocationShares() {
		List<ShareBean> shares = null;
		// TODO 数据操作
		return shares;
	}

	/**
	 * 保存最新分享内容,在activity 的onStop时调用
	 * 
	 * @param shares
	 * @return
	 */
	@Override
	public void saveShares(List<ShareBean> shares) {
		// TODO 保存数据操作

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
