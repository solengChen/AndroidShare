package com.scau.keshe.sharespace.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class ShareListBean{

	private ShareBean share;
	private boolean hasRead;
	private int readCount;
	
	public ShareListBean() {
		// TODO Auto-generated constructor stub
	}



	public ShareBean getShare() {
		return share;
	}



	public void setShare(ShareBean share) {
		this.share = share;
	}



	public boolean isHasRead() {
		return hasRead;
	}

	public void setHasRead(boolean hasRead) {
		this.hasRead = hasRead;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}	
}
