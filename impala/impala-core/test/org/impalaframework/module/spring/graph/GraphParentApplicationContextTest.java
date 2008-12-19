package org.impalaframework.module.spring.graph;

import static org.easymock.classextension.EasyMock.*;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;

import junit.framework.TestCase;

public class GraphParentApplicationContextTest extends TestCase {

	private ApplicationContext delegate;
	private GraphParentApplicationContext parent;

	@Override
	protected void setUp() throws Exception {
		delegate = createMock(ApplicationContext.class);
		parent = new GraphParentApplicationContext(delegate);
	}
	
	public void testDelegatingMethods() throws Exception {
		
		expect(delegate.containsBean("bean")).andReturn(true);
		expect(delegate.containsBeanDefinition("bean")).andReturn(true);
		expect(delegate.containsLocalBean("bean")).andReturn(true);
		expect(delegate.getAliases("bean")).andReturn(new String[0]);
		expect(delegate.getAutowireCapableBeanFactory()).andReturn(null);
		expect(delegate.getBean("bean")).andReturn(null);
		expect(delegate.getBean("bean", String.class)).andReturn(null);
		expect(delegate.getBean(eq("bean"), aryEq(new String[0]))).andReturn(null);
		expect(delegate.getBeanDefinitionCount()).andReturn(0);
		expect(delegate.getBeanDefinitionNames()).andReturn(null);
		expect(delegate.getBeanNamesForType(String.class)).andReturn(null);
		expect(delegate.getBeanNamesForType(String.class, true, false)).andReturn(null);
		expect(delegate.getClassLoader()).andReturn(null);
		expect(delegate.getMessage(isA(MessageSourceResolvable.class), eq(Locale.getDefault()))).andReturn(null);
		expect(delegate.getMessage(eq("code"), aryEq(new String[0]), eq(Locale.getDefault()))).andReturn(null);
		expect(delegate.getMessage(eq("code"), aryEq(new String[0]), eq("default"), eq(Locale.getDefault()))).andReturn(null);
		expect(delegate.getParentBeanFactory()).andReturn(null);
		expect(delegate.getResource("location")).andReturn(null);
		expect(delegate.getResources("locationPattern")).andReturn(null);
		expect(delegate.getType("type")).andReturn(null);
		expect(delegate.isPrototype("bean")).andReturn(true);
		expect(delegate.isSingleton("bean")).andReturn(true);
		expect(delegate.isTypeMatch("bean", String.class)).andReturn(true);
		delegate.publishEvent(isA(ApplicationEvent.class));
		
		replay(delegate);
		
		parent.containsBean("bean");
		parent.containsBeanDefinition("bean");
		parent.containsLocalBean("bean");
		parent.getAliases("bean");
		parent.getAutowireCapableBeanFactory();
		parent.getBean("bean");
		parent.getBean("bean", String.class);
		parent.getBean("bean", new String[0]);
		parent.getBeanDefinitionCount();
		parent.getBeanDefinitionNames();
		parent.getBeanNamesForType(String.class);
		parent.getBeanNamesForType(String.class, true, false);
		parent.getClassLoader();
		parent.getDisplayName();
		parent.getMessage(createMock(MessageSourceResolvable.class), Locale.getDefault());
		parent.getMessage("code", new String[0], Locale.getDefault());
		parent.getMessage("code", new String[0], "default", Locale.getDefault());
		assertSame(delegate, parent.getParent());
		parent.getParentBeanFactory();
		parent.getResource("location");
		parent.getResources("locationPattern");
		parent.getType("type");
		parent.isPrototype("bean");
		parent.isSingleton("bean");
		parent.isTypeMatch("bean", String.class);
		parent.publishEvent(createMock(ApplicationEvent.class));
		
		assertNotNull(parent.getId());
		assertTrue(parent.getStartupDate() > 0);
		
		verify(delegate);
	}
	
}
