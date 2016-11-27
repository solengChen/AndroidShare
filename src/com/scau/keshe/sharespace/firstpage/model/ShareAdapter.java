package com.scau.keshe.sharespace.firstpage.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.util.VersionInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.bean.PictureBean;
import com.scau.keshe.sharespace.bean.ShareBean;
import com.scau.keshe.sharespace.bean.ShareListBean;
import com.scau.keshe.sharespace.bean.UserBean;
import com.scau.keshe.sharespace.database.BaseDao;
import com.scau.keshe.sharespace.dealImage.ImageLoader;
import com.scau.keshe.sharespace.firstpage.view.FPlistView.ListScrollStatusListener;
import com.scau.keshe.sharespace.firstpage.view.ImageGridView;
import com.scau.keshe.sharespace.requestnet.ClientBehavior;
import com.scau.keshe.sharespace.util.ErrorMessage;
import com.scau.keshe.sharespace.welcome.MainActivity;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

public class ShareAdapter extends BaseAdapter implements
		ListScrollStatusListener {

	/**
	 * 网络数据
	 */
	private List<ShareListBean> shares;
	private HashMap<String, UserBean> users;
	private HashMap<String, List<PictureBean>> bufPictures;
	private Set<String> taskQue;

	private int firstItem, itemNum;
	private boolean isNetData;

	private LayoutInflater inflater;
	private Context context;

	private SimpleDateFormat format;

	private ClientBehavior imageRequest = ClientBehavior.getInstance();
	private Handler UIHandler;

	public ShareAdapter(Context context, List<ShareListBean> shares) {
		this.shares = shares;
		this.context = context;
		this.isNetData = true;
		this.bufPictures = MainActivity.bufPictures;

		this.taskQue = new HashSet<String>();
		this.users = MainActivity.bufUsers;

		inflater = LayoutInflater.from(context);
		format = new SimpleDateFormat("yyyy-MM-dd");
		
		MainActivity.bufTask.removeAll(MainActivity.bufTask);
	}

	// 加载的是本地的时候 TODO
	// public ShareAdapter(Context context...)

	// dirPath为本地默认的头像文件夹的保存地址
	public String getPortraitPath(int userId, String dirPath) {
		// TODO 根据userId获取本地头像名
		String portraitPath = dirPath + "/" + "";
		return portraitPath;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.first_page_sharelist,
					parent, false);
			holder = new ViewHolder();

			initHolder(holder, convertView);

			setOnclickListener(holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 数据为空同样不做任何事
		if (shares == null || shares.size() == 0) {
			holder.clearAll();
			return convertView;
		}

		if (isNetData) {
			final ShareBean share = shares.get(position).getShare();
			holder.commentbut.setTag(share); // 很重要，后面设置需要的数据在这里传过去了

			if (share == null || share.getId() == -1) {
				holder.clearAll();
				holder.gridView.setAdapter(null);
				return convertView;
			}

			UIHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					mhandleMessage(msg, position);
				}
			};

			// 重置内容
			holder.clearAll();

			int shareId = share.getId();

			if (shareId != -2) {
				if (!bufPictures.containsKey("shareId" + shareId)) {
//					if (!taskQue.contains("taskqueue" + shareId)) {
						// 开启新任务线程
						initSharePictures(isNetData, share, holder);
//						taskQue.add("taskqueue" + shareId);
//					}
				} else {
					List<PictureBean> picBeanList = bufPictures.get("shareId"
							+ shareId);
					if (picBeanList != null) {
						UserBean user = users
								.get("userId:" + share.getUserId());
						int portrait = -1;
						int[] netData;
						int[] sharepic = share.getPictureIds();
						int size = picBeanList.size();
						if (user != null) {
							portrait = user.getHeadImageId();
							netData = new int[size + 1];
							for (int i = 0; i < size; i++) {
								netData[i] = sharepic[i];
							}
							netData[size] = portrait;
						} else {
							netData = sharepic;
						}
						Message message = Message.obtain();
						message.what = 0x110;
						Bundle bundle = new Bundle();
						bundle.putIntArray("ids", netData);

						message.obj = holder;
						message.arg1 = shareId;
						message.setData(bundle);
						UIHandler.sendMessage(message);
					}
				}

			} else if (shareId == -2) {
				holder.gridView.setAdapter(null);

			}

			// 文字内容加载
			holder.content.setText(share.getContent());
			holder.name.setText(share.getUserName());
			if (share.getPraiseCount() == -1 && share.getCommentCount() == -1) {
				holder.commentbut.setVisibility(View.GONE);
				holder.prasiebut.setVisibility(View.GONE);
			} else {
				holder.prisenum.setText("" + share.getPraiseCount());
				holder.commentnum.setText("" + share.getCommentCount());
			}
			holder.datetime.setText(format.format(share.getCreateTime()));
		}

		// TODO
		/*
		 * ShareBean share = shares.get(position);
		 * 
		 * String path = getPortraitPath(share.getUserId(), ".allPortraits"); if
		 * (path != null) { ImageLoader.getInstance(3,
		 * Type.LIFO).loadImage(path, holder.portrait); } else {
		 * //从网络获取，获取不到显示默认图片 }
		 */

		return convertView;
	}

	/**
	 * 开启线程加载网络图片的数据，如果分享实体share为null将不做任何事
	 * 
	 * @param isNet
	 * @param share
	 * @param holder
	 * @param UIHandler
	 */
	private void initSharePictures(boolean isNet, final ShareBean share,
			final ViewHolder holder) {

//		MainActivity.threadPool.execute(new Runnable() {
		MainActivity.bufTask.add(new Runnable() {
			@Override
			public void run() {

				if (holder == null) {
					return;
				}
				Log.i("加载分享信息，线程任务被添加入线程池", "ShareAdapter------->180");
				UserBean user = null;

				ShareBean tag = (ShareBean) holder.commentbut.getTag();
				if (tag == null || tag != share) {
					return;
				}
				if (users.containsKey("userId:" + share.getUserId())) {
					user = users.get("userId:" + share.getUserId());
				} else {
					user = imageRequest.getUserInfo(share.getUserId());
					users.put("userId:" + share.getUserId(), user);
				}
				holder.portrait.setTag(user); // 重要设置！
				// user为null时图片id为-1用默认图片
				int portraitId;
				if (user != null) {
					portraitId = user.getHeadImageId(); // 头像没设置拿到的也是 -1
				} else {
					portraitId = -1;
				}

				// 取得分享图片的id数组，如果头像id不为-1,创建新数组加入头像id，如果分享图片数组为null，那么只处理头像
				int[] sharePcId = share.getPictureIds();
				int size = 0;
				if (sharePcId != null) {
					size = sharePcId.length;
				}
				int[] pictures;
				if (portraitId != -1) {
					// 头像id加入新图像数组中,总是在最后位置
					pictures = new int[size + 1];
					for (int i = 0; i < size; i++) {
						pictures[i] = sharePcId[i];
					}
					pictures[size] = portraitId;
				} else {
					pictures = sharePcId;
				}
				String json = imageRequest.getPictruesJSON(true, pictures);

				Bundle bundle = new Bundle();
				bundle.putIntArray("picturesId", pictures);
				bundle.putString("json", json);

				Message msg = Message.obtain();
				msg.obj = holder;
				msg.setData(bundle);
				msg.what = 0x111;
				msg.arg1 = share.getId(); // gridViewAdapter需要对应的shareId
				UIHandler.sendMessage(msg);
			}
		});
	}

	/**
	 * UI更新分享图片和头像，接收消息0x111，内部类ViewHolder holder对象，分享id，json数据
	 * 
	 * @param msg
	 * @param postion
	 */
	private void mhandleMessage(Message msg, int postion) {

		Log.i("shareAdapter 接收消息并刷新", "是否已刷新？？？");
		if (msg.what == 0x111) {
			int shareId = msg.arg1;
			ViewHolder holder = (ViewHolder) msg.obj;
			ShareBean share = (ShareBean) holder.commentbut.getTag();
			if (share == null || share.getId() != shareId) {
				return;
			}
			Bundle bundle = msg.getData();
			int[] picturesId;
			String json;
			List<PictureBean> pictureBean;
			if (bundle != null) {
				picturesId = bundle.getIntArray("picturesId");

				json = bundle.getString("json");
				JSONObject jobject = JSON.parseObject(json);
				int status = jobject.getIntValue("status");
				if (status != 0) {
					int errorCode = jobject.getIntValue("errcode");
					String error = ErrorMessage.Handler(errorCode);
					Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
					return;
				}

				String picJson = jobject.getJSONObject("result")
						.getJSONArray("pictures").toJSONString();

				pictureBean = JSON.parseArray(picJson, PictureBean.class);
				bufPictures.put("shareId" + shareId, pictureBean);
//				taskQue.remove("taskqueue" + shareId);
				if (picturesId != null && picturesId.length > 0
						&& pictureBean != null && pictureBean.size() > 0) {
					setImage(holder, shareId, picturesId, pictureBean);
				}
			}
		}
		if (msg.what == 0x110) {
			ViewHolder holder = (ViewHolder) msg.obj;
			int shareId = msg.arg1;
			int[] picturesId = msg.getData().getIntArray("ids");
			List<PictureBean> pictureBean = bufPictures
					.get("shareId" + shareId);
			setImage(holder, shareId, picturesId, pictureBean);
		}

	}

	/**
	 * 设置头像，分享图片 如果netData为null或者netData的尺寸为0将不做任何事
	 * 
	 * @param count
	 * @param holder
	 * @param netData
	 * @param shareId
	 */
	private void setImage(ViewHolder holder, int shareId, int[] netData,
			List<PictureBean> picBean) {

		// 是欢迎内容，设置默认图片内容，或者没有图片数据
		if (shareId == -2 || netData == null || netData.length == 0) {
			return;
		}
		ShareBean share = (ShareBean) holder.commentbut.getTag();
		if (share == null || share.getId() != shareId) {
			return;
		}
		int dataSize = netData.length;

		UserBean user = (UserBean) holder.portrait.getTag();
		int portraitId = netData[dataSize - 1];
		if (user != null && user.getHeadImageId() != -1
				&& portraitId == user.getHeadImageId()) {
			// 必有一张是头像id，所以要减一，得到的刚好是分享图片的数组长度，又是头像id的下标
			// 分享用户的头像设置
			if (picBean.size() == dataSize) { // 默认头像与默认分享图片上不一样的，所有要加判断
				PictureBean portrait = picBean.get(dataSize - 1);
				ImageLoader.getInstance().loadImage(true, null,
						portrait.getBytes(), portraitId, holder.portrait);
			}
			
		}
		dataSize--; // 减去头像的数量得到分享图片数组长度
		// 如果分享图片数组长度小于一就没必要继续了
		if (dataSize < 1) {
			return;
		}

		// 判断是否有分享图片数组
//		ImageGridAdapter gridAdapter = (ImageGridAdapter) holder.gridView
//				.getTag();
//		if ((gridAdapter == null || gridAdapter.getShareId() != shareId)
//				&& share != null && shareId == share.getId()) {
		if(share != null && shareId == share.getId()) {
			int numColumns = 1; // 默认一列
			if ((dataSize % 2 == 0) && (dataSize != 6)) { // 2、4则设为2列
				numColumns = 2;
			} else if (dataSize % 3 == 0 || dataSize == 5) { // 3、5、6则设为3列
				numColumns = 3;
			}
			holder.gridView.setNumColumns(numColumns);
			ImageGridAdapter gridAdapter = new ImageGridAdapter(ShareAdapter.this.context,
					picBean);
//			gridAdapter.setShareId(shareId);
			holder.gridView.setAdapter(gridAdapter);
//			holder.gridView.setTag(gridAdapter);
		}
		else {
			holder.gridView.setAdapter(null);
		}
		holder.gridView.invalidate();
	}

	private class ViewHolder {
		ImageView portrait;
		TextView name, content, prisenum, commentnum, datetime;
		View prasiebut, commentbut, inputCommentView;
		Button cancleInputBut, confirmInputBut;
		EditText edittext;
		ImageGridView gridView;

		/**
		 * 所有控件必须先初始化才能调用此方法,否则会抛异常
		 */
		void clearAll() {
			portrait.setImageResource(R.drawable.ic_launcher);
			name.setText("");
			content.setText("");
			prisenum.setText("");
			commentnum.setText("");
			datetime.setText("");
			gridView.setAdapter(null);
		}
	}

	/**
	 * 初始化控件
	 * 
	 * @param holder
	 * @param convertView
	 */
	private void initHolder(ViewHolder holder, View convertView) {
		holder.portrait = (ImageView) convertView
				.findViewById(R.id.first_page_sharelist_portrait);

		holder.name = (TextView) convertView
				.findViewById(R.id.first_page_sharelist_username);
		holder.content = (TextView) convertView
				.findViewById(R.id.first_page_sharelist_content);
		holder.prisenum = (TextView) convertView
				.findViewById(R.id.first_page_sharelist_text_praise);
		holder.commentnum = (TextView) convertView
				.findViewById(R.id.first_page_sharelist_text_comments);
		holder.datetime = (TextView) convertView
				.findViewById(R.id.first_page_sharelist_text_date);
		holder.commentbut = convertView
				.findViewById(R.id.first_page_sharelist_comment_line);
		holder.prasiebut = convertView
				.findViewById(R.id.first_page_sharelist_praise_line);
		holder.inputCommentView = convertView
				.findViewById(R.id.first_page_share_input_comment_view);
		holder.cancleInputBut = (Button) convertView
				.findViewById(R.id.first_fragment_input_comment_cancel);
		holder.confirmInputBut = (Button) convertView
				.findViewById(R.id.first_fragment_input_comment_confirm);
		holder.edittext = (EditText) convertView
				.findViewById(R.id.first_fragment_input_comment_edittext);
		holder.gridView = (ImageGridView) convertView
				.findViewById(R.id.first_page_sharelist_image_gridview);
	}

	/**
	 * 为控件添加点击事件，还缺点击姓名和点击分享的文字内容事件
	 * 
	 * @param holder
	 */
	private void setOnclickListener(final ViewHolder holder) {
		holder.content.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.first_page_pressdown);
				}

				else {
					v.setBackgroundResource(0);
				}
				return true;
			}
		});
		holder.name.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.first_page_pressdown);
				}

				else {
					v.setBackgroundResource(0);
				}
				return true;
			}
		});
		holder.commentbut.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.first_page_pressdown);

				}

				else {
					v.setBackgroundResource(0);
					if (holder.inputCommentView.getVisibility() == View.GONE) {
						holder.inputCommentView.setVisibility(View.VISIBLE);
						holder.inputCommentView.setFocusable(true);
					} else {
						holder.inputCommentView.setVisibility(View.GONE);
						holder.inputCommentView.setFocusable(false);
					}
				}
				return true;
			}
		});
		holder.prasiebut.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.first_page_pressdown);
				}

				else {
					v.setBackgroundResource(0);
				}
				return true;
			}
		});
		holder.cancleInputBut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				holder.edittext.setText("");
				holder.inputCommentView.setVisibility(View.GONE);
				holder.inputCommentView.setFocusable(false);
			}
		});

		holder.confirmInputBut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * String yourComment = holder.edittext.getText().toString();
				 * if(yourComment != null && yourComment.length() != 0) { //提交评论
				 * }
				 */

				holder.edittext.setText("");
				holder.inputCommentView.setVisibility(View.GONE);
				holder.inputCommentView.setFocusable(false);
			}
		});
	}

	@Override
	public int getCount() {
		if (shares != null)
			return shares.size();
		return -1;
	}

	@Override
	public Object getItem(int position) {
		if (shares != null) {
			return shares.get(position);
		}

		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void listScrollPosition(int start, int end) {
		this.firstItem = start;
		this.itemNum = end;
	}

	private boolean stopAddtask = false;

	@Override
	public void listScrollStatus(int status) {
		// TODO Auto-generated method stub
		if (status == OnScrollListener.SCROLL_STATE_FLING) {
			stopAddtask = true;
			// MainActivity.threadPool.shutdown();
			// try {
			// if(!MainActivity.threadPool.awaitTermination(1000,
			// TimeUnit.MILLISECONDS)) {
			// MainActivity.threadPool.shutdownNow();
			// }
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// Log.i("任务超时，强制关闭!", "内容：" + e);
			// MainActivity.threadPool.shutdownNow();
			// }
			// }
			// else {
			// if(MainActivity.threadPool.isTerminated()) {
			//
			// }
		}
		else {
			stopAddtask = false;
			int taskNum = Math.min(itemNum, MainActivity.bufTask.size());
			Log.i("ShareAdapter 任务数=======", "" + taskNum);
			for(int i = 0; i < taskNum; i++) {
				if(!stopAddtask) {
					MainActivity.ExcuteTask();
				}
				else {
					break;
				}
			}
		}
	}

}
