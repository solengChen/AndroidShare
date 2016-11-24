package com.scau.keshe.sharespace.welcome.model;

import com.scau.keshe.sharespace.bean.UserBean;
import com.scau.keshe.sharespace.requestnet.ClientBehavior;
import com.scau.keshe.sharespace.welcome.MainActivity;
import com.scau.keshe.sharespace.welcome.contract.LoginContract.LoadModel;

public class LoadUserBeanModelImpl implements LoadModel {

	private ClientBehavior loadUser = ClientBehavior.getInstance();
	
	public LoadUserBeanModelImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserBean getUser(final int id) {
		UserBean user = loadUser.getUserInfo(id);
		return user;
	}

}
