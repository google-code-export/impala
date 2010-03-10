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

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.impalaframework.spring.module.ModuleDefinitionPostProcessor;
import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.Parent;
import org.impalaframework.spring.service.registry.config.ServiceRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Phil Zoio
 */
public class ParentWithChildContextTest extends TestCase {

    private ServiceRegistry serviceRegistry;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry = new DelegatingServiceRegistry();
    }
    
    public void testContexts() {

        ClassPathXmlApplicationContext parentContext = new ClassPathXmlApplicationContext(
                "childcontainer/parent-with-child-context.xml"){
             @Override
            protected DefaultListableBeanFactory createBeanFactory() {
                DefaultListableBeanFactory beanFactory = super.createBeanFactory();
                beanFactory.addBeanPostProcessor(new ServiceRegistryPostProcessor(serviceRegistry, null));
                return beanFactory;
            }
        };
        
        parentContext.getBean("parent");
        
        Parent parentBean = (Parent) parentContext.getBean("parent");
        Child child = parentBean.tryGetChild();

        try {
            child.childMethod();
            fail();
        }
        catch (NoServiceException e) {
        }

        // now create child appliction context
        ClassPathXmlApplicationContext childContext = new ClassPathXmlApplicationContext(
                new String[] { "childcontainer/child-context.xml" }, parentContext){
             @Override
                protected DefaultListableBeanFactory createBeanFactory() {
                    DefaultListableBeanFactory beanFactory = super.createBeanFactory();
                    beanFactory.addBeanPostProcessor(new ServiceRegistryPostProcessor(serviceRegistry, null));
                    beanFactory.addBeanPostProcessor(new ModuleDefinitionPostProcessor(new SimpleModuleDefinition("module1")));
                    return beanFactory;
                }
        };

        // bingo, child.childMethod does nto throw NoServiceException
        child.childMethod();

        // now create another context which depends only on the parent
        ApplicationContext another = childContext;

        // call method from child
        Child anotherChild = (Child) another.getBean("child");
        anotherChild.childMethod();

        // shutdown the child context
        childContext.close();

        // should go back to what it was doing before
        try {
            child.childMethod();
            fail();
        }
        catch (NoServiceException e) {
        }

    }
}
