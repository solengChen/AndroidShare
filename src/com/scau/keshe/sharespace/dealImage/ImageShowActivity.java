package com.scau.keshe.sharespace.dealImage;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.R.id;
import com.scau.keshe.sharespace.R.layout;
import com.scau.keshe.sharespace.R.menu;
import com.scau.keshe.sharespace.R.style;
import com.scau.keshe.sharespace.bean.FolderBean;
import com.scau.keshe.sharespace.myself.IwouldShareActivity;
import com.scau.keshe.sharespace.welcome.MainActivity;

public class ImageShowActivity extends Activity {

	private GridView gridView;
	private ImageAdapter imgAdapter;
	private List<String> imgs;

	private RelativeLayout bottomLy;

	private TextView dirName, dirCount;

	private File currentDir;
	private FilenameFilter filter;
	private int maxCount = 0;

	private List<FolderBean> folderBeans = new ArrayList<FolderBean>();

	private ProgressDialog progressDialog;

	private static final int DATA_LOADED = 0X110;

	private ImageDirPopWindow popupWindow;

	private MenuInflater menuInflater;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case DATA_LOADED:
				progressDialog.dismiss();

				// 绑定数据到View中
				data2View();

				initPopupWindow();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_show_select);
		this.setTitle("");

		this.setOverflowShowingAwarys();

		initView();
		initDatas();
		initEvent();
	}

	protected void initPopupWindow() {
		popupWindow = new ImageDirPopWindow(ImageShowActivity.this, folderBeans);

		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {

				lightOn();
			}
		});

		popupWindow
				.setOnDirSelectedListener(new ImageDirPopWindow.OnDirSelectedListener() {

					@Override
					public void onSelected(FolderBean folderBean) {

						currentDir = new File(folderBean.getDir());

						imgs = Arrays.asList(currentDir.list(filter));
						imgAdapter = new ImageAdapter(ImageShowActivity.this,
								imgs, currentDir.getAbsolutePath());

						gridView.setAdapter(imgAdapter);

						dirCount.setText(imgs.size() + "");
						dirName.setText(folderBean.getName());

						popupWindow.dismiss();
					}
				});

	}

	protected void data2View() {

		if (currentDir == null) {
			Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_LONG).show();
			return;
		}
		// currentDir.list()返回的是一个数组
		// 如果有图片，为imgs赋值,但可能内部非图片类型的都会被加入

		imgs = Arrays.asList(currentDir.list(filter));

		if (imgs == null) {
			System.out.println("imgs = null !");
		}

		imgAdapter = new ImageAdapter(ImageShowActivity.this, imgs,
				currentDir.getAbsolutePath());
		gridView.setAdapter(imgAdapter);

		dirCount.setText(maxCount + "张");
		dirName.setText(currentDir.getName());
	}

	/**
	 * 添加图片数据 利用ContentProvider扫描手机中的图片
	 */
	private void initDatas() {

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "当前存储卡不可用", Toast.LENGTH_SHORT).show();
			return;
		}
		progressDialog = ProgressDialog.show(this, null, "正在加载");

		filter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")
						|| filename.endsWith(".png")) {
					return true;

				}
				return false;
			}
		};

		MainActivity.threadPool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

				ContentResolver contentResolver = ImageShowActivity.this
						.getContentResolver();
				Cursor cursor = contentResolver.query(uri, null,
						MediaStore.Images.Media.MIME_TYPE + "= ? or "
								+ MediaStore.Images.Media.MIME_TYPE + " = ?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				// 保存扫描过的路径，防止重复遍历路径
				Set<String> dirPaths = new HashSet<String>();

				while (cursor.moveToNext()) {

					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					File parentFile = new File(path).getParentFile();
					if (parentFile == null) {
						continue;
					}

					String dirPath = parentFile.getAbsolutePath();

					FolderBean folderBean = null;

					if (dirPaths.contains(dirPath)) {
						continue;
					} else {
						dirPaths.add(dirPath);
						folderBean = new FolderBean();
						folderBean.setDir(dirPath);
						folderBean.setFirstImgPath(path);

					}

					if (parentFile.list() == null) {
						continue;
					}

					// 获得文件夹下图片数量
					int picSize = parentFile.list(filter).length;

					folderBean.setCount(picSize);
					folderBeans.add(folderBean);

					// 获得当前图片最多的文件夹名称和图片数量
					if (picSize > maxCount) {
						maxCount = picSize;
						currentDir = parentFile;
					}
				}

				cursor.close();

				// 通知handler扫描图片完成
				handler.sendEmptyMessage(DATA_LOADED);

			}
		});
	}

	/**
	 * 添加监听器
	 */
	private void initEvent() {
		bottomLy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				popupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
				popupWindow.showAsDropDown(bottomLy, 0, 0);

				lightOff();
			}
		});
	}

	/**
	 * 内容区域变亮，改变透明度
	 */
	protected void lightOn() {

		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.alpha = 1.0f;
		this.getWindow().setAttributes(lp);
	}

	/**
	 * 内容区域变暗
	 */
	protected void lightOff() {

		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.alpha = 0.3f;
		this.getWindow().setAttributes(lp);

	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		gridView = (GridView) findViewById(R.id.id_gridView);
		bottomLy = (RelativeLayout) findViewById(R.id.id_button_ly);
		dirName = (TextView) findViewById(R.id.id_dir_name);
		dirCount = (TextView) findViewById(R.id.id_dir_count);

		getActionBar().setBackgroundDrawable(
				this.getResources().getDrawable(R.drawable.pressdown));
	}

	/**
	 * 当Activity首次启动时，系统会调用onCreateOptionsMenu()方法给你 的Activity组装Action bar和悬浮菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menuInflater = this.getMenuInflater();
		menuInflater.inflate(R.menu.image_show, menu);
		return true;
	}

	/**
	 * 处理actionBar按钮的触发事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// 操作栏左侧的返回箭头

		case android.R.id.home:
			imgAdapter.getSelectedImg().clear();
			finish();
			return true;

		case R.id.action_upload:
			Log.i("ImageShowActivity onOptionItemSelected----------->325",
					"pictures are uploading");
			setResult(IwouldShareActivity.UPLAODCALL, getIntent());
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
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
	 * 系统就是根据这个变量的值来判断手机有没有物理Menu键的 通过反射的方式修改它的值，让它永远为false
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
}
