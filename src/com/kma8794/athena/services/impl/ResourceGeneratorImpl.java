package com.kma8794.athena.services.impl;

import java.util.concurrent.atomic.AtomicInteger;

import com.kma8794.athena.services.ResourceGenerator;

public class ResourceGeneratorImpl implements ResourceGenerator{
	
	public final AtomicInteger value = new AtomicInteger();
	
	@Override
	public int generateUniqueId() {
		return value.incrementAndGet();
	}
}
