package com.scau.keshe.sharespace.welcome.view;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.bean.UserBean;
import com.scau.keshe.sharespace.requestnet.ClientBehavior;
import com.scau.keshe.sharespace.util.ErrorMessage;
import com.scau.keshe.sharespace.welcome.MainActivity;
import com.scau.keshe.sharespace.welcome.view.MyInputEditText.ButtonFocusableListener;

public class SignUpActivity extends Activity implements ButtonFocusableListener {

	private Button confirm;
	private MyInputEditText name;
	private MyInputEditText password;
	private MyInputEditText passwordconf;

	private Intent siIntent;
	private ProgressDialog dialog;

	private static final Map<String, Class<?>> BEANMAP = new HashMap<String, Class<?>>();

	static {
		BEANMAP.put("user", UserBean.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		initView();
		setEvent();
	}

	private void initView() {
		confirm = (Button) findViewById(R.id.signup_input_confirm_button);
		confirm.setBackgroundResource(R.drawable.before_pressdown);
		name = (MyInputEditText) findViewById(R.id.signup_input_name);
		password = (MyInputEditText) findViewById(R.id.signup_input_password);
		passwordconf = (MyInputEditText) findViewById(R.id.signup_input_passwordconf);

		ActionBar actionbar = this.getActionBar();
		actionbar.setHomeButtonEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setIcon(R.drawable.get_back);
		actionbar.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.pressdown));
		siIntent = getIntent();
	}

	private OnTouchListener clicklistener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (!confirm.isClickable()) {
				return false;
			}

			InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			inputMethod.hideSoftInputFromWindow(confirm.getWindowToken(), 0);

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				confirm.setBackgroundResource(R.drawable.after_pressdown);
			} else {
				confirm.setBackgroundResource(R.drawable.first_page_pressdown);
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				final String username = name.getText().toString();
				final String pwd = password.getText().toString();
				String psdconf = passwordconf.getText().toString();

				if ((username == null || username.length() == 0)
						|| (pwd == null || pwd.length() == 0)
						|| (psdconf == null || psdconf.length() == 0)) {
					Toast.makeText(SignUpActivity.this, "用户名和密码不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (!pwd.equals(psdconf)) {
					Toast.makeText(SignUpActivity.this, "两次输入的密码不相符！",
							Toast.LENGTH_SHORT).show();
				} else if (regexp(username, pwd)) {

					dialog.setCancelable(true);
					dialog = ProgressDialog.show(
							SignUpActivity.this, null, "正在注册");

					MainActivity.threadPool.execute(new Runnable() {

						@Override
						public void run() {

							ClientBehavior signUpRequest = ClientBehavior
									.getInstance();
							if (signUpRequest.regist(username, pwd) == -1) {
								final String error = ErrorMessage
										.Handler(signUpRequest.getErrorCode());
								dialog.dismiss();
								Looper.prepare();
								Handler handler = new Handler();
								handler.post(new Runnable() {
									
									@Override
									public void run() {
										Toast.makeText(SignUpActivity.this, error,
												Toast.LENGTH_SHORT).show();
									}
								});
								Looper.loop();
								Log.i("signup activity error----------->132",
										"注册失败，userId = -1 " + error);
							} else {

								// MainActivity.userId = userId; //返回id，则免登录
								dialog.dismiss();

								siIntent.putExtra("s-name", username);
								siIntent.putExtra("s-psw", pwd);
								setResult(MainActivity.SIGNUP_SUCCESS, siIntent);
								finish();
							}
						}
					});
				}
			}

			return false;
		}
	};
	
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

	private void setEvent() {
		confirm.setClickable(false);
		confirm.setOnTouchListener(clicklistener);
		name.setButtonFCListener(this);
		password.setButtonFCListener(this);
		passwordconf.setButtonFCListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			setResult(MainActivity.SIGNUP_CLOSEALL, getIntent());
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void buttonIsFocusable() {
		if (name != null && password != null && passwordconf != null
				&& name.length() != 0 && password.length() != 0
				&& passwordconf.length() != 0 && !confirm.isClickable()) {
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

	private boolean regexp(String name, String pwd) {
		String RegExp = "[0-9a-zA-Z_]{6,12}";
		boolean checkn = Pattern.matches(RegExp, pwd);
		boolean checkpsw = Pattern.matches(RegExp, name);
		if (!checkn || !checkpsw) {
			Toast.makeText(this, "用户名或密码输入有误！只能是6-12位的英文字符和数字组成",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

}
