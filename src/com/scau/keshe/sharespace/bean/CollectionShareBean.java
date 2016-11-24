package com.scau.keshe.sharespace.bean;

public class CollectionShareBean {
	/*             "collectionId":11,
	 *             "collectTime":14432423432,             # 收藏的时间
	 *             "share":                               # 对应的分享
	 *               {
	 *                 "shareId":1,                       # 分享ID
	 *                 "content":321,                     # 该分享的内容
	 *                 "createTime":14342424,             # 该分享创建的时间
	 *                 "praiseCount":432,                 # 赞数
	 *                 "commentCount":312                 # 评论数
	 */  
	private int collectionId;
	private long collectTime;
	private Share share;
	
	public CollectionShareBean() {}
	
	
	
	public CollectionShareBean(int collectionId, long collectTime, Share share) {
		this.collectionId = collectionId;
		this.collectTime = collectTime;
		this.share = share;
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



	public Share getShare() {
		return share;
	}



	public void setShare(Share share) {
		this.share = share;
	}



	class Share {
		private int shareId;
		private String content;
		private long createTime;
		private int praiseCount;
		private int commentCount;
		public int getShareId() {
			return shareId;
		}
		public void setShareId(int shareId) {
			this.shareId = shareId;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public long getCreateTime() {
			return createTime;
		}
		public void setCreateTime(long createTime) {
			this.createTime = createTime;
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
		
		
	}
}
