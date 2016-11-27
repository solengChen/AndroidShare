package com.scau.keshe.sharespace.bean;


/**
 * �����б�������ʾ�ķ�����Ϣ
 * 
 * @author ShouLun
 * 
 */
public class ShareBean extends BaseBean{
	private int userId;
	private String userName;
	private long createTime;
	private int id;
	private int praiseCount;
	private int commentCount;
	private int[] pictureIds;
	private String content;

	public ShareBean() {

	}

	/**
	 * int userID, String userName, long, int, int, int, int[],String
	 * userID = -2����ʱʹ�ñ���Ĭ�ϻ�ӭͼƬ
	 * userName ʹ��App��
	 * createTime ϵͳ��ǰʱ��
	 * itemID -1
	 * praiseCount -1 ���οؼ�
	 * commentCount -1 ���οؼ�
	 * pictureID null
	 * content ��ӭ��������
	 * @param userId
	 * @param userName
	 * @param createTime
	 * @param itemID
	 * @param praiseCount
	 * @param commentCount
	 * @param pictureID
	 * @param content
	 */
	public ShareBean(int userId, String userName, long createTime, int itemID,
			int praiseCount, int commentCount, int[] pictureID, String content) {
		this.userId = userId;
		this.userName = userName;
		this.createTime = createTime;
		this.id = itemID;
		this.praiseCount = praiseCount;
		this.commentCount = commentCount;
		this.pictureIds = pictureID;
		this.content = content;
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

	public int getId() {
		return id;
	}

	public void setId(int itemID) {
		this.id = itemID;
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

	public int[] getPictureIds() {
		return pictureIds;
	}

	public void setPictureIds(int[] pictureID) {
		this.pictureIds = pictureID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
