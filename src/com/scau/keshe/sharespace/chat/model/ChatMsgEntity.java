package com.scau.keshe.sharespace.chat.model;

import android.graphics.Bitmap;

public class ChatMsgEntity {

	private boolean isComMsg = true;
	
	private String text = null;
	
	private String userName;
	
	private Bitmap image;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isComMsg() {
		return isComMsg;
	}

	public void setComMsg(boolean isComMsg) {
		this.isComMsg = isComMsg;
	}

	public ChatMsgEntity() {
		// TODO Auto-generated constructor stub
	}
	public ChatMsgEntity(String username, String text, boolean iscommsg, Bitmap img) {
		this.image = img;
		this.isComMsg = iscommsg;
		this.text = text;
		this.userName = username;
	}
}
