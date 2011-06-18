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

import java.util.Collections;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

public class ServiceRegistryExporterTest extends TestCase {

    private ServiceRegistryExporter exporter;
    private DelegatingServiceRegistry registry;
    private DefaultListableBeanFactory beanFactory;
    
    private String service = "myservice";
    private Class<?>[] classes;
    private Class<?>[] exportTypes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        classes = new Class[]{String.class};
        exporter = new ServiceRegistryExporter();
        registry = new DelegatingServiceRegistry();
        beanFactory = createMock(DefaultListableBeanFactory.class);
        exporter.setBeanFactory(beanFactory);
        exporter.setModuleDefinition(new SimpleModuleDefinition("module1"));
        exporter.setServiceRegistry(registry);
        exportTypes = new Class<?>[]{String.class, Object.class};
        exporter.setExportTypes(exportTypes);
        exporter.setBeanClassLoader(ClassUtils.getDefaultClassLoader());
    }
    
    public void testGetBean() throws Exception {
        exporter.setBeanName("myBean");
        //no export name set, but no export types set either

        expectBeanFactoryWhenGettingReference();
        
        replay(beanFactory);
        exporter.afterPropertiesSet();
        verify(beanFactory);

        assertNull(registry.getService("myBean", classes, false));
        assertSame(service, registry.getService(null, classes, true).getServiceBeanReference().getService());
        
        exporter.destroy();
        assertNull(registry.getService("myBean", classes, false));
    }
    
    public void testInvalidType() throws Exception {
        exporter.setBeanName("myBean");

        exportTypes = new Class<?>[]{String.class, Object.class, Integer.class};
        exporter.setExportTypes(exportTypes);
        try {
            expectBeanFactoryWhenGettingReference();
            replay(beanFactory);
            exporter.afterPropertiesSet();
            fail();
        } catch (InvalidStateException e) {
        }
    }
    
    public void testGetBeanAlternative() throws Exception {
        exporter.setBeanName("myBean");
        exporter.setExportName("exportName");

        expectBeanFactoryWhenGettingReference();
        
        replay(beanFactory);
        exporter.afterPropertiesSet();
        verify(beanFactory);
        
        ServiceRegistryEntry serviceReference = registry.getService("exportName", classes, false);
        assertSame(service, serviceReference.getServiceBeanReference().getService());
        
        exporter.destroy();
        assertNull(registry.getService("exportName", classes, false));
    }
    
    public void testBeanNameOnly() throws Exception {
        exporter.setBeanName("myBean");
        exporter.setExportTypes(null);

        expectBeanFactoryWhenGettingReference();
        
        replay(beanFactory);
        exporter.afterPropertiesSet();
        verify(beanFactory);
        
        ServiceRegistryEntry serviceReference = registry.getService("myBean", classes, false);
        assertSame(service, serviceReference.getServiceBeanReference().getService());
        
        exporter.destroy();
        assertNull(registry.getService("myBean", classes, false));
    }
    
    public void testTagsAndAttribute() throws Exception {
        exporter.setBeanName("myBean");
        Map<String, ? extends Object> attributes = Collections.singletonMap("attribute1", "value1");
        exporter.setAttributeMap(attributes);

        expectBeanFactoryWhenGettingReference();
        
        replay(beanFactory);
        exporter.afterPropertiesSet();
        verify(beanFactory);
        
        ServiceRegistryEntry s = registry.getService(null, classes, true);
        assertSame(service, s.getServiceBeanReference().getService());
        assertEquals(attributes, s.getAttributes());
        
        assertFalse(registry.getServices(new LdapServiceReferenceFilter("(attribute1=*)"), classes, false).isEmpty());
        assertFalse(registry.getServices(new LdapServiceReferenceFilter("(attribute1=*)"), classes, true).isEmpty());
        
        exporter.destroy();
        assertNull(registry.getService("myBean", classes, false));
    }

    private void expectBeanFactoryWhenGettingReference() {
        expect(beanFactory.containsBean("&myBean")).andReturn(false);
        expect(beanFactory.containsBeanDefinition("myBean")).andReturn(false);
        expect(beanFactory.getBean("myBean")).andReturn(service);
    }
    
}
