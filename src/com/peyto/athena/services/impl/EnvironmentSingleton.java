package com.peyto.athena.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.peyto.athena.services.Environment;

public class EnvironmentSingleton implements Environment{
	
	public static final Environment env = new EnvironmentSingleton();
	@SuppressWarnings("rawtypes")
	private Map<Class, Object> map = new HashMap<Class, Object>(); 
	private Map<Integer, Object> objects = new HashMap<Integer, Object>();
	
	public static Environment getEnvironment() {
		return env;
	}
	
	@Override
	public <T> void addService(Class<T> classObj, T service) {
		map.put(classObj, service);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getService(Class<T> classObj) {
		return (T) map.get(classObj); 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getUniqueObject(int id) {
		return (T) objects.get(id);
	}
	
	@Override
	public <T> void storeUniqueObject(int id, T object) {
		objects.put(id, object);
	}
}
