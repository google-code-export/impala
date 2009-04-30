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

package org.impalaframework.spring.service.exporter;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.service.ContributionEndpoint;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.impalaframework.spring.service.proxy.NamedServiceProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class ModuleContributionPostProcessorTest extends TestCase {

    private ModuleContributionPostProcessor p;
    private DefaultListableBeanFactory beanFactory;
    private DefaultListableBeanFactory parentBeanFactory;
    private ContributionEndpoint endPoint;
    private FactoryBean factoryBean;
    private ServiceRegistry serviceRegistry;
    private Class<?>[] classes;

    public void setUp()
    {
        classes = new Class[]{Object.class};
        p = new ModuleContributionPostProcessor();
        beanFactory = createMock(DefaultListableBeanFactory.class);
        parentBeanFactory = createMock(DefaultListableBeanFactory.class);
        endPoint = createMock(NamedServiceProxyFactoryBean.class);
        factoryBean = createMock(FactoryBean.class);
        serviceRegistry = new ServiceRegistryImpl();
        p.setBeanFactory(beanFactory);
        p.setServiceRegistry(serviceRegistry);
        p.setBeanClassLoader(ClassUtils.getDefaultClassLoader());
    }
    
    public void testNull() {
        p.setBeanFactory(null);
        p.postProcessAfterInitialization(new Object(), "mybean");
        ModuleContributionUtils.findContributionEndPoint(beanFactory, "mybean");
    }
    
    public void testPostProcessAfterInitialization() {
        expectFactoryBean();
        Object object = new Object();
        p.setModuleDefinition(new SimpleModuleDefinition("pluginName"));
        
        //this is the method we are expecting to be called
        //endPoint.registerTarget("pluginName", object);
        
        replay(beanFactory);
        replay(parentBeanFactory);
        replay(endPoint);
        assertEquals(object, p.postProcessAfterInitialization(object, "mybean"));
        verify(beanFactory);
        verify(parentBeanFactory);
        verify(endPoint);
        
        ServiceRegistryReference service = serviceRegistry.getService("mybean", classes, false);
        assertSame(object, service.getBean());
    }
    
    public void testPostProcessAfterInitializationFactoryBean() throws Exception {
        expectFactoryBean();
        expect(factoryBean.getObject()).andReturn("value");

        //verify that if the object is a factory bean
        //then the registered object is the factoryBean.getObject()
        p.setModuleDefinition(new SimpleModuleDefinition("pluginName"));
        
        //endPoint.registerTarget("pluginName", object);
        
        replay(beanFactory);
        replay(parentBeanFactory);
        replay(endPoint);
        replay(factoryBean);
        assertEquals(factoryBean, p.postProcessAfterInitialization(factoryBean, "mybean"));

        ServiceRegistryReference service = serviceRegistry.getService("mybean", classes, false);
        assertNotNull(service.getBean());
        
        verify(beanFactory);
        verify(parentBeanFactory);
        verify(endPoint);
        verify(factoryBean);
    }
    
    
    public void testFindFactoryBean() {
        expectFactoryBean();
        
        replay(beanFactory);
        replay(parentBeanFactory);
        assertEquals(endPoint, ModuleContributionUtils.findContributionEndPoint(beanFactory, "mybean"));
        verify(beanFactory);
        verify(parentBeanFactory);
    }

    private void expectFactoryBean() {
        expect(beanFactory.getParentBeanFactory()).andReturn(parentBeanFactory);
        expect(parentBeanFactory.containsBean("&mybean")).andReturn(true);
        expect(parentBeanFactory.getBean("&mybean")).andReturn(endPoint);
    }

}
