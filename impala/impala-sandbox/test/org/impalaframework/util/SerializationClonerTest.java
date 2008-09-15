package org.impalaframework.util;

import junit.framework.TestCase;

public class SerializationClonerTest extends TestCase {

	public void testSerialize() throws Exception {
		DataBean bean = new DataBean();
		DataNestedBean nested = new DataNestedBean();
		nested.setItem("nestedvalue1");
		bean.setItem("value1");
		bean.setNestedBean(nested);
		DataBean dataBean = SerializationCloner.clone(bean);
		assertEquals("value1", dataBean.getItem());
		assertEquals("nestedvalue1", dataBean.getNestedBean().getItem());
	}
	
}
