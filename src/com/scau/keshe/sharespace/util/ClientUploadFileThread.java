package com.scau.keshe.sharespace.util;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Environment;

public class ClientUploadFileThread extends Thread {

	private String url;

	public ClientUploadFileThread() {

	}

	public void upLoad() {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		File parent = Environment.getExternalStorageDirectory();

		// 用户帐户加系统时间

		String fileName = "sky.jpg";
		File filAbs = new File(parent, fileName);

		FileBody fileBody = new FileBody(filAbs);

		MultipartEntity muti = new MultipartEntity();
		muti.addPart("file", fileBody);

		post.setEntity(muti);

		try {
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				System.out.println(EntityUtils.toString(response.getEntity()));
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.upLoad();
	}

}
