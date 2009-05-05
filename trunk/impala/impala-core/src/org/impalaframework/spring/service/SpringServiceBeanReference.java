package org.impalaframework.spring.service;

import org.impalaframework.service.ServiceBeanReference;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;

public class SpringServiceBeanReference implements ServiceBeanReference {

    private final String beanName;
    private final BeanFactory beanFactory;
    
    public SpringServiceBeanReference(BeanFactory beanFactory, String beanName) {
        super();
        Assert.notNull(beanName);
        Assert.notNull(beanFactory);
        
        this.beanFactory = beanFactory;
        this.beanName = beanName;
    }

    public Object getService() {
        return beanFactory.getBean(beanName);
    }

    public boolean isStatic() {
        return false;
    }

}
