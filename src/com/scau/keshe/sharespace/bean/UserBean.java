package com.scau.keshe.sharespace.bean;

public class UserBean {
	private int UserId;
	private String Username;
	private String sex;
	private String signature;
	private long registerTime;
	private int headImageId;
	private Role mrole;
	private boolean selfShield;
	private boolean adminShield;

	public UserBean() {

	}

	public UserBean(int userId, String username, String sex, String signature,
			long registerTime, int headImageId, Role mrole, boolean selfShield,
			boolean adminShield) {
		UserId = userId;
		Username = username;
		this.sex = sex;
		this.signature = signature;
		this.registerTime = registerTime;
		this.headImageId = headImageId;
		this.mrole = mrole;
		this.selfShield = selfShield;
		this.adminShield = adminShield;
	}

	public boolean isSelfShield() {
		return selfShield;
	}

	public void setSelfShield(boolean selfShield) {
		this.selfShield = selfShield;
	}

	public boolean isAdminShield() {
		return adminShield;
	}

	public void setAdminShield(boolean adminShield) {
		this.adminShield = adminShield;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	public int getHeadImageId() {
		return headImageId;
	}

	public void setHeadImageId(int headImageId) {
		this.headImageId = headImageId;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Role getMrole() {
		return mrole;
	}

	public void setMrole(Role mrole) {
		this.mrole = mrole;
	}

}
