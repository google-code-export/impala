package org.impalaframework.bean;

import java.util.Properties;

import junit.framework.TestCase;

public class SystemPropertiesFactoryBeanTest extends TestCase {
	
	private SystemPropertiesFactoryBean factoryBean;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factoryBean = new SystemPropertiesFactoryBean();
		factoryBean.setPropertyName("myProperty");
		System.clearProperty("myProperty");
	}
	
	public final void testDefault() throws Exception {
		factoryBean.setDefaultValue("myDefault");
		
		factoryBean.afterPropertiesSet();
		assertEquals("myDefault", factoryBean.getObject());
		
		Properties props = new Properties();
		factoryBean.setProperties(props);
		
		factoryBean.afterPropertiesSet();
		assertEquals("myDefault", factoryBean.getObject());
		
		//now set a valid property value
		props.setProperty("myProperty", "myPropertiesValue");
		factoryBean.afterPropertiesSet();
		assertEquals("myPropertiesValue", factoryBean.getObject());
		
		//now set a system property value
		System.setProperty("myProperty", "systemPropertyValue");
		factoryBean.afterPropertiesSet();
		assertEquals("systemPropertyValue", factoryBean.getObject());
	}

}
