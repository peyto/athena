package com.kma8794.athena.services;

public interface Environment {
	
	public<T> T getService(Class<T> classObj);
	
	public<T> void addService(Class<T> classObj, T service);
	
	public <T> T getUniqueObject(int id);
	
	public <T> void storeUniqueObject(int id, T object);
}
