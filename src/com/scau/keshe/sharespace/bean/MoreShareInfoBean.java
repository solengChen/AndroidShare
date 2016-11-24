package com.scau.keshe.sharespace.bean;

import java.util.List;

/**
 * 详细的分享内容信息
 * 
 * @author ShouLun
 * 
 */
public class MoreShareInfoBean {
	private int shareId;
	private PraiseInfo info;
	private commentInfos commentinfo;

	private int userId;
	private String userName;
	private long createTime;
	private int itemID;
	private int praiseCount;
	private int commentCount;
	private int[] pictureID;
	private String content;

	public MoreShareInfoBean() {

	}

	public MoreShareInfoBean(int shareId, PraiseInfo info,
			commentInfos commentinfo, int userId, String userName,
			long createTime, int itemID, int praiseCount, int commentCount,
			int[] pictureID, String content) {
		this.shareId = shareId;
		this.info = info;
		this.commentinfo = commentinfo;
		this.userId = userId;
		this.userName = userName;
		this.createTime = createTime;
		this.itemID = itemID;
		this.praiseCount = praiseCount;
		this.commentCount = commentCount;
		this.pictureID = pictureID;
		this.content = content;
	}

	public int getShareId() {
		return shareId;
	}

	public void setShareId(int shareId) {
		this.shareId = shareId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public PraiseInfo getInfo() {
		return info;
	}

	public void setInfo(PraiseInfo info) {
		this.info = info;
	}

	public commentInfos getCommentinfo() {
		return commentinfo;
	}

	public void setCommentinfo(commentInfos commentinfo) {
		this.commentinfo = commentinfo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int[] getPictureID() {
		return pictureID;
	}

	public void setPictureID(int[] pictureID) {
		this.pictureID = pictureID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	/**
	 * 内部类
	 * @author ShouLun
	 *
	 */
	public static class PraiseInfo {
		int praiseCount;
		PraiseUser[] praiseUsers;
		public int getPraiseCount() {
			return praiseCount;
		}
		public void setPraiseCount(int praiseCount) {
			this.praiseCount = praiseCount;
		}
		public PraiseUser[] getPraiseUsers() {
			return praiseUsers;
		}
		public void setPraiseUsers(PraiseUser[] praiseUsers) {
			this.praiseUsers = praiseUsers;
		}
		
	}

	public static class commentInfos {
		int commentCount;
		List<CommentBean> commentlist;

		public int getCommentCount() {
			return commentCount;
		}

		public void setCommentCount(int commentCount) {
			this.commentCount = commentCount;
		}

		public List<CommentBean> getCommentlist() {
			return commentlist;
		}

		public void setCommentlist(List<CommentBean> commentlist) {
			this.commentlist = commentlist;
		}

	}


	public static class PraiseUser {
		int _id;
		String name;

		public int get_id() {
			return _id;
		}

		public void set_id(int _id) {
			this._id = _id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
