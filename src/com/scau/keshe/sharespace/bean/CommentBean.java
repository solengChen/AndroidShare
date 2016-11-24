package com.scau.keshe.sharespace.bean;

public class CommentBean {

	/*
	 * "id":11, # 评论ID "shareId":32 # 所属分享的ID "content":"good!!", # 评论的内容
	 * "kind":0 # 评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论 "commentTime":1465565576, #
	 * 评论时间 "fromUserId":1 # 评论者ID "fromUsername":"lixiaohui" # 评论者用户名
	 * "toUserId":2 # 被评论的用户ID "toUsername":"zhangsan" # 被评论的用户ID
	 */
	private int id;
	private int shareId;
	private String content;
	private int kind;
	private long commentTime;
	private int fromUserId;
	private String fromUsername;
	private int toUserId;
	private String toUsername;

	public CommentBean() {

	}

	public CommentBean(int id, int shareId, String content, int kind,
			long commentTime, int fromUserId, String fromUsername,
			int toUserId, String toUsername) {
		this.id = id;
		this.shareId = shareId;
		this.content = content;
		this.kind = kind;
		this.commentTime = commentTime;
		this.fromUserId = fromUserId;
		this.fromUsername = fromUsername;
		this.toUserId = toUserId;
		this.toUsername = toUsername;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShareId() {
		return shareId;
	}

	public void setShareId(int shareId) {
		this.shareId = shareId;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public long getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(long commentTime) {
		this.commentTime = commentTime;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromUsername() {
		return fromUsername;
	}

	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}

	public String getToUsername() {
		return toUsername;
	}

	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}

}
