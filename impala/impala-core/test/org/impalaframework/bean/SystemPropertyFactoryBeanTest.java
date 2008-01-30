package org.impalaframework.bean;

import junit.framework.TestCase;

public class SystemPropertyFactoryBeanTest extends TestCase {

	private SystemPropertyFactoryBean factoryBean;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factoryBean = new SystemPropertyFactoryBean();
		System.clearProperty("myProperty");
	}
	
	public final void testNoSysPropertySet() throws Exception {
		factoryBean.setPropertyName("myProperty");
		
		factoryBean.afterPropertiesSet();
		assertNull(factoryBean.getObject());
		
		factoryBean.setDefaultValue("myValue");
		
		factoryBean.afterPropertiesSet();
		assertEquals("myValue", factoryBean.getObject());
	}
	
	public final void testWithSysPropertySet() throws Exception {
		factoryBean.setPropertyName("myProperty");
		System.setProperty("myProperty", "systemPropertyValue");
		
		factoryBean.afterPropertiesSet();
		assertEquals("systemPropertyValue", factoryBean.getObject());
	}

}
