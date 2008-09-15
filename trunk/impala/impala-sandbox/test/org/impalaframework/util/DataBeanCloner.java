package org.impalaframework.util;

public class DataBeanCloner {
	public void cloneAndRead(byte[] bytes) {
		DataBean bean = new DataBean();
		bean.setItem("value1");
		SerializationCloner.clone(bean);
		
	}
}
