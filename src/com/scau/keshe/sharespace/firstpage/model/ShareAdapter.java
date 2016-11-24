package com.scau.keshe.sharespace.firstpage.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.bean.PictureBean;
import com.scau.keshe.sharespace.bean.ShareBean;
import com.scau.keshe.sharespace.bean.ShareListBean;
import com.scau.keshe.sharespace.bean.UserBean;
import com.scau.keshe.sharespace.database.BaseDao;
import com.scau.keshe.sharespace.dealImage.ImageLoader;
import com.scau.keshe.sharespace.firstpage.view.ImageGridView;
import com.scau.keshe.sharespace.requestnet.ClientBehavior;
import com.scau.keshe.sharespace.welcome.MainActivity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareAdapter extends BaseAdapter {

	/**
	 * 网络数据
	 */
	private List<ShareListBean> shares;
	private boolean isNetData;
	private int portraitId;
	
	private List<PictureBean> netData;

	private LayoutInflater inflater;
	private Context context;
	private ViewHolder holder;

	private SimpleDateFormat format;
	private Handler UIHandler;

	private ClientBehavior imageRequest = ClientBehavior.getInstance();

	public ShareAdapter(Context context, List<ShareListBean> shares) {
		this.shares = shares;
		this.context = context;
		this.isNetData = true;
		inflater = LayoutInflater.from(context);
		format = new SimpleDateFormat("yyyy-MM-dd");
		
		UIHandler  = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0x111 && netData != null && netData.size() != 0) {

					ImageLoader.getInstance().loadImage(
							true,
							null,
							netData.get(netData.size() - 1).getBytes(), portraitId, holder.portrait);

					final ImageGridAdapter gridAdapter = new ImageGridAdapter(
							ShareAdapter.this.context, netData);
					holder.gridView.setAdapter(gridAdapter);
				}
			};
		};
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

		if (shares == null || shares.size() == 0) {
			return null;
		}
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.first_page_sharelist_items,
					parent, false);
			holder = new ViewHolder();

			initHolder(holder, convertView);

			setOnclickListener(holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (isNetData) {
			final ShareBean share = shares.get(position).getShare();
			
			initSharePictures(isNetData, share, holder);
			holder.content.setText(share.getContent());
			holder.name.setText(share.getUserName());
			holder.prisenum.setText("" + share.getPraiseCount());
			holder.commentnum.setText("" + share.getCommentCount());
			holder.datetime.setText(format.format(new Date(share
					.getCreateTime())));
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

	private void initSharePictures(boolean isNet, final ShareBean share,
			final ViewHolder holder) {
		
		MainActivity.threadPool.execute(new Runnable() {

			@Override
			public void run() {

				if (share == null)
					return;

				UserBean user = imageRequest.getUserInfo(share.getUserId());
				portraitId = -1;
				// TODO 需要给默认图片id -1，然后检查时发现图片id为-1就用默认图片
				if (user != null) {
					portraitId = user.getHeadImageId();
				}

				int[] sharePcId = share.getPictureIds();
				int size = 0;
				if (sharePcId != null) {
					size = sharePcId.length;
				}

				int[] pictures = new int[size + 1];
				for (int i = 0; i < size; i++) {
					pictures[i] = sharePcId[i];
				}
				pictures[size] = portraitId;

				netData = imageRequest.getPictures(true,
						pictures);

				UIHandler.sendEmptyMessage(0x111);
			}
		});
	}

	private class ViewHolder {
		ImageView portrait;
		TextView name, content, prisenum, commentnum, datetime;
		View prasiebut, commentbut, inputCommentView;
		Button cancleInputBut, confirmInputBut;
		EditText edittext;
		ImageGridView gridView;
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
}
