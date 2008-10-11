package org.impalaframework.util;

import java.io.Serializable;

public class DataNestedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String item;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
}
