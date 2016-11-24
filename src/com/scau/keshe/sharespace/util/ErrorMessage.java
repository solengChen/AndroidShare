package com.scau.keshe.sharespace.util;


public class ErrorMessage {

	public ErrorMessage() {
		// TODO Auto-generated constructor stub
	}

	public static String Handler(int code) {
		String message = null;
		switch (code) {
		case 0:
			message = "未知错误！";
			break;

		case 1:
			message = "数据库异常！";
			break;

		case 2:
			message = "服务器异常！";
			break;

		case 3:
			message = "权限不足！";
			break;

		case 4:
			message = "参数错误！";
			break;

		case 5:
			message = "找不到资源！";
			break;

		case 10:
			message = "用户名已存在！";
			break;

		case 6:
			message = "服务器异常！";
			break;

		case 7:
			message = "服务器无法连接！";
			break;

		case 8:
			message = "请求超时！";
			break;

		case 9:
			message = "用户不存在！";
			break;

		case 11:
			message = "用户名或密码错误！";
			break;
		}
		return message;
	}
}
