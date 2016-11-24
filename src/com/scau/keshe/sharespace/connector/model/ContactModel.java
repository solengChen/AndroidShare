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

	/**isLocation = trueʱ
	 * �ӱ������ݿ��ȡ������Ϣ ����Id ����name ͷ��portrait �Ա�sex ����ǩ��signature
	 * ���������󵽷���˻�ȡ��ϵ����Ϣ��
	 * @return
	 */

	@Override
	public List<FriendsBean> getFriends(boolean isLocation, int userId, int start, int limit) {
		
		if(isLocation) {
			//TODO�ӱ������ݲ��Ҳ�����
			return null;
		}
		else {
			return requestContactsBehavior.getFriends( start, limit);
		}
	}

	/**
	 * isLocation = true�����ݱ������ݿ�����ݻ�ȡͼƬ·���� Ȼ���ڱ����ļ����ҵ�ͼƬ�������أ�ʧ�ܷ���null isLocation
	 * = false������ͼƬId���������ȡ����˵�ͼƬ�� ͼƬId�Ļ�ȡ����toResponse�е���ϵ�˼�
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
			//TODO ���ұ�������
		}

		return bms;
	}
}
