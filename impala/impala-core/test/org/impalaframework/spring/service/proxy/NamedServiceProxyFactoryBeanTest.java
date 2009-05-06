/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.spring.service.proxy;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.Parent;
import org.springframework.util.ClassUtils;

/**
 * Test for {@link NamedServiceProxyFactoryBean}
 * @author Phil Zoio
 */
public class NamedServiceProxyFactoryBeanTest extends TestCase {

    private NamedServiceProxyFactoryBean bean;
    private ServiceRegistryImpl serviceRegistry;
    private ClassLoader classLoader;
    private DefaultServiceProxyFactoryCreator proxyFactoryCreator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bean = new NamedServiceProxyFactoryBean();
        serviceRegistry = new ServiceRegistryImpl();
        
        proxyFactoryCreator = new DefaultServiceProxyFactoryCreator();
        bean.setServiceRegistry(serviceRegistry);
        
        classLoader = ClassUtils.getDefaultClassLoader();
    }
    
    public void testWithBeanName() throws Exception {
        bean.setProxyTypes(new Class[] { Child.class });
        bean.setBeanName("someBean");
        bean.afterPropertiesSet();

        Child child = (Child) bean.getObject();

        try {
            child.childMethod();
            fail();
        }
        catch (NoServiceException e) {
        }

        Child service = newChild();
        serviceRegistry.addService("someBean", "pluginName", new StaticServiceBeanReference(service), classLoader);
        child.childMethod();
    }   
    
    public void testWithExportName() throws Exception {
        bean.setProxyTypes(new Class[] { Child.class });
        bean.setBeanName("someBean");
        bean.setExportName("exportBean");
        bean.afterPropertiesSet();

        Child child = (Child) bean.getObject();

        try {
            child.childMethod();
            fail();
        }
        catch (NoServiceException e) {
            e.printStackTrace();
        }

        Child service = newChild();
        serviceRegistry.addService("exportBean", "pluginName",  new StaticServiceBeanReference(service), classLoader);
        child.childMethod();
    }
    
    public void testAllowNoService() throws Exception {
        bean.setProxyTypes(new Class[] { Child.class });
        bean.setBeanName("someBean");
        proxyFactoryCreator.setAllowNoService(true);
        bean.setServiceProxyFactoryCreator(proxyFactoryCreator);
        
        bean.afterPropertiesSet();

        Child child = (Child) bean.getObject();
        child.childMethod();
    }

    private Child newChild() {
        return new Child() {
            public void childMethod() {
            }

            public Parent tryGetParent() {
                return null;
            }
        };
    }

}
