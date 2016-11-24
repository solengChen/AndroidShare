package com.scau.keshe.sharespace.welcome.contract;


import com.scau.keshe.sharespace.BaseModel;
import com.scau.keshe.sharespace.BasePresenter;
import com.scau.keshe.sharespace.BaseView;
import com.scau.keshe.sharespace.bean.UserBean;

/**
 * 联系人模块的契约接口类
 * @author ShouLun
 *
 */
public interface LoginContract {
	interface View extends BaseView {
		boolean setIdAfterlogin(int userId);	
	}
	
	interface LoadUserView{
		void  setUserBean(UserBean user);
	}

	interface Presenter<V extends BaseView, M extends BaseModel> extends
			BasePresenter<BaseView, BaseModel> {
		void loginOrder(String username, String password);
	}
	
	interface LoadUserPresenter {
		void loadUser(int id);
	}

	interface Model extends BaseModel {
		int login(String username, String password);
	}
	interface LoadModel {
		UserBean getUser(int id);
	}
}
