package net.java.impala.spring.plugin;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class PluginBeanPostProcessorTest extends TestCase {

	private PluginBeanPostProcessor p;
	private DefaultListableBeanFactory beanFactory;
	private DefaultListableBeanFactory parentBeanFactory;
	private PluginProxyFactoryBean pluginProxyFactoryBean;
	private FactoryBean factoryBean;

	public void setUp()
	{
		p = new PluginBeanPostProcessor();
		beanFactory = createMock(DefaultListableBeanFactory.class);
		parentBeanFactory = createMock(DefaultListableBeanFactory.class);
		pluginProxyFactoryBean = createMock(PluginProxyFactoryBean.class);
		factoryBean = createMock(FactoryBean.class);
		p.setBeanFactory(beanFactory);
	}
	
	public void testNull() {
		p.setBeanFactory(null);
		p.postProcessAfterInitialization(new Object(), "mybean");
		p.findFactoryBean("mybean");
	}
	
	public void testPostProcessAfterInitialization() {
		expectFactoryBean();
		Object object = new Object();
		
		//this is the method we are expecting to be called
		pluginProxyFactoryBean.registerTarget(object);
		
		replay(beanFactory);
		replay(parentBeanFactory);
		replay(pluginProxyFactoryBean);
		assertEquals(object, p.postProcessAfterInitialization(object, "mybean"));
		verify(beanFactory);
		verify(parentBeanFactory);
		verify(pluginProxyFactoryBean);
	}
	
	public void testPostProcessAfterInitializationFactoryBean() throws Exception {
		expectFactoryBean();

		//verify that if the object is a factory bean
		//then the registered object is the factoryBean.getObject()
		Object object = new Object();
		expect(factoryBean.getObject()).andReturn(object);
		
		pluginProxyFactoryBean.registerTarget(object);
		
		replay(beanFactory);
		replay(parentBeanFactory);
		replay(pluginProxyFactoryBean);
		replay(factoryBean);
		assertEquals(factoryBean, p.postProcessAfterInitialization(factoryBean, "mybean"));
		verify(beanFactory);
		verify(parentBeanFactory);
		verify(pluginProxyFactoryBean);
		verify(factoryBean);
	}
	
	
	public void testFindFactoryBean() {
		expectFactoryBean();
		
		replay(beanFactory);
		replay(parentBeanFactory);
		assertEquals(pluginProxyFactoryBean, p.findFactoryBean("mybean"));
		verify(beanFactory);
		verify(parentBeanFactory);
	}

	private void expectFactoryBean() {
		expect(beanFactory.getParentBeanFactory()).andReturn(parentBeanFactory);
		expect(parentBeanFactory.containsBean("&mybean")).andReturn(true);
		expect(parentBeanFactory.getBean("&mybean")).andReturn(pluginProxyFactoryBean);
	}

}
