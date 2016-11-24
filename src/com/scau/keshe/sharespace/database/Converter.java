package com.scau.keshe.sharespace.database;

import android.content.Context;

import com.scau.keshe.sharespace.bean.FriendsBean;
import com.scau.keshe.sharespace.bean.ShareBean;
import com.scau.keshe.sharespace.bean.TalkingBean;

public class Converter {
	private BaseDao dao;

	public Converter(Context context) {
		dao = new BaseDao(context);
	}

	public enum TYPE {
		FRIENDS_BEAN, SHARE_BEAN, TALKING_BEAN;
	}

	/**
	 * 插入数据 friendID, friendName, friendPortratPath, friendPortratID,
	 * firendShare
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertInfo(FriendsBean bean) {

		return false;

	}

	/**
	 * 插入数据 itemID, picturePath, content
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertInfo(ShareBean bean) {

		return false;

	}

	/**
	 * 插入数据 talkingId, friendPortratPath, talkingDate, talkingContent,
	 * friendName
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertInfo(TalkingBean bean) {

		return dao.excuteUpdate("insert_TalkingInfo", bean.getAllInfo());

	}

}
