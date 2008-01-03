package org.impalaframework.spring.module;

import junit.framework.TestCase;

import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.ChildBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ModuleContributionExportersTest extends TestCase {

	public final void testAutoRegisteringExporterWithNoDefinition() {
		doTest("contribution/root-no-definition.xml", "contribution/autoregistering-exporter.xml");
	}

	public final void testAutoRegisteringExporterWithDefinition() {
		doTest("contribution/root-with-definition.xml", "contribution/autoregistering-exporter.xml");
	}

	public final void testModuleArrayExporterWithNoDefinition() {
		try {
			doTest("contribution/root-no-definition.xml", "contribution/array-exporter.xml");
			fail();
		}
		catch (NoSuchBeanDefinitionException e) {
			System.out.println(e.getMessage());
		}
	}

	public final void testModuleArrayExporterWithDefinition() {
		doTest("contribution/root-with-definition.xml", "contribution/array-exporter.xml");
	}

	private void doTest(String rootDefinition, String childDefinition) {
		ClassPathXmlApplicationContext parent = new ClassPathXmlApplicationContext(rootDefinition);
		ClassPathXmlApplicationContext child = new ClassPathXmlApplicationContext(
				new String[] { "contribution/child.xml" }, parent);
		ClassPathXmlApplicationContext childOfChild = new ClassPathXmlApplicationContext(
				new String[] { childDefinition }, child);

		checkBean(parent, childOfChild, "child");
		checkBean(parent, childOfChild, "another");
	}

	private void checkBean(ApplicationContext parent, ClassPathXmlApplicationContext childOfChild, String beanName) {
		Object beanFromChild = childOfChild.getBean(beanName);
		Object beanFromRoot = parent.getBean(beanName);

		assertTrue(beanFromChild instanceof ChildBean);
		assertTrue(beanFromRoot instanceof Child);
		assertFalse(beanFromRoot instanceof ChildBean);
	}
}
