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

import java.util.Arrays;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.Parent;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class TypedServiceProxyFactoryBeanTest extends TestCase {

    private TypedServiceProxyFactoryBean bean;
    private DelegatingServiceRegistry serviceRegistry;
    private ClassLoader classLoader;
    private Class<?>[] exportTypes;
    private Class<?>[] proxyClassTypes;
    private Class<?>[] proxyInterfaceTypes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bean = new TypedServiceProxyFactoryBean();
        serviceRegistry = new DelegatingServiceRegistry();
        bean.setServiceRegistry(serviceRegistry);
        
        classLoader = ClassUtils.getDefaultClassLoader();
        exportTypes = new Class<?>[] { Child.class };
        proxyClassTypes = new Class<?>[] { TestChild.class };
        proxyInterfaceTypes = new Class<?>[] { SubChild.class, GrandChild.class };
    }
    
    public void testGetObjectType() throws Exception {
        assertNull(bean.getObjectType());
        
        bean.setExportTypes(exportTypes);
        assertEquals(Child.class, bean.getObjectType());

        bean.setProxyTypes(proxyClassTypes);
        assertEquals(TestChild.class, bean.getObjectType());

        bean.setProxyTypes(proxyInterfaceTypes);
        assertEquals(SubChild.class, bean.getObjectType());
        
        bean.setExportTypes(null);
        assertEquals(SubChild.class, bean.getObjectType());
    }
    
    public void testWithExportTypesAndBeanName() throws Exception {
        
        bean.setExportName("mybean");
        bean.setExportTypes(exportTypes);
        bean.afterPropertiesSet();

        Child child = (Child) bean.getObject();

        try {
            child.childMethod();
            fail();
        }
        catch (NoServiceException e) {
            e.printStackTrace();
        }

        //create service and register using export types
        Child service = newChild();
        serviceRegistry.addService(null, "moduleName",  new StaticServiceBeanReference(service), Arrays.asList(exportTypes) , null, classLoader);
        try {
            child.childMethod();
            fail();
        }
        catch (NoServiceException e) {
            e.printStackTrace();
        }
        
        //register using bean name too
        serviceRegistry.addService("mybean", "moduleName",  new StaticServiceBeanReference(service), Arrays.asList(exportTypes) , null, classLoader);
        child.childMethod();
    }  
    
    public void testWithExportTypesOnly() throws Exception {
        bean.setExportTypes(exportTypes);
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
        serviceRegistry.addService(null, "moduleName",  new StaticServiceBeanReference(service), Arrays.asList(exportTypes) , null, classLoader);
        child.childMethod();
    }  
    
    public void testWithExportAndProxyClass() throws Exception {
        bean.setExportTypes(exportTypes);
        bean.setProxyTypes(proxyClassTypes);
        bean.afterPropertiesSet();

        TestChild child = (TestChild) bean.getObject();

        try {
            child.childMethod();
            fail();
        }
        catch (NoServiceException e) {
            e.printStackTrace();
        }

        Child service = newChild();
        serviceRegistry.addService(null, "moduleName",  new StaticServiceBeanReference(service), Arrays.asList(exportTypes) , null, classLoader);
        child.childMethod();
    }
    
    public void testWithExportAndProxyInterfaces() throws Exception {
        bean.setExportTypes(exportTypes);
        bean.setProxyTypes(proxyInterfaceTypes);
        bean.afterPropertiesSet();

        //use one of the interfaces
        SubChild child = (SubChild) bean.getObject();

        try {
            child.childMethod();
            fail();
        }
        catch (NoServiceException e) {
            e.printStackTrace();
        }

        Child service = newChild();
        serviceRegistry.addService(null, "moduleName",  new StaticServiceBeanReference(service), Arrays.asList(exportTypes) , null, classLoader);
        child.childMethod();
        
        //show we can also cast to one of the other interfaces
        GrandChild grandChild = (GrandChild) child;
        grandChild.childMethod();
        
        assertFalse(grandChild instanceof TestChild);
    }
    
    private Child newChild() {
        return new TestChild();
    }
    
}

interface SubChild extends Child {
    
}

interface GrandChild extends Child {
    
}

class TestChild implements SubChild, GrandChild {
    public void childMethod() {
    }

    public Parent tryGetParent() {
        return null;
    }
}
