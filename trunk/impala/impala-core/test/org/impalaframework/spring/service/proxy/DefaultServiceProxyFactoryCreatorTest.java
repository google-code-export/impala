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

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.reference.BasicServiceRegistryReference;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class DefaultServiceProxyFactoryCreatorTest extends TestCase {

    private DefaultServiceProxyFactoryCreator creator;
    private ServiceRegistry serviceRegistry;
    private Class<?>[] classes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        classes = new Class[]{List.class};
        creator = new DefaultServiceProxyFactoryCreator();
        serviceRegistry = createMock(ServiceRegistry.class);
    }
    
    @SuppressWarnings("unchecked")
    public void testDynamicProxyFactory() throws Exception {
        final List<String> list = new ArrayList<String>();
        ServiceRegistryReference ref = new BasicServiceRegistryReference(list, "mybean", "mymod", ClassUtils.getDefaultClassLoader());
        expect(serviceRegistry.getService("mykey", classes, false)).andReturn(ref);
        expect(serviceRegistry.getService("mykey", classes, false)).andReturn(ref);
        
        replay(serviceRegistry);
        final ProxyFactory proxyFactory = creator.createProxyFactory(new BeanRetrievingProxyFactorySource(classes, serviceRegistry, "mykey"), null);
        
        final List proxy = (List) proxyFactory.getProxy();
        proxy.add("obj");
        
        verify(serviceRegistry);
        
        assertTrue(list.contains("obj"));
    }
    
    @SuppressWarnings("unchecked")
    public void testDynamicProxyFactoryForClass() throws Exception { 
        classes = new Class[]{ 
                ArrayList.class 
                };
        final List<String> list = new ArrayList<String>();
        ServiceRegistryReference ref = new BasicServiceRegistryReference(list, "mybean", "mymod", ClassUtils.getDefaultClassLoader());
        expect(serviceRegistry.getService("mykey", classes, false)).andReturn(ref);
        expect(serviceRegistry.getService("mykey", classes, false)).andReturn(ref);
        
        replay(serviceRegistry);
        final ProxyFactory proxyFactory = creator.createProxyFactory(new BeanRetrievingProxyFactorySource(classes, serviceRegistry, "mykey"), null);
        
        final List proxy = (List) proxyFactory.getProxy();
        proxy.add("obj");
        
        verify(serviceRegistry);
        
        assertTrue(list.contains("obj"));
    }
    
    @SuppressWarnings("unchecked")
    public void testStaticProxyFactory() throws Exception {
        final List<String> list = new ArrayList<String>();
        ServiceRegistryReference ref = new BasicServiceRegistryReference(list, "mybean", "mymod", ClassUtils.getDefaultClassLoader());
        
        replay(serviceRegistry);
        final ProxyFactory proxyFactory = creator.createProxyFactory(new StaticServiceReferenceProxyFactorySource(new Class<?>[]{List.class}, ref), null);
        
        final List proxy = (List) proxyFactory.getProxy();
        proxy.add("obj");
        
        verify(serviceRegistry);
    }
    
    @SuppressWarnings("unchecked")
    public void testStaticProxyFactoryWithNoInterfaces() throws Exception {
        final List<String> list = new ArrayList<String>();
        ServiceRegistryReference ref = new BasicServiceRegistryReference(list, "mybean", "mymod", ClassUtils.getDefaultClassLoader());
        
        replay(serviceRegistry);
        final ProxyFactory proxyFactory = creator.createProxyFactory(new StaticServiceReferenceProxyFactorySource(null, ref), null);
        
        final List proxy = (List) proxyFactory.getProxy();
        assertTrue(proxy instanceof ArrayList);
        proxy.add("obj");
        
        verify(serviceRegistry);
    }
    
    public void testWithFinalClass() throws Exception {
        ServiceRegistryReference ref = new BasicServiceRegistryReference("service", "mybean", "mymod", ClassUtils.getDefaultClassLoader());
        
        replay(serviceRegistry);
        try {
            creator.createProxyFactory(new StaticServiceReferenceProxyFactorySource(null, ref), null);
            fail();
        }
        catch (Exception e) {
            System.out.println(e);
            assertTrue(e.getMessage().endsWith("as no interfaces have been specified and the bean class is final, therefore cannot be proxied"));
        }
        
        verify(serviceRegistry);
    }
    
}
