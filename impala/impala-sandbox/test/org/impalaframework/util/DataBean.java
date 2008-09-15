package org.impalaframework.util;

import java.io.Serializable;

public class DataBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String item;

	private DataNestedBean nestedBean;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public DataNestedBean getNestedBean() {
		return nestedBean;
	}

	public void setNestedBean(DataNestedBean nestedBean) {
		this.nestedBean = nestedBean;
	}

}
