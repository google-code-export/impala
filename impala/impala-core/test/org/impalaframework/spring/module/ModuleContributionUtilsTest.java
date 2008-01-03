package org.impalaframework.spring.module;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ModuleContributionUtilsTest extends TestCase {

	public final void testFindContributionEndPointPresent() {
		ClassPathXmlApplicationContext childOfChild = getContext("contribution/root-with-definition.xml")[0];

		//true because applicationContext is not an instanceof AbstractBeanFactory
		assertNotNull(ModuleContributionUtils.findContributionEndPoint(childOfChild, "child"));
		assertNotNull(ModuleContributionUtils.findContributionEndPoint(childOfChild.getBeanFactory(), "child"));
	}

	public final void testFindContributionEndPointNotPresent() {
		ClassPathXmlApplicationContext childOfChild = getContext("contribution/root-no-definition.xml")[0];

		ContributionEndpoint endPoint = ModuleContributionUtils.findContributionEndPoint(childOfChild.getBeanFactory(),
				"child");
		assertNull(endPoint);
	}

	public final void testGetRootBeanFactory() {
		ClassPathXmlApplicationContext[] contexts = getContext("contribution/root-with-definition.xml");
		ClassPathXmlApplicationContext parent = contexts[2];
		assertSame(parent, ModuleContributionUtils.getRootBeanFactory(contexts[0]));
		assertSame(parent, ModuleContributionUtils.getRootBeanFactory(contexts[1]));
		assertSame(parent, ModuleContributionUtils.getRootBeanFactory(contexts[2]));		
	}

	public final void testGetInvalidRootBeanFactory() {
		try {
			ModuleContributionUtils.getRootBeanFactory(EasyMock.createMock(BeanFactory.class));
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("BeanFactory EasyMock for interface org.springframework.beans.factory.BeanFactory is of type $Proxy0, which is not an instance of org.springframework.beans.factory.HierarchicalBeanFactory", e.getMessage());
		}		
	}

	public final void testGetTarget() {
		ClassPathXmlApplicationContext childOfChild = getContext("contribution/root-with-definition.xml")[0];
		
		Object bean = childOfChild.getBean("&moduleDefinition");
		assertTrue(bean instanceof FactoryBean);
		
		Object target = ModuleContributionUtils.getTarget(bean, "moduleDefinition");
		assertFalse(target instanceof FactoryBean);
		assertSame(target, ModuleContributionUtils.getTarget(target, "moduleDefinition"));
	}
	
	
	private ClassPathXmlApplicationContext[] getContext(String rootName) {
		ClassPathXmlApplicationContext parent = new ClassPathXmlApplicationContext(rootName);
		ClassPathXmlApplicationContext child = new ClassPathXmlApplicationContext(
				new String[] { "contribution/child.xml" }, parent);
		ClassPathXmlApplicationContext childOfChild = new ClassPathXmlApplicationContext(
				new String[] { "contribution/array-exporter.xml" }, child);
		return new ClassPathXmlApplicationContext[] {childOfChild, child, parent};
	}

}
