package org.impalaframework.osgimodule1;

import org.impalaframework.osgiroot.MessageService;

public class MessageServiceImpl implements MessageService {

	private String value;
	
	public String getMessage() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
