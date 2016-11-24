package com.scau.keshe.sharespace.connector.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.scau.keshe.sharespace.bean.FriendsBean;
import com.scau.keshe.sharespace.bean.PictureBean;
import com.scau.keshe.sharespace.bean.UserBean;
import com.scau.keshe.sharespace.connector.contract.ContactContract.Model;
import com.scau.keshe.sharespace.requestnet.ClientBehavior;
import com.scau.keshe.sharespace.util.ErrorMessage;

public class ContactModel implements Model {

	private ClientBehavior requestContactsBehavior;

	public ContactModel() {
		requestContactsBehavior = ClientBehavior.getInstance();
	}

	@Override
	public String getFailMsg() {
		// TODO Auto-generated method stub
		return ErrorMessage.Handler(requestContactsBehavior.getErrorCode());
	}

	@Override
	public void showFragmentListInfo() {
		// TODO Auto-generated method stub

	}

	/**isLocation = true时
	 * 从本地数据库获取朋友信息 朋友Id 姓名name 头像portrait 性别sex 个性签名signature
	 * 否则发送请求到服务端获取联系人信息集
	 * @return
	 */

	@Override
	public List<FriendsBean> getFriends(boolean isLocation, int userId, int start, int limit) {
		
		if(isLocation) {
			//TODO从本地数据查找并返回
			return null;
		}
		else {
			return requestContactsBehavior.getFriends( start, limit);
		}
	}

	/**
	 * isLocation = true，根据本地数据库的数据获取图片路径， 然后在本地文件夹找到图片集并返回，失败返回null isLocation
	 * = false，根据图片Id发送请求获取服务端的图片集 图片Id的获取依赖toResponse中的联系人集
	 * 
	 * @param isLocation
	 * @return
	 */
	public List<Bitmap> getPictures(boolean isLocation,
			boolean ignoreIfNotExist, int[] pictureIds) {
		List<Bitmap> bms = null;
		
		if(! isLocation) {
			List<PictureBean> pictures = requestContactsBehavior.getPictures(
					ignoreIfNotExist, pictureIds);
			if (pictures == null || pictures.size() == 0) {
				return null;
			}
			bms = new ArrayList<Bitmap>();
			byte[] imgByte;
			Bitmap bitmap;
			
			for (PictureBean p : pictures) {
				imgByte = p.getBytes();
				bitmap = BitmapFactory.decodeByteArray(imgByte, 0,
						imgByte.length);
				bms.add(bitmap);
			}
		}
		
		else {
			//TODO 查找本地数据
		}

		return bms;
	}
}
