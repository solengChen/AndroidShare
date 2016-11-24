package com.scau.keshe.sharespace.bean;


public class FriendsBean {
	
	/*             "id":21,                    # 好友的用户ID
	 *             "time":1466757677,          # 成为好友的时间
	 *             "username":"lisi",          # 好友用户名
	 *             "sex":"男",
	 *             "signature":"",
	 *             "role":2						# 角色ID
	 *             "registerTime":144342342    # 用户注册时间
	 *             "headImageId":13            # 头像ID
	 */
	
	private int id;
	private long time;
	private String userName;
	private String sex;
	private String signature;
	private int role;
	private long registerTime;
	private int headImageId;
	
	public FriendsBean() {
		
	}
	
	
	
	public FriendsBean(int id, long time, String userName, String sex,
			String signature, int role, long registerTime, int headImageId) {
		this.id = id;
		this.time = time;
		this.userName = userName;
		this.sex = sex;
		this.signature = signature;
		this.role = role;
		this.registerTime = registerTime;
		this.headImageId = headImageId;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
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

	

}
