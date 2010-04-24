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

package org.impalaframework.spring.module.registry;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.springframework.beans.factory.BeanFactory;

public class RegistryContributorTest extends TestCase {

    private NamedBeanRegistryContributor contributor;
    private BeanFactory beanFactory;
    private ModuleLoader moduleLoader1;
    private ModuleLoader moduleLoader2;
    private ModuleLoaderRegistry moduleLoaderRegistry;
    private TypeReaderRegistry typeReaderRegistry;
    
    public void setUp() {
        moduleLoaderRegistry = new ModuleLoaderRegistry();
        typeReaderRegistry = new TypeReaderRegistry();
        
        contributor = new NamedBeanRegistryContributor();
        
        contributor.setRegistryBeanName("myRegistry");
        contributor.setOrder(1);
        beanFactory = createMock(BeanFactory.class);
        
        Map<String,String> contributions = new LinkedHashMap<String, String>();
        contributions.put("contribution1", "bean1");
        contributions.put("contribution2", "bean2");
        
        moduleLoader1 = createMock(ModuleLoader.class);
        moduleLoader2 = createMock(ModuleLoader.class);
        
        contributor.setBeanFactory(beanFactory);
        contributor.setContributions(contributions);
    }
    
    public void testOrder() throws Exception {
        assertEquals(1, contributor.getOrder());
    }
    
    public void testDoContributions() throws Exception {
        expect(beanFactory.getBean("myRegistry")).andReturn(moduleLoaderRegistry);
        expect(beanFactory.getBean("contribution1")).andReturn(moduleLoader1);
        expect(beanFactory.getBean("contribution2")).andReturn(moduleLoader2);
        
        replay(beanFactory);
        
        contributor.doContributions();
        assertEquals(2, moduleLoaderRegistry.getEntries().size());
        
        verify(beanFactory);
    }
    
    public void testDoWithDifferentRegistry() throws Exception {
        expect(beanFactory.getBean("myRegistry")).andReturn(typeReaderRegistry);
        expect(beanFactory.getBean("contribution1")).andReturn(moduleLoader1);
        
        replay(beanFactory);
        
        try {
            contributor.doContributions();
            fail();
        } catch (ConfigurationException e) {
            assertEquals("Bean 'contribution1' is not type compatible with registry bean 'myRegistry'", e.getMessage());
        }
        
        verify(beanFactory);
    }

}
