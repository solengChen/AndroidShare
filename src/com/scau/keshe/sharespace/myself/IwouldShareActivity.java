package com.scau.keshe.sharespace.myself;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.R.layout;
import com.scau.keshe.sharespace.R.menu;
import com.scau.keshe.sharespace.dealImage.ImageAdapter;
import com.scau.keshe.sharespace.dealImage.ImageShowActivity;
import com.scau.keshe.sharespace.myself.IshareImageAdapter.openImageShowListener;
import com.scau.keshe.sharespace.requestnet.ClientBehavior;
import com.scau.keshe.sharespace.util.ErrorMessage;
import com.scau.keshe.sharespace.welcome.MainActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class IwouldShareActivity extends Activity implements
		openImageShowListener, View.OnTouchListener {

	private Intent intent;

	private EditText myWords;
	private Button clear;
	private Button confirm;
	private GridView gridView;

	private IshareImageAdapter adapter;
	private int IMAGESHOW = 0x333;
	public static int UPLAODCALL = 0x344;

	private List<String> nullImages;
	private List<String> uploadImages;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iwould_share);

		initView();
		setEvent();
	}

	private void initView() {
		this.setTitle("发布分享");

		intent = new Intent();
		intent.setClass(IwouldShareActivity.this, ImageShowActivity.class);
		nullImages = new ArrayList<String>();

		myWords = (EditText) findViewById(R.id.iwould_share_mywords);
		clear = (Button) findViewById(R.id.iwould_share_clear);
		confirm = (Button) findViewById(R.id.iwould_share_confirm);
		gridView = (GridView) findViewById(R.id.iwould_share_images);

	}

	private void setEvent() {
		nullImages.add("");
		// 初始时images 内容都为空，但必须有一个默认图标
		adapter = new IshareImageAdapter(this, nullImages);
		adapter.setOpenImageShowListener(this);
		gridView.setAdapter(adapter);
		confirm.setOnTouchListener(this);
		clear.setOnTouchListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.iwould_share, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void openImageShowActy() {
		startActivityForResult(intent, IMAGESHOW);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGESHOW && resultCode == UPLAODCALL) {
			uploadImages = ImageAdapter.getSelectedImg();
			adapter.setData(uploadImages);
			adapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			v.setBackgroundResource(R.drawable.after_pressdown);
			return false;
		}
		v.setBackgroundResource(R.drawable.first_page_pressdown);

		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
			case R.id.iwould_share_confirm:
				if ((uploadImages == null || uploadImages.size() == 0)
						&& (myWords == null || myWords.length() == 0)) {
					return false;
				}
				dialog = ProgressDialog.show(IwouldShareActivity.this, null,
						"正在上传分享");
				MainActivity.threadPool.execute(new Runnable() {

					@Override
					public void run() {
						parseImage();
						dialog.dismiss();
						finish();
					}
				});
				break;

			case R.id.iwould_share_clear:
				myWords.setText("");
				adapter.setData(nullImages);
				ImageAdapter.getSelectedImg().clear();
				adapter.notifyDataSetChanged();
				break;
			}
		}

		return false;
	}

	private void parseImage() {

		int len = 0;
		int[] imagesId = null;
		ClientBehavior publishShare = ClientBehavior.getInstance();
		/**
		 * 如果有图片，将图片原文件转为byte[]后上传，并保存返回的imageId
		 */
		if (uploadImages != null && (len = uploadImages.size()) != 0) {

			imagesId = new int[len];
			for (int i = 0; i < len; i++) {
				FileInputStream ipsm;
				try {
					String path = uploadImages.get(i);
//					String stuffix = path.substring(path.lastIndexOf(".") + 1);
					String stuffix = path.substring(path.lastIndexOf("."));
					Log.i("IwouldShareAct------>174", path + " " + stuffix);
					ipsm = new FileInputStream(new File(path));
					BufferedInputStream bf = new BufferedInputStream(ipsm);

					int n = 0;
					byte[] mybyte = new byte[128];
					ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
					while ((n = bf.read(mybyte)) != -1) {

						bytestream.write(mybyte, 0, n);
					}
					byte[] textdata = bytestream.toByteArray();
					Log.i("uploadimage==================", "" + textdata.length);
					imagesId[i] = publishShare.uploadPicture(stuffix, textdata);
					// 图片上传失败，打印错误信息日志
					if (imagesId[i] == -1) {
						String error = ErrorMessage.Handler(publishShare
								.getErrorCode());
						Log.i("IwouldShareActivity-------------->193", error);
					}
					ipsm.close();
					bf.close();
					bytestream.close();

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		/**
		 * 分享内容与图片数组一同上传
		 */
		String words = myWords.getText().toString();
		int shareId = publishShare.publishShare(words, imagesId);
		String error = ErrorMessage.Handler(publishShare.getErrorCode());
		getIntent().putExtra("publishError", error);
		if (shareId == -1) {
			setResult(MainActivity.IWOULDSHARE_FAIL, getIntent());
		} else {
			setResult(MainActivity.IWOULDSHARE_SUCCESS);
		}

	}
}
