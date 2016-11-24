package com.scau.keshe.sharespace.welcome.presenter;

import com.scau.keshe.sharespace.welcome.contract.LoginContract;

public class LoadUserBeanPresenterImpl implements
		LoginContract.LoadUserPresenter {

	private LoginContract.LoadUserView view;
	private LoginContract.LoadModel model;

	@Override
	public void loadUser(final int id) {
		// TODO Auto-generated method stub

		view.setUserBean(model.getUser(id));

	}

	public LoadUserBeanPresenterImpl(LoginContract.LoadUserView view,
			LoginContract.LoadModel model) {
		this.view = view;
		this.model = model;
	}
}
