package com.scau.keshe.sharespace.bean;

import android.graphics.Bitmap;

public class TalkingBean {
	private String talkingId;
	private String friendPortratPath;
	private String talkingDate;
	private String talkingContent;
	private String friendName;

	public TalkingBean(String id, String portratPath, String date,
			String content, String name) {
		this.friendName = name;
		this.friendPortratPath = portratPath;
		this.talkingContent = content;
		this.talkingDate = date;
		this.talkingId = id;
	}

	public String[] getAllInfo() {
		String[] info = new String[] { talkingId, friendPortratPath,
				talkingDate, talkingContent, friendName };
		return info;
	}

	public String getTalkingId() {
		return talkingId;
	}

	public void setTalkingId(String talkingId) {
		this.talkingId = talkingId;
	}

	public String getFriendPortrat() {
		return friendPortratPath;
	}

	public void setFriendPortrat(String friendPortratPath) {
		this.friendPortratPath = friendPortratPath;
	}

	public String getTalkingDate() {
		return talkingDate;
	}

	public void setTalkingDate(String talkingDate) {
		this.talkingDate = talkingDate;
	}

	public String getTalkingContent() {
		return talkingContent;
	}

	public void setTalkingContent(String talkingContent) {
		this.talkingContent = talkingContent;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

}
