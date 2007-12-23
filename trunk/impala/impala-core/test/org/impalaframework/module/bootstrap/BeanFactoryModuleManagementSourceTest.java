package org.impalaframework.module.bootstrap;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactoryModuleManagementSourceTest extends TestCase {

	public final void testBootstrapBeanFactory() {
		BeanFactoryModuleManagementFactory factory = new BeanFactoryModuleManagementFactory(new ClassPathXmlApplicationContext(
				"META-INF/impala-bootstrap.xml"));

		assertNotNull(factory.getApplicationContextLoader());
		assertNotNull(factory.getClassLocationResolver());
		assertNotNull(factory.getPluginLoaderRegistry());
		assertNotNull(factory.getModificationExtractorRegistry());
		assertNotNull(factory.getModuleStateHolder());
		assertNotNull(factory.getTransitionProcessorRegistry());
		assertNotNull(factory.getClassLocationResolver());
		assertNotNull(factory.getModuleOperationRegistry());
	}

}
