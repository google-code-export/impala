package org.impalaframework.spring.module;

import java.util.Collections;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.exception.ExecutionException;
import org.springframework.beans.factory.BeanFactory;

public class AutoRegisteringModuleContributionExporterTest extends TestCase {

	private AutoRegisteringModuleContributionExporter exporter;

	@Override
	protected void setUp() throws Exception {
		exporter = new AutoRegisteringModuleContributionExporter();
	}

	public final void testInvalidGetBeanDefinitionRegistry() {
		try {
			exporter.getBeanDefinitionRegistry(EasyMock.createMock(BeanFactory.class));
			fail();
		}
		catch (ExecutionException e) {
			assertEquals(
					"Cannot use org.impalaframework.spring.module.AutoRegisteringModuleContributionExporter with bean factory which does not implement org.springframework.beans.factory.support.BeanDefinitionRegistry",
					e.getMessage());
		}
	}

	public void testGetContributionClasses() throws Exception {
		exporter.checkContributionClasses(Collections.emptyList(),
				"beanName", "java.util.List,java.io.Serializable");
	}

	public void testInvalidContributionClasses() throws Exception {
		try {
			exporter.checkContributionClasses(Collections.emptyList(), "beanName", "java.util.List,java.util.Map");
			fail();
		}
		catch (ExecutionException e) {
			assertEquals("Bean 'beanName' is not instance of type java.util.Map, declared in type list 'java.util.List,java.util.Map'", e.getMessage());
		}
	}

}
