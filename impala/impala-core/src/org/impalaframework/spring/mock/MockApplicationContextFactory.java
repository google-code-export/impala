package org.impalaframework.spring.mock;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;

public class MockApplicationContextFactory {

	private BeanMap beanMap = new BeanMap();

	public MockApplicationContextFactory() {
		super();
	}

	public ApplicationContext mockContext() {
		ProxyFactory factory = new ProxyFactory();
		factory.setInterfaces(new Class[] { ApplicationContext.class });
		factory.addAdvice(new BeanMapInterceptor(beanMap));
		return (ApplicationContext) factory.getProxy();
	}

	public void putBean(String beanName, Object bean) {
		beanMap.putBean(beanName, bean);
	}

}
