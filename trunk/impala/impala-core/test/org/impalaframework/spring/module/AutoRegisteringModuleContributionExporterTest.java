package org.impalaframework.spring.module;

import junit.framework.TestCase;

import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.ChildBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutoRegisteringModuleContributionExporterTest extends TestCase {

	public final void testAutoRegisteringExporterWithNoDefinition() {
		doTest("contribution/root-no-definition.xml");
	}

	public final void testAutoRegisteringExporterWithDefinition() {
		doTest("contribution/root-with-definition.xml");
	}

	private void doTest(String rootDefinition) {
		ClassPathXmlApplicationContext parent = new ClassPathXmlApplicationContext(rootDefinition);
		ClassPathXmlApplicationContext child = new ClassPathXmlApplicationContext(
				new String[] { "contribution/child.xml" }, parent);
		ClassPathXmlApplicationContext childOfChild = new ClassPathXmlApplicationContext(
				new String[] { "contribution/autoregistering-exporter.xml" }, child);

		Object childOfChildBean = childOfChild.getBean("child");
		Object beanFromParent = parent.getBean("child");

		assertTrue(childOfChildBean instanceof ChildBean);
		assertTrue(beanFromParent instanceof Child);
		assertFalse(beanFromParent instanceof ChildBean);
	}
}
