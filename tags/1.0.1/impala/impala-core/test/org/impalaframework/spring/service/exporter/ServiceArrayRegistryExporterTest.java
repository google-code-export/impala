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

package org.impalaframework.spring.service.exporter;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

public class ServiceArrayRegistryExporterTest extends TestCase {

    private ServiceArrayRegistryExporter exporter;
    private DelegatingServiceRegistry registry;
    private DefaultListableBeanFactory beanFactory;
    
    private String service1 = "myservice1";
    private String service2 = "myservice2";
    private Class<?>[] classes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        classes = new Class[]{String.class};
        exporter = new ServiceArrayRegistryExporter();
        registry = new DelegatingServiceRegistry();
        beanFactory = createMock(DefaultListableBeanFactory.class);
        exporter.setBeanFactory(beanFactory);
        exporter.setModuleDefinition(new SimpleModuleDefinition("module1"));
        exporter.setServiceRegistry(registry);
        exporter.setBeanClassLoader(ClassUtils.getDefaultClassLoader());
    }
    
    public void testGetBean() throws Exception {
        exporter.setBeanNames(new String[]{"myBean1","myBean2"});

        expectService1();
        expectService2();
        
        replay(beanFactory);
        exporter.afterPropertiesSet();
        verify(beanFactory);
        
        assertSame(service1, registry.getService("myBean1", classes, false).getServiceBeanReference().getService());
        assertSame(service2, registry.getService("myBean2", classes, false).getServiceBeanReference().getService());
        
        exporter.destroy();
        assertNull(registry.getService("myBean1", classes, false));
        assertNull(registry.getService("myBean2", classes, false));
    }
    
    public void testGetBeanWithExportNames() throws Exception {
        exporter.setBeanNames(new String[]{"myBean1","myBean2"});
        exporter.setExportNames(new String[]{"myExport1","myExport2"});

        expectService1();
        expectService2();
        
        replay(beanFactory);
        exporter.afterPropertiesSet();
        verify(beanFactory);
        
        assertSame(service1, registry.getService("myExport1", classes, false).getServiceBeanReference().getService());
        assertSame(service2, registry.getService("myExport2", classes, false).getServiceBeanReference().getService());
        
        exporter.destroy();
        assertNull(registry.getService("myExport1", classes, false));
        assertNull(registry.getService("myExport2", classes, false));
    }

    private void expectService2() {
        expect(beanFactory.containsBean("&myBean2")).andReturn(false);
        expect(beanFactory.containsBeanDefinition("myBean2")).andReturn(false);
        expect(beanFactory.getBean("myBean2")).andReturn(service2);
    }

    private void expectService1() {
        expect(beanFactory.containsBean("&myBean1")).andReturn(false);
        expect(beanFactory.containsBeanDefinition("myBean1")).andReturn(false);
        expect(beanFactory.getBean("myBean1")).andReturn(service1);
    }
    
}
