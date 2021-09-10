package org.tain.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ClsProp {

	private Properties prop = null;
	
	private ClsProp() throws Exception {
		String configFile = System.getProperty("config.file", "/Users/kang-air/KANG/cls/ClsServer.properties");
		
		System.out.println(">>>>> configFile: " + configFile);
		
		this.prop = new Properties();
		this.prop.load(new FileInputStream(configFile));
	}
	
	private static ClsProp instance = null;
	public static ClsProp getInstance() throws Exception {
		if (instance == null) {
			instance = new ClsProp();
		}
		return instance;
	}
	
	public String get(String key) {
		return this.prop.getProperty(key);
	}
	
	public void printAll() {
		this.prop.list(System.out);
	}
}
