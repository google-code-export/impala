package org.impalaframework.config;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import junit.framework.TestCase;

public class ManualDynamicPropertiesPropertySourceTest extends TestCase {

	public void testProperties() throws Exception {
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
	
}
