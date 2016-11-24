package com.scau.keshe.sharespace.chat.view;

import java.util.ArrayList;
import java.util.List;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.R.id;
import com.scau.keshe.sharespace.R.layout;
import com.scau.keshe.sharespace.R.menu;
import com.scau.keshe.sharespace.chat.model.ChatMsgEntity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends Activity {

	private EditText input;

	private Button confirm;

	private ListView chatUI;

	private ChatAdapter adapter;

	private Handler handler;

	private List<ChatMsgEntity> list;

	// private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO
		/*
		 * Intent intent = this.getIntent(); String name =
		 * intent.getStringExtra("friend"); this.setTitle(name);
		 */
		setContentView(R.layout.activity_chat);
		ActionBar actionbar = this.getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 * 
	 * 调用NavUtils.getParentActivityIntent()方法可以获取到跳转至父Activity的Intent
	 * 如果父Activity和当前Activity是在同一个Task中的，则直接调用navigateUpTo()方法进行跳转
	 * 如果不是在同一个Task中的，则需要借助TaskStackBuilder来创建一个新的Task。
	 */
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this)
						.addNextIntentWithParentStack(upIntent)
						.startActivities();
			}
			else {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {

		input = (EditText) findViewById(R.id.chat_ui_input);

		confirm = (Button) findViewById(R.id.chat_ui_confbut);

		chatUI = (ListView) findViewById(R.id.chat_ui_listView);

		chatUI.setSelected(true);

		list = new ArrayList<ChatMsgEntity>();

		adapter = new ChatAdapter(ChatActivity.this, list);

		chatUI.setAdapter(adapter);

		HandlerMyMessage();

		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String text = input.getText().toString();

				ChatMsgEntity entity = null;

				if (text != null && text.length() != 0) {

					entity = new ChatMsgEntity();

					entity.setText(text);

					entity.setComMsg(false);

					list.add(entity);
					adapter.notifyDataSetChanged();
					chatUI.setSelection(chatUI.getCount() - 1);

					Message message = new Message();
					message.what = 1;

					handler.sendMessageDelayed(message, 3 * 1000);

				} else {
					Log.i("Error------------>", "text = null!");
				}
				input.setText("");
			}
		});
	}

	/**
	 * 处理来自服务器的消息，即好友通过服务器发送过来的信息
	 */
	private void HandlerMyMessage() {
		// TODO
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					ChatMsgEntity entity = new ChatMsgEntity();

					entity.setText("Hello my friend!");

					entity.setComMsg(true);

					list.add(entity);

					adapter.notifyDataSetChanged();

					chatUI.setSelection(chatUI.getCount() - 1);

					break;

				}
			}
		};
	}
}
