package com.scau.keshe.sharespace.util;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpClientThread extends Thread {

	private String url;
	private String name;
	private String age;

	private void dohttpClientGet() {
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		
		//请求超时
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5 * 1000);

		try {
			HttpResponse respone = client.execute(httpGet);

			if (respone.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(respone.getEntity());

				System.out.println("content---------->" + content);
				Log.i("content------------->", content);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//数据量较大时
	
	private void dohttpClientPost() {
		HttpPost httpPost = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		
		//请求超时
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5 * 1000);
		
		
		
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("name", name));
		list.add(new BasicNameValuePair("age", age));
		
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(list)) ;
			HttpResponse respone = client.execute(httpPost);
			

			if (respone.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(respone.getEntity());

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public HttpClientThread(String url) {
		this.url = url;
	}

	public HttpClientThread(String url, String name, String age) {
		this.url = url;
		this.age = age;
		this.name = name;
	}
	
	@Override
	public void run() {
		dohttpClientGet();
	}

}
