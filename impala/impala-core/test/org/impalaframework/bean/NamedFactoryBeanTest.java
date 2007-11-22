package org.impalaframework.bean;

import static org.easymock.EasyMock.*;
import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;

public class NamedFactoryBeanTest extends TestCase {

	private BeanFactory beanFactory;
	private NamedFactoryBean factoryBean;

	@Override
	protected void setUp() throws Exception {
		beanFactory = createMock(BeanFactory.class);
		factoryBean = new NamedFactoryBean();
		factoryBean.setBeanName("mybean");
		factoryBean.setObjectType(Integer.class);
		factoryBean.setBeanFactory(beanFactory);
	}
	
	public void testGetObject() throws Exception {
		
		expect(beanFactory.getBean("mybean", Integer.class)).andReturn(1);
		replay(beanFactory);
		assertEquals(new Integer(1), factoryBean.getObject());
		verify(beanFactory);
	}
	
	public void testInvalidType() throws Exception {
		
		expect(beanFactory.getBean("mybean", Integer.class)).andReturn("a string");
		replay(beanFactory);
		factoryBean.getObject();
		verify(beanFactory);
	}

}
