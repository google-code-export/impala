package org.impalaframework.config;

import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.core.io.FileSystemResource;

public class DynamicPropertiesFactoryBeanTest extends TestCase {

	public void testProperties() throws Exception {
		DynamicPropertiesFactoryBean factoryBean = new DynamicPropertiesFactoryBean();
		factoryBean.setLocation(new FileSystemResource("../impala-core/files/reload/reloadable.properties"));
		
		Properties props = (Properties) factoryBean.getObject();
		System.out.println(props);
		
		Long lastModified = factoryBean.getLastModified();
		assertTrue(lastModified > 0L);
		
		factoryBean.getObject();
		System.out.println(props);
		assertEquals(lastModified, factoryBean.getLastModified());
	}
	
	
	public void testManuallyProperties() throws Exception {
		DynamicPropertiesFactoryBean factoryBean = new DynamicPropertiesFactoryBean();
		factoryBean.setLocation(new FileSystemResource("../impala-core/files/reload/reloadable.properties"));
		
		while (true) {
			Properties props = (Properties) factoryBean.getObject();
			System.out.println(props);
			Long lastModified = factoryBean.getLastModified();
			System.out.println(lastModified);
			Thread.sleep(1000);
		}
	}
}
