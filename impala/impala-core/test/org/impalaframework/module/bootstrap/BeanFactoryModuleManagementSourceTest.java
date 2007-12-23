package org.impalaframework.module.bootstrap;

import junit.framework.TestCase;

import org.impalaframework.util.ObjectUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactoryModuleManagementSourceTest extends TestCase {

	public final void testBootstrapBeanFactory() {

		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/impala-bootstrap.xml");
		Object bean = appContext.getBean("moduleManagementFactory");
		ModuleManagementFactory factory = ObjectUtils.cast(bean, ModuleManagementFactory.class);

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
