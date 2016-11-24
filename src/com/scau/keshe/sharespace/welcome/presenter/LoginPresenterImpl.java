package com.scau.keshe.sharespace.welcome.presenter;


import android.util.Log;

import com.scau.keshe.sharespace.welcome.contract.LoginContract.*;

public class LoginPresenterImpl implements Presenter<View, Model> {

	private View mView;
	private Model mModel;
	
	public LoginPresenterImpl(View view, Model model) {
		mView = view;
		mModel = model;
	}

	@Override
	public void showFailMsg() {
		mView.showFailMsg(mModel.getFailMsg());
	}

	@Override
	public void getFragmentListInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loginOrder(String username, String password) {
		if(mView.setIdAfterlogin(mModel.login(username, password))) {
			Log.i("LoginPresenterImpl login success--------->32", "presenterµÇÂ¼²Ù×÷³É¹¦...");
		}
	}

}
