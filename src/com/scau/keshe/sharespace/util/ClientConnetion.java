package com.scau.keshe.sharespace.util;

import android.util.Log;


public class ClientConnetion {

	
	
	public ClientConnetion(String name, String age) {
		String url = "http://192.168.110.1:8080/web/MyServlet";
		
		url = url + "?name=" + name + "&age=" + age;
		System.out.println(url);
		Log.i("url====================", url);
		new HttpClientThread(url).start();
	}
}
