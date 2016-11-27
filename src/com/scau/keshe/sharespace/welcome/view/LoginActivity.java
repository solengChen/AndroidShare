package com.scau.keshe.sharespace.welcome.view;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.welcome.MainActivity;
import com.scau.keshe.sharespace.welcome.contract.LoginContract;
import com.scau.keshe.sharespace.welcome.contract.LoginContract.*;
import com.scau.keshe.sharespace.welcome.model.LoginModel;
import com.scau.keshe.sharespace.welcome.presenter.LoginPresenterImpl;
import com.scau.keshe.sharespace.welcome.view.MyInputEditText.ButtonFocusableListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements LoginContract.View,
		ButtonFocusableListener {

	private Button confirm;
	private TextView signUp;
	private MyInputEditText name;
	private MyInputEditText password;

	private ImageView showPassword;

	private Model mModel;
	private Presenter mPresenter;

	private ProgressDialog dialog;
	private boolean ispsdShowable = false;

	private Handler ToastHandler;
	private final int PLEASE_LOGIN_FIRST = 0X001, ERROR_EMPTY = 0X002,
			ERROR_CONNET = 0X003, ERROR_APP = 0x004;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.setmActionBar();
		this.initView();
		this.setEvent();

		if (ToastHandler == null) {
			ToastHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case PLEASE_LOGIN_FIRST:
						Toast.makeText(LoginActivity.this, "您还未登录，请先登录！",
								Toast.LENGTH_SHORT).show();
						break;

					case ERROR_EMPTY:
						Toast.makeText(LoginActivity.this, "用户名或密码不能为空！",
								Toast.LENGTH_SHORT).show();
						break;

					case ERROR_CONNET:
						String error = msg.getData().getString("error");
						Toast.makeText(LoginActivity.this, error,
								Toast.LENGTH_SHORT).show();
						break;
					case ERROR_APP:
						Toast.makeText(LoginActivity.this, "应用操作错误，错误编号0X004",
								Toast.LENGTH_SHORT).show();
						break;
					}
				};
			};
		}

	}

	private void setEvent() {
		confirm.setClickable(false);
		name.setButtonFCListener(this);
		password.setButtonFCListener(this);
		confirm.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (!confirm.isClickable()) {
					return false;
				}

				// ==============隐藏输入法=============================
				InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				inputMethod.hideSoftInputFromWindow(confirm.getWindowToken(), 0);

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					confirm.setBackgroundResource(R.drawable.after_pressdown);
				} else {
					confirm.setBackgroundResource(R.drawable.first_page_pressdown);
				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					// 登录
					final String userName = name.getText().toString();
					final String mpassword = password.getText().toString();

					if (userName == null || mpassword == null
							|| userName.length() == 0
							|| mpassword.length() == 0) {
						ToastHandler.sendEmptyMessage(ERROR_EMPTY);
					} else {
						// 发送登录数据
						dialog = ProgressDialog.show(LoginActivity.this, null,
								"正在登录");
						MainActivity.threadPool.execute(new Runnable() {

							@Override
							public void run() {
								mPresenter.loginOrder(userName, mpassword);
							}
						});

					}
				}
				return false;
			}
		});

		signUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 打开注册activity
				Intent intent = LoginActivity.this.getIntent();
				intent.setClass(LoginActivity.this, SignUpActivity.class);
				signUp.setBackgroundResource(R.drawable.before_pressdown);
				startActivityForResult(intent, MainActivity.SIGNUP);
				// finish();
			}
		});

		showPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ispsdShowable) {
					ispsdShowable = true;
					password.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else {
					ispsdShowable = false;
					password.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private void setmActionBar() {
		ActionBar bar = this.getActionBar();
		bar.setHomeButtonEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		// bar.setTitle("登录");
		bar.setDisplayShowTitleEnabled(false);
		bar.setIcon(R.drawable.get_back);
		bar.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.pressdown));
		// bar.setLogo(logo);
	}

	private void initView() {
		confirm = (Button) findViewById(R.id.login_input_confirm_button);
		confirm.setBackgroundResource(R.drawable.before_pressdown);

		signUp = (TextView) findViewById(R.id.login_input_signup_button);
		name = (MyInputEditText) findViewById(R.id.login_input_name);
		password = (MyInputEditText) findViewById(R.id.login_input_password);
		showPassword = (ImageView) findViewById(R.id.login_input_showpsd);

		mModel = new LoginModel();
		mPresenter = new LoginPresenterImpl(this, mModel);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == MainActivity.SIGNUP
				&& resultCode == MainActivity.SIGNUP_SUCCESS) {
			String s_name = data.getStringExtra("s-name");
			String s_psw = data.getStringExtra("s-psw");

			Log.i("LoginActivity onActivityResult-------->191", s_name + " "
					+ s_psw);
			name.setText(s_name);
			password.setText(s_psw);
		} else if (requestCode == MainActivity.SIGNUP
				&& resultCode == MainActivity.SIGNUP_CLOSEALL) {
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.KEYCODE_BACK == keyCode) {
			if(dialog.isShowing()) {
				dialog.dismiss();
			}
		}
		else {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void showFailMsg(String error) {
		if (error != null) {
			Message msg = Message.obtain();
			Bundle errorData = new Bundle();
			errorData.putString("error", error);
			msg.setData(errorData);
			msg.what = ERROR_CONNET;
			ToastHandler.sendMessage(msg);

		} else {
			ToastHandler.sendEmptyMessage(ERROR_APP);
		}

	}

	/**
	 * 保存userId到intent
	 */
	@Override
	public boolean setIdAfterlogin(int userId) {
		Log.i("loginActivity------->", "userid = " + userId);
		if (userId == -1) {
			dialog.dismiss();
			mPresenter.showFailMsg();
			return false;
		}
		// MainActivity.userId = userId;
		Intent intent = getIntent();
		intent.putExtra("L-userId", "" + userId);
		setResult(MainActivity.LOGIN_SUCCESS, intent);
		dialog.dismiss();
		finish();
		return true;
	}

	@Override
	public void buttonIsFocusable() {
		if (name != null && password != null && name.length() != 0
				&& password.length() != 0 && !confirm.isClickable()) {
			confirm.setClickable(true);
			confirm.setBackgroundResource(R.drawable.first_page_pressdown);
		}

	}

	@Override
	public void buttonUnFocusable() {
		if (confirm.isClickable()) {
			confirm.setClickable(false);
			confirm.setBackgroundResource(R.drawable.before_pressdown);
		}
	}
}
