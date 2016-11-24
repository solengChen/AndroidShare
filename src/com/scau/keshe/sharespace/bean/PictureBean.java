package com.scau.keshe.sharespace.bean;

public class PictureBean {
	/*             "id":32,                    # 图片ID
	 *             "bytes":dshdssad,           # 图片字节
	 *             "uploadTime":14678787787,   # 图片上传时间
	 *             "uploadUserId":321,         # 上传该图片的用户ID
	 */ 
	private int id;
	private byte[] bytes;
	private long uploadTime;
	private int uploadUserId;
	
	public PictureBean() {}

	public PictureBean(int id, byte[] imgByte, long uploadTime, int uploadUserId) {
		this.id = id;
		this.bytes = imgByte;
		this.uploadTime = uploadTime;
		this.uploadUserId = uploadUserId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] imgByte) {
		this.bytes = imgByte;
	}

	public long getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}

	public int getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(int uploadUserId) {
		this.uploadUserId = uploadUserId;
	}
	
	
}
