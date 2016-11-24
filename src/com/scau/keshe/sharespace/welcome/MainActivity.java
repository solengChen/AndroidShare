package com.scau.keshe.sharespace.welcome;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.bean.ShareListBean;
import com.scau.keshe.sharespace.bean.UserBean;
import com.scau.keshe.sharespace.connector.view.ContactsFragment;
import com.scau.keshe.sharespace.dealImage.ImageShowActivity;
import com.scau.keshe.sharespace.firstpage.view.FirstPageFragment;
import com.scau.keshe.sharespace.fragment.ChangeFragment;
import com.scau.keshe.sharespace.fragment.FragmentAdapter;
import com.scau.keshe.sharespace.myself.IwouldShareActivity;
import com.scau.keshe.sharespace.myself.MyselfFragment;
import com.scau.keshe.sharespace.requestnet.ClientBehavior;
import com.scau.keshe.sharespace.util.ClientStartException;
import com.scau.keshe.sharespace.welcome.contract.LoginContract;
import com.scau.keshe.sharespace.welcome.model.LoadUserBeanModelImpl;
import com.scau.keshe.sharespace.welcome.presenter.LoadUserBeanPresenterImpl;
import com.scau.keshe.sharespace.welcome.view.LoginActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends FragmentActivity implements OnClickListener,
		LoginContract.LoadUserView {
	private View but1, but2, but3, but4, viewLine;
	private LayoutParams lyparams; // 滚动条的布局格式
	private int lineWidth;

	private ChangeFragment chgfg2;

	private FirstPageFragment chgfg1;
	private ContactsFragment chgfg3;
	private MyselfFragment chgfg4;
	// private EditText name, age;

	private ViewPager viewpager;

	/**
	 * 更新界面主线程
	 */
	private Handler UIhandler;

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentAdapter fadapter;

	private ClientBehavior mClientBehavior;

	public static Intent mainIntent = new Intent();

	public static List<ShareListBean> shareData = new ArrayList<ShareListBean>();
	/**
	 * MVP
	 */
	private LoginContract.LoadUserPresenter loadPresent;
	private LoginContract.LoadModel loadModel;
	public static int userId = -1;
	private UserBean mUser;

	public static String USERIDSTRING = "UserId";
	/**
	 * 线程池，只有一个
	 */
	public static ExecutorService threadPool = new ThreadPoolExecutor(2, 5, 30,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	/**
	 * 加载对话框
	 */
	private ProgressDialog dialog;

	/**
	 * main activity直接跳转码
	 * 
	 * @author ShouLun
	 * 
	 */
	public static final int MAIN = 0x101, LOGIN = 0x201, SIGNUP = 0x204,
			IWOULDSHARE = 0x303, ADDFRIENDS = 0x404, CHECKTOP = 0x510,
			MYINFO = 0x550, TALKING = 0x640, FRIENDINFO = 0x505;

	/**
	 * main activity 间接跳转码
	 */
	public static final int JUMP_FROM_IWOULD_SHARE = 0x001,
			JUMP_FROM_MYSELF_PART = 0x002, JUMP_FROM_ADDFRIEND = 0x003;

	public static final int SIGNUP_FAIL = 0X205, SIGNUP_SUCCESS = 0X206, SIGNUP_CLOSEALL = 0x210;
	public static final int LOGIN_SUCCESS = 0x202, LOGIN_FAIL = 0X203;
	/**
	 * 发布分享结果码
	 */
	public static final int IWOULDSHARE_FAIL = 0x301,
			IWOULDSHARE_SUCCESS = 0x302;

	/**
	 * UI更新指令
	 */
	private final int FLASH_FIRSTPAGE = 0, FLASH_TALKING = 1,
			FLASH_CONTRACTS = 2, FLASH_MYINFO = 3, FLASH_ALL = 4;
	private int mRequestCode = MAIN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle("首页");
		this.setOverflowShowingAwarys();
		initView();
		setmActionBar();
	}

	private void setmActionBar() {
		ActionBar bar = this.getActionBar();
		bar.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.pressdown));
		// bar.setLogo(logo);
	}

	/**
	 * 初始化控件，并添加监听事件
	 */
	private void initView() {

		but1 = (View) findViewById(R.id.bottom_bar_tab1);
		but2 = (View) findViewById(R.id.bottom_bar_tab2);
		but3 = (View) findViewById(R.id.bottom_bar_tab3);
		but4 = (View) findViewById(R.id.bottom_bar_tab4);

		viewLine = (View) findViewById(R.id.bottom_bar_viewline);

		but1.setAlpha(1.0f);
		but1.setOnClickListener(this);
		but2.setOnClickListener(this);
		but3.setOnClickListener(this);
		but4.setOnClickListener(this);

		viewpager = (ViewPager) findViewById(R.id.fragment_viewpager);

		// FragmentManager fm = this.getFragmentManager();
		// FragmentTransaction ft = fm.beginTransaction();

		chgfg1 = new FirstPageFragment();
		chgfg1.setContext(this);
		this.setFirstpageListener(chgfg1);

		chgfg2 = new ChangeFragment(R.layout.fragment_talking);

		chgfg3 = new ContactsFragment();
		chgfg3.setContext(this);
		this.setContractsListener(chgfg3);

		chgfg4 = new MyselfFragment();
		chgfg4.setContext(this);
		this.setMyselfListener(chgfg4);

		fragments.add(chgfg1);
		fragments.add(chgfg2);
		fragments.add(chgfg3);
		fragments.add(chgfg4);

		fadapter = new FragmentAdapter(this.getSupportFragmentManager(),
				fragments);

		viewpager.setAdapter(fadapter);

		setFragmentAdapter();

		mClientBehavior = ClientBehavior.getInstance();
		try {
			mClientBehavior.startClient();
		} catch (ClientStartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.initHandler();
		loadModel = new LoadUserBeanModelImpl();
		loadPresent = new LoadUserBeanPresenterImpl(this, loadModel);

		Display defaultDisplay = this.getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		defaultDisplay.getMetrics(dm);

		lineWidth = dm.widthPixels / 4;
		lyparams = (LayoutParams) viewLine.getLayoutParams();
		lyparams.width = lineWidth;
		viewLine.setLayoutParams(lyparams);
	}

	/**
	 * fragment动画效果
	 */
	private void setFragmentAdapter() {
		viewpager.setCurrentItem(0);

		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				changeButtonAlpha(position);

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
					int offsetPixels) {
				if (offset != 0 && offsetPixels != 0) {
					lyparams.leftMargin = offsetPixels / 4 + lineWidth
							* position;
					viewLine.setLayoutParams(lyparams);
				}
			}

			@Override
			public void onPageScrollStateChanged(int position) {

			}
		});
	}

	/**
	 * 加载actionBar的布局文件
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	/**
	 * 设置点击actionBar触发器
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_iwould_share:
			if (userId == -1) {
				mainIntent.setClass(MainActivity.this, LoginActivity.class);
				mainIntent.putExtra("jump-from", MainActivity.JUMP_FROM_IWOULD_SHARE);
				startActivityForResult(mainIntent, LOGIN);
			} else {
				mainIntent.setClass(MainActivity.this, IwouldShareActivity.class);
				startActivityForResult(mainIntent, IWOULDSHARE);
			}

			return true;

		}
		// TODO 完成 添加好友 查看榜单 和 我要分享
		return super.onOptionsItemSelected(item);
	}

	//
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == IWOULDSHARE) {
			if (resultCode == IWOULDSHARE_FAIL) {
				Toast.makeText(this, intent.getStringExtra("publishError"),
						Toast.LENGTH_SHORT).show();
			} else if (resultCode == IWOULDSHARE_SUCCESS) {

			}
		}

		if (requestCode == LOGIN && resultCode == LOGIN_SUCCESS) {
			userId = Integer.parseInt(intent.getStringExtra("L-userId"));
			int jumpOrder = intent.getIntExtra("jump-from", -1);
			if(jumpOrder == JUMP_FROM_IWOULD_SHARE) {
				intent.removeExtra("jump-from");
				intent.setClass(MainActivity.this, IwouldShareActivity.class);
				startActivityForResult(intent, IWOULDSHARE);
			}
			else {
				intent.removeExtra("jump-from");
				
			}
			//TODO other jump
		}
		Log.i("MainActivity onActivityResult----------->320", LOGIN + " " + LOGIN_SUCCESS + " ? " + requestCode + " " + resultCode);
		super.onActivityResult(requestCode, resultCode, intent);
	}

	/**
	 * overflow中的Action按钮应不应该显示图标，是由MenuBuilder这个类的
	 * setOptionalIconsVisible方法来决定的 如果我们在overflow被展开的时候给这个方法传入true，那么里面的每一个
	 * Action按钮对应的图标就都会显示出来了。调用的方法当然仍然是用反射了
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
	}

	/**
	 * overflow按钮的显示情况和手机的硬件情况是有关系的，如果手机没有物理Menu键的话，
	 * overflow按钮就可以显示，如果有物理Menu键的话，overflow按钮就不会显示出来
	 * 在ViewConfiguration这个类中有一个叫做sHasPermanentMenuKey的静态变量
	 * 系统就是根据这个变量的值来判断手机有没有物理Menu键的 通过反射的方式修改它的 值，让它永远为false
	 */
	private void setOverflowShowingAwarys() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHandler() {
		UIhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				// 更新user信息
				switch (msg.what) {
				case FLASH_FIRSTPAGE:
					firstpageListener.mainRequestReflash();
					break;

				case FLASH_TALKING:
					break;

				case FLASH_CONTRACTS:
					contractsListener.mainRequestReflash();
					break;

				case FLASH_MYINFO:
					myselfListener.mainRequestReflash();
					break;
				case FLASH_ALL:
					firstpageListener.mainRequestReflash();
					myselfListener.mainRequestReflash();
					contractsListener.mainRequestReflash();
					break;

				}

			}
		};
	}

	/**
	 * 
	 */
	@Override
	protected void onResume() {
		Log.i("mainActivity onresume-------------", "");
//		UIhandler.sendEmptyMessage(FLASH_FIRSTPAGE);
		if (userId != -1 && mUser != null) {
			MainActivity.threadPool.execute(new Runnable() {

				@Override
				public void run() {
					// 获取用户自己的信息
					loadPresent.loadUser(userId);
				}
			});
		}
		super.onResume();
	}

	@Override
	public void setUserBean(UserBean user) {
		if (user != null) {
			mUser = user;
			System.out.println("after login----------> userid = "
					+ user.getUserId());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (dialog != null) {
			dialog.dismiss();
		} else if (keyCode == event.KEYCODE_BACK) {
			threadPool.shutdown();
			userId = -1;
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * =======================================================
	 * 接口回调设置，登录或是注册都会导致数据发生改变 ，需要在UI线程里调用方法实现fragment内容的刷新
	 * =======================================================
	 */

	/**
	 * fragment接口
	 */
	private FragmentListener firstpageListener;
	private FragmentListener contractsListener;
	private FragmentListener myselfListener;

	void setFirstpageListener(FragmentListener listener) {
		this.firstpageListener = listener;
	}
	
	

	public void setContractsListener(FragmentListener contractsListener) {
		this.contractsListener = contractsListener;
	}

	public void setMyselfListener(FragmentListener myselfListener) {
		this.myselfListener = myselfListener;
	}



	public interface FragmentListener {
		void mainRequestReflash();
	}
	
	//===========================5毛钱特效=======================
	/**
	 * 实现点击切换fragment的效果
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bottom_bar_tab1:

			viewpager.setCurrentItem(0);

			break;

		case R.id.bottom_bar_tab2:

			viewpager.setCurrentItem(1);

			break;

		case R.id.bottom_bar_tab3:

			viewpager.setCurrentItem(2);

			break;

		case R.id.bottom_bar_tab4:

			viewpager.setCurrentItem(3);

			break;
		}
	}

	/**
	 * 改变底部按钮的透明度
	 * 
	 * @param position
	 */
	private void changeButtonAlpha(int position) {
		switch (position) {
		case 0:
			but1.setAlpha(1.0f);
			but2.setAlpha(0.5f);
			but3.setAlpha(0.5f);
			but4.setAlpha(0.5f);
			setTitle("首页");
			break;
		case 1:
			but2.setAlpha(1.0f);
			but1.setAlpha(0.5f);
			but3.setAlpha(0.5f);
			but4.setAlpha(0.5f);
			setTitle("巨信");
			break;
		case 2:
			but3.setAlpha(1.0f);
			but2.setAlpha(0.5f);
			but1.setAlpha(0.5f);
			but4.setAlpha(0.5f);
			setTitle("联系人");
			break;
		case 3:
			but4.setAlpha(1.0f);
			but2.setAlpha(0.5f);
			but1.setAlpha(0.5f);
			but3.setAlpha(0.5f);
			setTitle("我的");
			break;
		}
	}

}
