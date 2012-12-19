package org.impalaframework.spring.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * Factory bean which will attempt to use a named bean, if present. If not, will use the fallback bean
 * @author Phil Zoio
 */
public class OptionalFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {
    
    private static final Log logger = LogFactory.getLog(OptionalFactoryBean.class);
    
    /**
     * Fallback bean, wired in as a dependency. Cannot be null
     */
    private Object fallback;
    
    /**
     * The application context bean name to look for.
     */
    private String beanName;
    
    /**
     * The application context from which this bean will attempt to load the optional bean
     */
    private ApplicationContext applicationContext;
    
    /**
     * The populated bean, either the optianal or fallback, set during {@link #afterPropertiesSet()}
     */
    private Object bean;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(applicationContext, "applicationContext cannot be null");
        Assert.notNull(fallback, "fallback cannot be null");
        Assert.notNull(beanName, "beanName cannot be null");
        
        try {
            this.bean = applicationContext.getBean(beanName);
        }
        catch (NoSuchBeanDefinitionException e) {
            logger.info("Unable to find optional bean '" + beanName + "', using fallback, an instance of " + fallback.getClass().getName());
        }
        
        if (this.bean == null) {
            this.bean = fallback;
        }
    }

    public Object getObject() throws Exception {
        return bean;
    }
    
    public Class<Object> getObjectType() {
        return Object.class;
    }
    
    public boolean isSingleton() {
        return true;
    }
 
    public void setFallback(Object fallback) {
        this.fallback = fallback;
    }
    
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

}
