package net.java.impala.spring.web;

import java.util.Arrays;

import javax.servlet.ServletContext;

import junit.framework.TestCase;
import net.java.impala.spring.resolver.DefaultWebContextResourceHelper;
import net.java.impala.spring.resolver.WebPropertyClassLocationResolver;

import org.easymock.EasyMock;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;

public class DefaultWebApplicationContextLoaderTest extends TestCase {

	public void testCreateWebApplicationContext() {
		WebPropertyClassLocationResolver propertyClassLocationResolver = new WebPropertyClassLocationResolver();
		DefaultWebApplicationContextLoader loader = new DefaultWebApplicationContextLoader(new DefaultWebContextResourceHelper(propertyClassLocationResolver));

		Resource[] resources1 = new Resource[] { new ClassPathResource("loader/context1.xml") };
		Resource[] resources2 = new Resource[] { new ClassPathResource("loader/context2.xml") };
		WebApplicationContext parent = loader.loadWebApplicationContext(null, EasyMock
				.createNiceMock(ServletContext.class), Arrays.asList(resources1), this.getClass().getClassLoader());
		WebApplicationContext child = loader.loadWebApplicationContext(parent, EasyMock
				.createNiceMock(ServletContext.class), Arrays.asList(resources2), this.getClass().getClassLoader());

		assertNotNull(parent.getBean("bean1"));
		assertNotNull(child.getBean("bean1"));
		assertNotNull(child.getBean("bean2"));

		try {
			assertNotNull(parent.getBean("bean2"));
			fail("bean2 not expected in parent context");
		}
		catch (NoSuchBeanDefinitionException e) {
		}
	}

}
