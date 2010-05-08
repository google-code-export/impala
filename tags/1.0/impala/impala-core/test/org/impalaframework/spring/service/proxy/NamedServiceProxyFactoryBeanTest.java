/*
 * Copyright 2007-2010 the original author or authors.
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

import java.util.Arrays;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.Parent;
import org.springframework.util.ClassUtils;

/**
 * Test for {@link NamedServiceProxyFactoryBean}
 * @author Phil Zoio
 */
public class NamedServiceProxyFactoryBeanTest extends TestCase {

    private NamedServiceProxyFactoryBean bean;
    private DelegatingServiceRegistry serviceRegistry;
    private ClassLoader classLoader;
    private DefaultProxyFactoryCreator proxyFactoryCreator;
    private Class<?>[] exportTypes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bean = new NamedServiceProxyFactoryBean();
        serviceRegistry = new DelegatingServiceRegistry();
        
        proxyFactoryCreator = new DefaultProxyFactoryCreator();
        bean.setServiceRegistry(serviceRegistry);
        
        classLoader = ClassUtils.getDefaultClassLoader();
        exportTypes = new Class<?>[] { Child.class };
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
        
        //now register using types. Will still not be able to find
        Child service = newChild();
        serviceRegistry.addService(null, "moduleName",  new StaticServiceBeanReference(service), Arrays.asList(exportTypes) , null, classLoader);
        try {
            child.childMethod();
            fail();
        }
        catch (NoServiceException e) {
            e.printStackTrace();
        }

        serviceRegistry.addService("someBean", "moduleName", new StaticServiceBeanReference(service), classLoader);
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
        serviceRegistry.addService("exportBean", "moduleName",  new StaticServiceBeanReference(service), classLoader);
        child.childMethod();
    }
    
    public void testAllowNoService() throws Exception {
        bean.setProxyTypes(new Class[] { Child.class });
        bean.setBeanName("someBean");
        proxyFactoryCreator.setAllowNoService(true);
        bean.setProxyFactoryCreator(proxyFactoryCreator);
        
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
