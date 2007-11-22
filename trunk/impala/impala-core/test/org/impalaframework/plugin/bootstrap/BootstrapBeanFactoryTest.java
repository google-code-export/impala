package org.impalaframework.plugin.bootstrap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class BootstrapBeanFactoryTest extends TestCase {

	public final void testBootstrapBeanFactory() {
		BootstrapBeanFactory factory = new BootstrapBeanFactory(new ClassPathXmlApplicationContext(
				"org/impalaframework/plugin/bootstrap/impala-bootstrap.xml"));

		assertNotNull(factory.getApplicationContextLoader());
		assertNotNull(factory.getClassLocationResolver());
		assertNotNull(factory.getPluginLoaderRegistry());
		assertNotNull(factory.getPluginModificationCalculator());
		assertNotNull(factory.getPluginStateManager());
		assertNotNull(factory.getStickyPluginModificationCalculator());
		assertNotNull(factory.getTransitionProcessorRegistry());
		assertNotNull(factory.getClassLocationResolver());
	}

}
