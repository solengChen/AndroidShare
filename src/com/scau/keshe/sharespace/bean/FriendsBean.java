package com.scau.keshe.sharespace.bean;


public class FriendsBean {
	
	/*             "id":21,                    # ���ѵ��û�ID
	 *             "time":1466757677,          # ��Ϊ���ѵ�ʱ��
	 *             "username":"lisi",          # �����û���
	 *             "sex":"��",
	 *             "signature":"",
	 *             "role":2						# ��ɫID
	 *             "registerTime":144342342    # �û�ע��ʱ��
	 *             "headImageId":13            # ͷ��ID
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
