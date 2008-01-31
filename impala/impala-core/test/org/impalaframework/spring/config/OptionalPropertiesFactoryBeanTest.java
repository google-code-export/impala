package org.impalaframework.spring.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import junit.framework.TestCase;

public class OptionalPropertiesFactoryBeanTest extends TestCase {

	public final void testSetLocationResource() throws IOException {
		OptionalPropertiesFactoryBean factoryBean = new OptionalPropertiesFactoryBean();
		
		Resource[] resources = new Resource[2];
		resources[0] = new ClassPathResource("aresourcewhichdoesnotexist");
		factoryBean.afterPropertiesSet();
		
		Properties properties = (Properties) factoryBean.getObject();
		assertEquals(0, properties.size());
	}

}
