package com.grayash.auditactivity.utils;

import java.util.HashMap;
import java.util.Map;

public class BaseProfile {
	
	private static BaseProfile instance;
	
	private Map<String, Object> data = new HashMap<String, Object>();

	private BaseProfile() {
		
	}
	
	public static BaseProfile getInstance() {
        if (instance == null) {
        	instance = new BaseProfile();
        }
        return instance;
    }
	
	public void setProperty(String key, Object val) {
		data.put(key, val);
	}
	
	public void removeProperty(String key) {
		data.remove(key);
	}
	
	public Object getProperty(String key) {
		return data.get(key);
	}
	
}
