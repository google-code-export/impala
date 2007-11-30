package org.impalaframework.spring.mock;

import junit.framework.TestCase;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class MockApplicationContextFactoryTest extends TestCase {

	public final void testMockContext() {
		MockApplicationContextFactory factory = new MockApplicationContextFactory();
		ApplicationContext mockContext = factory.mockContext();

		assertNull(mockContext.getBean("mybean"));
		assertFalse(mockContext.containsBean("mybean"));
		
		Object object = new Object();
		factory.putBean("mybean", object);
		assertSame(object, mockContext.getBean("mybean"));

		assertTrue(mockContext.containsBean("mybean"));
		assertEquals(Object.class, mockContext.getType("mybean"));
	}
	
	public final void testStrictContext() {
		MockApplicationContextFactory factory = new MockApplicationContextFactory(true);
		ApplicationContext mockContext = factory.mockContext();

		try {
			mockContext.getBean("mybean");
			fail();
		}
		catch (NoSuchBeanDefinitionException e) {
			assertEquals("No bean named 'mybean' is defined", e.getMessage());
		}
		
		assertFalse(mockContext.containsBean("mybean"));
		
		Object object = new Object();
		factory.putBean("mybean", object);
		assertSame(object, mockContext.getBean("mybean"));

		assertTrue(mockContext.containsBean("mybean"));

		assertEquals(Object.class, mockContext.getType("mybean"));
	}

}
