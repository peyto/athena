package com.peyto.athena.modules.protocol;

public class UpdateEvent {
	final String type;
	final Object event;

	public UpdateEvent(String type, Object event) {
		this.type = type;
		this.event = event;
	}
	
	
}
