package org.impalaframework.config;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import junit.framework.TestCase;

public class ManualDynamicPropertiesPropertySourceTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.clearProperty("property.folder");
	}
	
	public void testDynamicProperties() throws Exception {
		PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
		//factory
		factoryBean.setLocation(new ClassPathResource("reload/reloadable.properties"));
		
		DynamicPropertiesPropertySource source = new DynamicPropertiesPropertySource();
		source.setFactoryBean(factoryBean);
		source.setReloadInitialDelay(1);
		source.setReloadInterval(3);
		source.afterPropertiesSet();
		
		while (true) {
			System.out.println(source.getValue("property1"));
			Thread.sleep(1000);
		}
	}

	//this test will dynamically reflect the value of the classpath resource reload/reloadable.properties
	public void testExternalProperties() throws Exception, InterruptedException {
		ExternalDynamicPropertySource source = new ExternalDynamicPropertySource();
		source.setFileName("reload/reloadable.properties");
		source.setReloadInitialDelay(1);
		source.setReloadInterval(3);
		source.afterPropertiesSet();
		
		while (true) {
			System.out.println(source.getValue("property1"));
			Thread.sleep(1000);
		}
	}
	
	//this test will dynamically reflect the value of the file system resource files/reload/reloadable.properties.
	//in other words, it overrides the value in the testExternalProperties() method
	public void testExternalPropertiesWithFileOverride() throws Exception {
		System.setProperty("property.folder", "files");
		testExternalProperties();
	}
	
}
