package com.scau.keshe.sharespace.bean;

public class CollectionUserBean {
	/*             "collectionId":12,            # 该收藏ID
	 *             "collectTime":14342432423,    # 收藏时间
	 *             "user":                       # 对应的用户
	 *               {
	 *                 "userId":32,              # 用户ID
	 *                 "username":"李晓明",        # 用户名
	 *                 "headImageId":321         # 头像ID
	 *               }
	 */
	private int collectionId;
	private long collectTime;
	private User user;
	
	public CollectionUserBean() {
		
	}
	
	
	
	public CollectionUserBean(int collectionId, long collectTime, User user) {
		this.collectionId = collectionId;
		this.collectTime = collectTime;
		this.user = user;
	}



	public int getCollectionId() {
		return collectionId;
	}



	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
	}



	public long getCollectTime() {
		return collectTime;
	}



	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	class User {
		private int userId;
		private String username;
		private int headImageId;
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public int getHeadImageId() {
			return headImageId;
		}
		public void setHeadImageId(int headImageId) {
			this.headImageId = headImageId;
		}
		
		
	}
}
