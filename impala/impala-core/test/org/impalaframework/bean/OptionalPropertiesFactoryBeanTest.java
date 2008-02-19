package org.impalaframework.bean;

import java.io.IOException;
import java.util.Properties;

import org.impalaframework.bean.OptionalPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import junit.framework.TestCase;

public class OptionalPropertiesFactoryBeanTest extends TestCase {

	private OptionalPropertiesFactoryBean factoryBean;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factoryBean = new OptionalPropertiesFactoryBean();
	}
	
	public final void testNonexistOnly() throws IOException {
		Resource[] resources = new Resource[1];
		resources[0] = new ClassPathResource("aresourcewhichdoesnotexist");
		factoryBean.setLocations(resources);
		factoryBean.afterPropertiesSet();
		
		Properties properties = (Properties) factoryBean.getObject();
		assertEquals(0, properties.size());
	}
	
	public final void testSingleWithExisting() throws IOException {
		Resource resource = new ClassPathResource("beanset.properties");
		factoryBean.setLocation(resource);
		factoryBean.afterPropertiesSet();
		
		Properties properties = (Properties) factoryBean.getObject();
		assertEquals(3, properties.size());
		assertEquals("applicationContext-set1.xml", properties.get("set1"));
	}
	
	public final void testWithExisting() throws IOException {
		Resource[] resources = new Resource[2];
		resources[0] = new ClassPathResource("aresourcewhichdoesnotexist");
		resources[1] = new ClassPathResource("beanset.properties");
		factoryBean.setLocations(resources);
		factoryBean.afterPropertiesSet();
		
		Properties properties = (Properties) factoryBean.getObject();
		assertEquals(3, properties.size());
		assertEquals("applicationContext-set1.xml", properties.get("set1"));
	}

}
