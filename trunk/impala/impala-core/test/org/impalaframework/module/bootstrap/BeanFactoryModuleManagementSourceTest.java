package org.impalaframework.module.bootstrap;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactoryModuleManagementSourceTest extends TestCase {

	public final void testBootstrapBeanFactory() {
		BeanFactoryModuleManagementSource factory = new BeanFactoryModuleManagementSource(new ClassPathXmlApplicationContext(
				"META-INF/impala-bootstrap.xml"));

		assertNotNull(factory.getApplicationContextLoader());
		assertNotNull(factory.getClassLocationResolver());
		assertNotNull(factory.getPluginLoaderRegistry());
		assertNotNull(factory.getModificationExtractorRegistry());
		assertNotNull(factory.getModuleStateHolder());
		assertNotNull(factory.getTransitionProcessorRegistry());
		assertNotNull(factory.getClassLocationResolver());
	}

}
