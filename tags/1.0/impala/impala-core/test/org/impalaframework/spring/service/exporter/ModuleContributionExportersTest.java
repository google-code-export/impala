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
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.impalaframework.spring.module.impl.Child;
import org.impalaframework.spring.module.impl.ChildBean;
import org.impalaframework.spring.service.registry.config.ServiceRegistryPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ModuleContributionExportersTest extends TestCase {

    private ServiceRegistry serviceRegistry;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry = new DelegatingServiceRegistry();
    }
    
    public final void testAutoRegisteringExporterWithNoDefinition() {
        doTest("contribution/root-no-definition.xml", "contribution/autoregistering-exporter.xml");
    }

    public final void testAutoRegisteringExporterWithDefinition() {
        doTest("contribution/root-with-definition.xml", "contribution/autoregistering-exporter.xml");
    }

    public final void testModuleArrayExporterWithNoDefinition() {
        try {
            doTest("contribution/root-no-definition.xml", "contribution/array-exporter.xml");
            fail();
        }
        catch (NoSuchBeanDefinitionException e) {
            System.out.println(e.getMessage());
        }
    }

    public final void testModuleArrayExporterWithDefinition() {
        doTest("contribution/root-with-definition.xml", "contribution/array-exporter.xml");
    }

    private void doTest(String rootDefinition, String childDefinition) {
        TestContext parent = new TestContext(serviceRegistry, new String[] {rootDefinition}, null);
        TestContext child = new TestContext(serviceRegistry,
                new String[] { "contribution/child.xml" }, parent);
        TestContext childOfChild = new TestContext(serviceRegistry,
                new String[] { childDefinition }, child);

        checkInitialized(parent, childOfChild, "child");
        checkInitialized(parent, childOfChild, "another");
        
        //now destroy, and check that bean has been deregistered
        childOfChild.destroy();

        checkDestroyed(parent, "child");
        checkDestroyed(parent, "another");
        
        //restart
        childOfChild = new TestContext(serviceRegistry,
                new String[] { childDefinition }, child);
        
        checkInitialized(parent, childOfChild, "child");
        checkInitialized(parent, childOfChild, "another");
        
    }

    private void checkDestroyed(ClassPathXmlApplicationContext parent, String beanName) {
        Child bean = (Child) parent.getBean(beanName);
        try {
            bean.childMethod();fail();
        }
        catch (NoServiceException e) {
            assertEquals("No service available for bean " + beanName, e.getMessage());
        }
    }

    private void checkInitialized(ApplicationContext parent, ClassPathXmlApplicationContext childOfChild, String beanName) {
        Object beanFromChild = childOfChild.getBean(beanName);
        Object beanFromRoot = parent.getBean(beanName);

        assertTrue(beanFromChild instanceof ChildBean);
        assertTrue(beanFromRoot instanceof Child);
        assertFalse(beanFromRoot instanceof ChildBean);
        
        Child bean = (Child) parent.getBean(beanName);
        bean.childMethod();
    }
}

class TestContext extends ClassPathXmlApplicationContext {
        
    private ServiceRegistry serviceRegistry;
        
     public TestContext(ServiceRegistry serviceRegistry, String[] configLocations, ApplicationContext parent)
            throws BeansException {
        super(configLocations, false, parent);
        this.serviceRegistry = serviceRegistry;
        refresh();
    }

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory beanFactory = super.createBeanFactory();
        beanFactory.addBeanPostProcessor(new ServiceRegistryPostProcessor(serviceRegistry, null));
        return beanFactory;
    }
};



