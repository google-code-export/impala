package net.java.impala.spring.web;

import javax.servlet.ServletContext;

import junit.framework.TestCase;
import net.java.impala.location.PropertyClassLocationResolver;

import org.easymock.EasyMock;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class ParentWebPluginLoaderTest extends TestCase {

	public final void testNewApplicationContext() {
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		PropertyClassLocationResolver resolver = new PropertyClassLocationResolver();
		ParentWebPluginLoader loader = new ParentWebPluginLoader(resolver, servletContext);
		final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		final GenericApplicationContext parent = new GenericApplicationContext();
		final GenericWebApplicationContext applicationContext = loader.newApplicationContext(parent, classLoader);

		assertNotNull(applicationContext);
		assertNotNull(applicationContext.getBeanFactory());
		assertSame(classLoader, applicationContext.getClassLoader());
		assertSame(servletContext, applicationContext.getServletContext());
	}

}
