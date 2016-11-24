package com.scau.keshe.sharespace.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MProperties extends Properties {
	private static MProperties properties;
	
	public static MProperties getInstance() {
		
		if(properties == null) {
			synchronized (MProperties.class) {
				if(properties == null) {
					properties =  new MProperties();
				}
			}
		}
		return properties;
	}
	
	private MProperties() {
		InputStream in = MProperties.class.getResourceAsStream("/friendInfo.properties");
		try {
			this.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDBName() {
		return properties.getProperty("db_name", "myFriends");
	}
	
	public int getVersion() {
		return Integer.parseInt(properties.getProperty("version", "1"));
	}
	
	public String getBySQLName(String sql) {
		return properties.getProperty(sql);
	}
}
