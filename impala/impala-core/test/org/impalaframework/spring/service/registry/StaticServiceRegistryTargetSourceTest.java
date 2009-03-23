package org.impalaframework.spring.service.registry;

import junit.framework.TestCase;

import org.impalaframework.service.registry.BasicServiceRegistryReference;
import org.impalaframework.spring.bean.StringFactoryBean;
import org.springframework.util.ClassUtils;

public class StaticServiceRegistryTargetSourceTest extends TestCase {

	public void testWithBean() throws Exception {
		final BasicServiceRegistryReference reference = new BasicServiceRegistryReference("bean", "beanName", "moduleName", ClassUtils.getDefaultClassLoader());
		doTest(reference);
	}
	
	public void testWithFactoryBean() throws Exception {
		final StringFactoryBean bean = new StringFactoryBean();
		bean.setValue("bean");
		final BasicServiceRegistryReference reference = new BasicServiceRegistryReference(bean, "beanName", "moduleName", ClassUtils.getDefaultClassLoader());
		doTest(reference);
	}

	private void doTest(final BasicServiceRegistryReference reference)
			throws Exception {
		StaticServiceRegistryTargetSource targetSource = new StaticServiceRegistryTargetSource(reference);
		assertEquals("bean", targetSource.getTarget());
		assertSame(reference, targetSource.getServiceRegistryReference());
		assertSame(true, targetSource.isStatic());
		assertSame(true, targetSource.hasTarget());
	}

}
