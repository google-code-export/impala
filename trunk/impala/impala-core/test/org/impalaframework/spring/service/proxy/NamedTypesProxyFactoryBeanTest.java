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
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.Parent;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class NamedTypesProxyFactoryBeanTest extends TestCase {

    private NamedTypesProxyFactoryBean bean;
    private ServiceRegistryImpl serviceRegistry;
    private ClassLoader classLoader;
    private Class<?>[] exportTypes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bean = new NamedTypesProxyFactoryBean();
        serviceRegistry = new ServiceRegistryImpl();
        bean.setServiceRegistry(serviceRegistry);
        
        classLoader = ClassUtils.getDefaultClassLoader();
        exportTypes = new Class<?>[] { Child.class };
        bean.setBeanName("mybean");
    }
    
    public void testWithBeanName() throws Exception {
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

        Child newChild = newChild();
        serviceRegistry.addService(null, "pluginName", newChild, Arrays.asList(exportTypes) , null, classLoader);
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
