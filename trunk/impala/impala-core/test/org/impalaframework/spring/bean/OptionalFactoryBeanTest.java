package org.impalaframework.spring.bean;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.*;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import junit.framework.TestCase;

public class OptionalFactoryBeanTest extends TestCase {
    
    private OptionalFactoryBean factoryBean;
    private ApplicationContext applicationContext;
    private String fallback;
    private String bean;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fallback = "fallback";
        bean = "bean";
        
        factoryBean = new OptionalFactoryBean();
        applicationContext = createMock(ApplicationContext.class);
        factoryBean.setApplicationContext(applicationContext);
        factoryBean.setBeanName("beanName");
        factoryBean.setFallback(fallback);
    }

    public void testGetBean() throws Exception {
        
        expect(applicationContext.getBean("beanName")).andReturn(bean);
        
        replay(applicationContext);
        
        factoryBean.afterPropertiesSet();
        assertEquals(bean, factoryBean.getObject());
        
        verify(applicationContext);
    }

    public void testNull() throws Exception {
        
        expect(applicationContext.getBean("beanName")).andReturn(null);
        
        replay(applicationContext);
        
        factoryBean.afterPropertiesSet();
        assertEquals(fallback, factoryBean.getObject());
        
        verify(applicationContext);
    }

    public void testNoSuchBeanDef() throws Exception {
        
        expect(applicationContext.getBean("beanName")).andThrow(new NoSuchBeanDefinitionException("beanName"));
        
        replay(applicationContext);
        
        factoryBean.afterPropertiesSet();
        assertEquals(fallback, factoryBean.getObject());
        
        verify(applicationContext);
    }

}
