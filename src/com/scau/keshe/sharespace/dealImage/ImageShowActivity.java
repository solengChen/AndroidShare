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

				// �����ݵ�View��
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
			Toast.makeText(this, "δɨ�赽�κ�ͼƬ", Toast.LENGTH_LONG).show();
			return;
		}
		// currentDir.list()���ص���һ������
		// �����ͼƬ��Ϊimgs��ֵ,�������ڲ���ͼƬ���͵Ķ��ᱻ����

		imgs = Arrays.asList(currentDir.list(filter));

		if (imgs == null) {
			System.out.println("imgs = null !");
		}

		imgAdapter = new ImageAdapter(ImageShowActivity.this, imgs,
				currentDir.getAbsolutePath());
		gridView.setAdapter(imgAdapter);

		dirCount.setText(maxCount + "��");
		dirName.setText(currentDir.getName());
	}

	/**
	 * ���ͼƬ���� ����ContentProviderɨ���ֻ��е�ͼƬ
	 */
	private void initDatas() {

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "��ǰ�洢��������", Toast.LENGTH_SHORT).show();
			return;
		}
		progressDialog = ProgressDialog.show(this, null, "���ڼ���");

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

				// ����ɨ�����·������ֹ�ظ�����·��
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

					// ����ļ�����ͼƬ����
					int picSize = parentFile.list(filter).length;

					folderBean.setCount(picSize);
					folderBeans.add(folderBean);

					// ��õ�ǰͼƬ�����ļ������ƺ�ͼƬ����
					if (picSize > maxCount) {
						maxCount = picSize;
						currentDir = parentFile;
					}
				}

				cursor.close();

				// ֪ͨhandlerɨ��ͼƬ���
				handler.sendEmptyMessage(DATA_LOADED);

			}
		});
	}

	/**
	 * ��Ӽ�����
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
	 * ��������������ı�͸����
	 */
	protected void lightOn() {

		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.alpha = 1.0f;
		this.getWindow().setAttributes(lp);
	}

	/**
	 * ��������䰵
	 */
	protected void lightOff() {

		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.alpha = 0.3f;
		this.getWindow().setAttributes(lp);

	}

	/**
	 * ��ʼ���ؼ�
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
	 * ��Activity�״�����ʱ��ϵͳ�����onCreateOptionsMenu()�������� ��Activity��װAction bar�������˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menuInflater = this.getMenuInflater();
		menuInflater.inflate(R.menu.image_show, menu);
		return true;
	}

	/**
	 * ����actionBar��ť�Ĵ����¼�
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// ���������ķ��ؼ�ͷ

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
	 * overflow�е�Action��ťӦ��Ӧ����ʾͼ�꣬����MenuBuilder������
	 * setOptionalIconsVisible������������ ���������overflow��չ����ʱ��������������true����ô�����ÿһ��
	 * Action��ť��Ӧ��ͼ��Ͷ�����ʾ�����ˡ����õķ�����Ȼ��Ȼ���÷�����
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
	 * overflow��ť����ʾ������ֻ���Ӳ��������й�ϵ�ģ�����ֻ�û������Menu���Ļ���
	 * overflow��ť�Ϳ�����ʾ�����������Menu���Ļ���overflow��ť�Ͳ�����ʾ����
	 * ��ViewConfiguration���������һ������sHasPermanentMenuKey�ľ�̬����
	 * ϵͳ���Ǹ������������ֵ���ж��ֻ���û������Menu���� ͨ������ķ�ʽ�޸�����ֵ��������ԶΪfalse
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
