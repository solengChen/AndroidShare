package com.scau.keshe.sharespace.talking;

import android.graphics.Bitmap;

public class Talker {
	private Bitmap portrat;
	private String info;
	private String userName;
	private String date;
	public Bitmap getPortrat() {
		return portrat;
	}
	public void setPortrat(Bitmap portrat) {
		this.portrat = portrat;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public Talker(Bitmap bitmap, String info, String username, String date) {
		this.portrat = bitmap;
		this.info = info;
		this.userName = username;
		this.date = date;
	}
}
