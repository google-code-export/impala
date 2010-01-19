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

package org.impalaframework.spring.module.graph;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

import org.impalaframework.spring.module.graph.GraphDelegatingApplicationContext;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;

public class GraphDelegatingApplicationContextTest extends TestCase {

    private ApplicationContext delegate;
    private ApplicationContext dependencyOne;
    private ApplicationContext dependencyTwo;
    private GraphDelegatingApplicationContext parent;
    private List<ApplicationContext> dependencies;

    @Override
    protected void setUp() throws Exception {
        
        delegate = createMock(ApplicationContext.class);
        dependencyOne = createMock(ApplicationContext.class);
        dependencyTwo = createMock(ApplicationContext.class);
        
        dependencies = new ArrayList<ApplicationContext>();
        dependencies.add(dependencyOne);
        dependencies.add(dependencyTwo);
        parent = new GraphDelegatingApplicationContext(delegate, dependencies, true);
    }
    
    public void testNoDefinition() throws Exception {
        
        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(false);
        
        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(false);
        
        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(false);

        replay(delegate, dependencyOne, dependencyTwo);
        
        try {
            parent.getBean("bean");
            fail();
        } catch (NoSuchBeanDefinitionException e) {
        }

        try {
            parent.getBean("bean", String.class);
            fail();
        } catch (NoSuchBeanDefinitionException e) {
        }

        try {
            parent.getBean("bean", new String[0]);
            fail();
        } catch (NoSuchBeanDefinitionException e) {
        }

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testWithDependencies() throws Exception {
        
        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(true);
        expect(dependencyOne.getBean("bean")).andReturn("value");
        
        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(true);
        expect(dependencyTwo.getBean("bean", String.class)).andReturn("value");
        
        expect(delegate.containsBean("bean")).andReturn(false);
        expect(dependencyOne.containsBeanDefinition("bean")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("bean")).andReturn(true);
        expect(dependencyTwo.getBean(eq("bean"), aryEq(new String[0]))).andReturn("value");

        replay(delegate, dependencyOne, dependencyTwo);
        
        parent.getBean("bean");
        parent.getBean("bean", String.class);
        parent.getBean("bean", new String[0]);

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testGetContainingApplicationContextFromParent() throws Exception {
        
        expect(delegate.containsBeanDefinition("beanName")).andReturn(true);
        
        replay(delegate, dependencyOne, dependencyTwo);
        
        parent.getContainingApplicationContext("beanName");

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testGetContainingApplicationContextFromDelegate() throws Exception {
        
        expect(delegate.containsBeanDefinition("beanName")).andReturn(false);
        expect(delegate.getParent()).andReturn(null);
        expect(dependencyOne.containsBeanDefinition("beanName")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("beanName")).andReturn(true);
        
        replay(delegate, dependencyOne, dependencyTwo);
        
        assertSame(dependencyTwo, parent.getContainingApplicationContext("beanName"));

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testGetContainingApplicationNotParent() throws Exception {
        
        parent = new GraphDelegatingApplicationContext(delegate, dependencies, false);
        
        expect(dependencyOne.containsBeanDefinition("beanName")).andReturn(false);
        expect(dependencyTwo.containsBeanDefinition("beanName")).andReturn(true);
        
        replay(delegate, dependencyOne, dependencyTwo);
        
        assertSame(dependencyTwo, parent.getContainingApplicationContext("beanName"));

        verify(delegate, dependencyOne, dependencyTwo);
    }
    
    public void testDelegatingMethods() throws Exception {
        
        expect(delegate.containsBean("bean")).andReturn(true);
        expect(delegate.getBean("bean")).andReturn("value");
        
        expect(delegate.containsBean("bean")).andReturn(true);
        expect(delegate.getBean("bean", String.class)).andReturn("value");
        
        expect(delegate.containsBean("bean")).andReturn(true);
        expect(delegate.getBean(eq("bean"), aryEq(new String[0]))).andReturn("value");
        
        expect(delegate.containsBean("bean")).andReturn(true);
        expect(delegate.containsBeanDefinition("bean")).andReturn(true);
        expect(delegate.containsLocalBean("bean")).andReturn(true);
        expect(delegate.getAliases("bean")).andReturn(new String[0]);
        expect(delegate.getAutowireCapableBeanFactory()).andReturn(null);
        expect(delegate.getBeanDefinitionCount()).andReturn(0);
        expect(delegate.getBeanDefinitionNames()).andReturn(null);
        expect(delegate.getBeanNamesForType(String.class)).andReturn(null);
        expect(delegate.getBeanNamesForType(String.class, true, false)).andReturn(null);
        expect(delegate.getClassLoader()).andReturn(null);
        expect(delegate.getMessage(isA(MessageSourceResolvable.class), eq(Locale.getDefault()))).andReturn(null);
        expect(delegate.getMessage(eq("code"), aryEq(new String[0]), eq(Locale.getDefault()))).andReturn(null);
        expect(delegate.getMessage(eq("code"), aryEq(new String[0]), eq("default"), eq(Locale.getDefault()))).andReturn(null);
        expect(delegate.getParentBeanFactory()).andReturn(null);
        expect(delegate.getResource("location")).andReturn(null);
        expect(delegate.getResources("locationPattern")).andReturn(null);
        expect(delegate.getType("type")).andReturn(null);
        expect(delegate.isPrototype("bean")).andReturn(true);
        expect(delegate.isSingleton("bean")).andReturn(true);
        expect(delegate.isTypeMatch("bean", String.class)).andReturn(true);
        delegate.publishEvent(isA(ApplicationEvent.class));
        
        replay(delegate, dependencyOne, dependencyTwo);

        parent.getBean("bean");
        parent.getBean("bean", String.class);
        parent.getBean("bean", new String[0]);
        
        parent.containsBean("bean");
        parent.containsBeanDefinition("bean");
        parent.containsLocalBean("bean");
        parent.getAliases("bean");
        parent.getAutowireCapableBeanFactory();
        parent.getBeanDefinitionCount();
        parent.getBeanDefinitionNames();
        parent.getBeanNamesForType(String.class);
        parent.getBeanNamesForType(String.class, true, false);
        parent.getClassLoader();
        parent.getDisplayName();
        parent.getMessage(createMock(MessageSourceResolvable.class), Locale.getDefault());
        parent.getMessage("code", new String[0], Locale.getDefault());
        parent.getMessage("code", new String[0], "default", Locale.getDefault());
        assertSame(delegate, parent.getParent());
        parent.getParentBeanFactory();
        parent.getResource("location");
        parent.getResources("locationPattern");
        parent.getType("type");
        parent.isPrototype("bean");
        parent.isSingleton("bean");
        parent.isTypeMatch("bean", String.class);
        parent.publishEvent(createMock(ApplicationEvent.class));
        
        assertNotNull(parent.getId());
        assertTrue(parent.getStartupDate() > 0);
        
        verify(delegate, dependencyOne, dependencyTwo);
    }
    
}
