package com.scau.keshe.sharespace.util;


public class ErrorMessage {

	public ErrorMessage() {
		// TODO Auto-generated constructor stub
	}

	public static String Handler(int code) {
		String message = null;
		switch (code) {
		case 0:
			message = "δ֪����";
			break;

		case 1:
			message = "���ݿ��쳣��";
			break;

		case 2:
			message = "�������쳣��";
			break;

		case 3:
			message = "Ȩ�޲��㣡";
			break;

		case 4:
			message = "��������";
			break;

		case 5:
			message = "�Ҳ�����Դ��";
			break;

		case 10:
			message = "�û����Ѵ��ڣ�";
			break;

		case 6:
			message = "�������쳣��";
			break;

		case 7:
			message = "�������޷����ӣ�";
			break;

		case 8:
			message = "����ʱ��";
			break;

		case 9:
			message = "�û������ڣ�";
			break;

		case 11:
			message = "�û������������";
			break;
		}
		return message;
	}
}
