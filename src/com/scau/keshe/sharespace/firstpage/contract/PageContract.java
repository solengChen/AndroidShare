package com.scau.keshe.sharespace.firstpage.contract;

import java.util.List;

import com.scau.keshe.sharespace.BaseModel;
import com.scau.keshe.sharespace.BasePresenter;
import com.scau.keshe.sharespace.BaseView;
import com.scau.keshe.sharespace.bean.ShareBean;
import com.scau.keshe.sharespace.bean.ShareListBean;
import com.scau.keshe.sharespace.firstpage.FPCallback;

//契约接口类
public interface PageContract {

	interface View extends BaseView {
		enum Option {
			GETSHARES, SAVESHARES
		}

		boolean initFragmentListInfo(List<ShareListBean> shares);

		boolean dealSharesWithSQL(Option option);

		boolean getSharesBySQL(List<ShareBean> shares);

		List<ShareBean> saveShares();
	}

	interface Presenter<V extends BaseView, M extends BaseModel> extends
			BasePresenter<BaseView, BaseModel> {
		void sendRequest(String keyword, int userId, long datetime, int orderColumn, int order, int start, int limit, boolean deleted);

		void dealShares(View.Option option);
	}

	interface Model extends BaseModel {
		List<ShareListBean> toResponse(String keyword, int userId, long datetime, int orderColumn, int order, int start, int limit, boolean deleted);

		void saveShares(List<ShareBean> shares);

		List<ShareBean> getLocationShares();

		void toSortListView();
	}
}
