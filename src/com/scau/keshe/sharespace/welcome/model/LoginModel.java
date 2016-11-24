package com.scau.keshe.sharespace.welcome.model;

import com.scau.keshe.sharespace.requestnet.ClientBehavior;
import com.scau.keshe.sharespace.util.ErrorMessage;
import com.scau.keshe.sharespace.welcome.contract.LoginContract.Model;

public class LoginModel implements Model {

	ClientBehavior login;
	
	public LoginModel() {
		login = ClientBehavior.getInstance();
	}

	@Override
	public String getFailMsg() {
		return ErrorMessage.Handler(login.getErrorCode());
	}

	@Override
	public void showFragmentListInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public int login(String username, String password) {
		return login.login(username, password);
	}

}
