package com.scau.keshe.sharespace.requestnet;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cc.lixiaohui.share.client.IShareClient;
import cc.lixiaohui.share.client.ShareClientFactory;
import cc.lixiaohui.share.client.util.ClientException;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scau.keshe.sharespace.bean.CollectionShareBean;
import com.scau.keshe.sharespace.bean.CollectionUserBean;
import com.scau.keshe.sharespace.bean.CommentBean;
import com.scau.keshe.sharespace.bean.ForbidenWordBean;
import com.scau.keshe.sharespace.bean.FriendsBean;
import com.scau.keshe.sharespace.bean.MoreShareInfoBean;
import com.scau.keshe.sharespace.bean.PictureBean;
import com.scau.keshe.sharespace.bean.ShareBean;
import com.scau.keshe.sharespace.bean.ShareListBean;
import com.scau.keshe.sharespace.bean.SortType;
import com.scau.keshe.sharespace.bean.UserBean;
import com.scau.keshe.sharespace.util.ClientStartException;

/**
 * 用户的各种访问服务器操作 包括登录、注册、注销、获取信息 更新用户数据
 * 
 * @author ShouLun
 * 
 */
public class ClientBehavior {
	private IShareClient client = ShareClientFactory.newInstance(
			"172.16.27.181", 8989);

	private int errorCode;

	private static volatile ClientBehavior behavior;

	/**
	 * 实例化ClientBehavior时，创建一个ClientBehavior单例 并且启动它，一个客户端只能拥有一个IShareClient对象
	 */
	private ClientBehavior() {

	}

	public static ClientBehavior getInstance() {
		if (behavior == null) {
			synchronized (ClientBehavior.class) {
				if (behavior == null) {
					behavior = new ClientBehavior();
				}
			}
		}
		return behavior;
	}

	public void startClient() throws ClientStartException {
		try {
			if (client != null) {
				client.start();
			} else {
				Log.i("start client warming------------->", "client为空！");
			}
		} catch (LifeCycleException e) {
			Log.e("ClientBehavior start Exception--------->", e.getMessage());
			throw new ClientStartException(e);
		}
	}

	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * 用户注册，返回注册id，失败返回-1
	 * 
	 * @param clientName
	 * @param password
	 */
	public int regist(String clientName, String password) {

		int userId = -1;

		try {

			String json = client.register(clientName, password);
			Log.i("json regist-------------->", json);
			JSONObject jobject = JSONObject.parseObject(json);

			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return -1;
			}

			userId = jobject.getJSONObject("result").getInteger("userId");

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return userId;
	}

	/**
	 * 用户登录，失败返回-1
	 * 
	 * @param clientName
	 * @param password
	 * @return
	 */
	public int login(String clientName, String password) {

		int userId = -1;

		try {
			String json = client.login(clientName, password);
			Log.i("json login-------------->", json);
			JSONObject jobject = JSONObject.parseObject(json);
			userId = jobject.getJSONObject("result").getInteger("userId");

			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return -1;
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}

		errorCode = -1;
		return userId;
	}

	/**
	 * 注销用户
	 */
	public void logout() {
		try {
			String json = client.logout();
			Log.i("json logout-------------->", json);
			JSONObject jobject = JSONObject.parseObject(json);

			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取用户信息，传入用户Id即可 可获取自己的和朋友的
	 * 
	 * @param userId
	 * @return
	 */
	public UserBean getUserInfo(int userId) {
		UserBean user = null;
		try {
			String json = client.getUser(userId);
			/*
			 * JSONObject jobject = JSONObject.parseObject(json);
			 * 
			 * UserBean.role role = jobject.getObject("role",
			 * UserBean.role.class);
			 * 
			 * String clientId = jobject.getString("userId"); String name =
			 * jobject.getString("username"); String sex =
			 * jobject.getString("sex"); String signature =
			 * jobject.getString("signature"); String time =
			 * jobject.getString("registerTime"); String imageId =
			 * jobject.getString("headImageId");
			 * 
			 * user = new UserBean(clientId, name, sex, signature, time,
			 * imageId, role);
			 */

			Log.i("ClientBehavior getUserInfo", json);
			JSONObject jobject = JSONObject.parseObject(json);

			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}
			JSONObject result = jobject.getJSONObject("result");
			user = JSON.parseObject(result.toJSONString(), UserBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 更新用户信息，失败返回false，成功返回true
	 * 
	 * @param password
	 * @param sex
	 * @param signature
	 * @param headImageId
	 * @return
	 */
	public boolean updateUser(String password, String sex, String signature,
			int headImageId) {
		try {
			String json = client.updateUser(password, sex, signature,
					headImageId);
			JSONObject jobject = JSONObject.parseObject(json);

			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 分享时屏蔽所有人
	 * 
	 * @param userId
	 * @return
	 */
	public boolean shield(int userId) {
		try {
			String json = client.shield(userId);
			JSONObject jobject = JSONObject.parseObject(json);

			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 获得分享列表
	 * 
	 * @param keyword
	 * @param order
	 *            0 时间 1赞 2评论 orderColum 0 逆序 1 正序 orderType
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<ShareListBean> getShares(String keyword, int userId, long datetime,
			int orderColumn, int order, int start, int limit, boolean deleted) {
		List<ShareListBean> shares = null;

		try {
			String json = client.getShares(keyword, userId, datetime,
					orderColumn, order, start, limit, deleted);
			Log.i("ClientBehavior getShares-------------->266", json);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");

			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}
			JSONArray result = jobject.getJSONObject("result").getJSONArray(
					"shares");

			// int count = jobject.getInteger("count");

			shares = JSON.parseArray(result.toJSONString(), ShareListBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return shares;
	}

	/**
	 * 获取详细的分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	public MoreShareInfoBean getShare(int shareId) {
		MoreShareInfoBean shareInfo = null;

		try {
			String json = client.getShare(shareId);
			Log.i("ClientBehavior more share info--------------->299", json);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}

			JSONObject result = jobject.getJSONObject("result");
			// int count = jobject.getInteger("count");

			shareInfo = jobject.getObject(result.toJSONString(),
					MoreShareInfoBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return shareInfo;
	}

	/**
	 * 删除分享
	 * 
	 * @return
	 */
	public boolean deleteShare(int shareId, boolean physically) {
		try {
			String json = client.deleteShare(shareId, physically);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 发布分享，返回分享的ID
	 * 
	 * @param content
	 * @param picturesIds
	 * @return
	 */
	public int publishShare(String content, int[] picturesIds) {
		int shareId = -1;
		try {
			String json = client.publishShare(content, picturesIds);
			JSONObject jobject = JSONObject.parseObject(json);
			Log.i("ClientBehavior publishShare--------------> 353", json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return -1;
			}

			shareId = jobject.getJSONObject("result").getInteger("shareId");

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return shareId;
	}

	/**
	 * 获取分享的评论
	 * 
	 * @param shareId
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<CommentBean> getComments(int shareId, int start, int limit) {
		List<CommentBean> comments = null;
		try {
			String json = client.getComments(shareId, start, limit);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}
			JSONObject result = jobject.getJSONObject("result");
			comments = JSON.parseArray(result.getJSONArray("list")
					.toJSONString(), CommentBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return comments;
	}

	/**
	 * 删除用户评论
	 * 
	 * @param commentId
	 * @return
	 */
	public boolean deleteComment(int commentId) {

		try {
			String json = client.deleteComment(commentId);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 评论分享
	 * 
	 * @param shareId
	 * @param content
	 * @return
	 */
	public boolean publishComment(int shareId, int toUserId, String content) {
		try {
			String json = client.publishComment(shareId, toUserId, content);
			JSONObject jobject = JSONObject.parseObject(json);
			Log.i("ClientBehavior publishComment-------->", json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 获取朋友信息
	 * 
	 * @param userId
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<FriendsBean> getFriends(int start, int limit) {

		List<FriendsBean> friends = null;

		try {
			String json = client.getFriends(start, limit);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}

			JSONObject result = jobject.getJSONObject("result");
			friends = JSON.parseArray(result.getJSONArray("list")
					.toJSONString(), FriendsBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return friends;
	}

	/**
	 * 删除好友
	 * 
	 * @param friendId
	 * @return
	 */
	public boolean deleteFriend(int friendId) {

		try {
			String json = client.deleteFriend(friendId);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 单张图片上传,失败返回-1
	 * 
	 * @param suffix
	 * @param bytes
	 * @return
	 */
	public int uploadPicture(String suffix, byte[] bytes) {
		int id = -1;
		try {
			String json = client.uploadPicture(suffix, bytes);
			JSONObject jobject = JSONObject.parseObject(json);
			Log.i("ClientBehavior uploadPicture-------------->504", json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return -1;
			}
			id = jobject.getJSONObject("result").getInteger("id");

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return id;
	}

	/**
	 * 根据id获取所有图片信息
	 * 
	 * @param ignoreIfNotExist
	 * @param pictureIds
	 * @return
	 */
	public List<PictureBean> getPictures(boolean ignoreIfNotExist,
			int[] pictureIds) {
		List<PictureBean> pictures = null;
		try {
			String json = client.getPictures(ignoreIfNotExist, pictureIds);
			JSONObject jobject = JSONObject.parseObject(json);
			Log.i("ClientBehavior getPictures json---------->540", json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}

			JSONObject result = jobject.getJSONObject("result");
			JSONArray shares = result.getJSONArray("pictures");
			pictures = JSON
					.parseArray(shares.toJSONString(), PictureBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return pictures;
	}

	/**
	 * 根据ID删除图片
	 * 
	 * @param pictureId
	 * @return
	 */
	public boolean deletePicture(int pictureId) {
		try {
			String json = client.deletePicture(pictureId);
			JSONObject jobject = JSONObject.parseObject(json);
			Log.i("ClientBehavior deletePicture--------->569", json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 获取用户收藏中收藏用户的列表
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<CollectionUserBean> getUserCollection(int start, int limit) {
		List<CollectionUserBean> collections = null;
		try {
			String json = client.getUserCollection(start, limit);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}

			JSONObject result = jobject.getJSONObject("result");
			collections = JSON.parseArray(result.getJSONArray("collections")
					.toJSONString(), CollectionUserBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return collections;
	}

	/**
	 * 获取用户收藏中收藏的分享的列表
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<CollectionShareBean> getShareCollection(int start, int limit) {
		List<CollectionShareBean> collections = null;
		try {
			String json = client.getShareCollection(start, limit);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}

			JSONObject result = jobject.getJSONObject("result");
			collections = JSON.parseArray(result.getJSONArray("collections")
					.toJSONString(), CollectionShareBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return collections;
	}

	/**
	 * 根据id删除收藏列表中的某个用户
	 * 
	 * @param userId
	 * @return
	 */
	public boolean unCollectUser(int userId) {
		try {
			String json = client.unCollectUser(userId);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 根据id删除收藏列表中的某个分享
	 * 
	 * @param userId
	 * @return
	 */
	public boolean unCollectShare(int shareId) {
		try {
			String json = client.unCollectShare(shareId);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 获取所有排序方式
	 * 
	 * @return
	 */
	public List<SortType> getSortings() {
		List<SortType> sortType = null;
		try {
			String json = client.getSortings();
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}

			JSONObject result = jobject.getJSONObject("result");
			sortType = JSON.parseArray(result.getJSONArray("sortings")
					.toJSONString(), SortType.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return sortType;
	}

	/**
	 * 获取所有管理员设置的敏感词
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<ForbidenWordBean> getForbidenWords(int start, int limit) {
		List<ForbidenWordBean> forbidenWords = null;
		try {
			String json = client.getForbidenWords(start, limit);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}

			JSONObject result = jobject.getJSONObject("result");
			forbidenWords = JSON.parseArray(result.getJSONArray("words")
					.toJSONString(), ForbidenWordBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return forbidenWords;
	}

	/**
	 * 获取所有已删除的敏感词
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<ForbidenWordBean> getDeletedForbidenWords(int start, int limit) {
		List<ForbidenWordBean> forbidenWords = null;
		try {
			String json = client.getDeletedForbidenWords(start, limit);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return null;
			}

			JSONObject result = jobject.getJSONObject("result");
			forbidenWords = JSON.parseArray(result.getJSONArray("words")
					.toJSONString(), ForbidenWordBean.class);

		} catch (ClientException e) {
			e.printStackTrace();
		}
		return forbidenWords;
	}

	/**
	 * 根据敏感词ID删除敏感词
	 * 
	 * @param wordId
	 * @return
	 */
	public boolean deleteForbidendWord(int wordId) {
		try {
			String json = client.deleteForbidendWord(wordId);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 根据敏感词ID，恢复已删除的敏感词
	 * 
	 * @param wordId
	 * @return
	 */
	public boolean recoverForbidendWord(int wordId) {
		try {
			String json = client.recoverForbidendWord(wordId);
			JSONObject jobject = JSONObject.parseObject(json);
			int status = jobject.getInteger("status");
			if (status != 0) {
				errorCode = jobject.getInteger("errcode");
				return false;
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		return true;
	}
}
