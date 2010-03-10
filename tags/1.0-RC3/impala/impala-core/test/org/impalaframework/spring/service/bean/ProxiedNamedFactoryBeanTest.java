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

package org.impalaframework.spring.service.bean;

import junit.framework.TestCase;

import org.impalaframework.spring.module.impl.ParentBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Phil Zoio
 */
public class ProxiedNamedFactoryBeanTest extends TestCase {
    
    private ClassPathXmlApplicationContext parentContext;
    private ClassPathXmlApplicationContext childContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        parentContext = new ClassPathXmlApplicationContext("childcontainer/parent-factory.xml");
        childContext = new ClassPathXmlApplicationContext(new String[]{"childcontainer/child-factory.xml"}, parentContext);
    }

    public void testNamedFactoryBean() throws Exception {
        Object namedBean = childContext.getBean("namedBean");
        assertEquals(ParentBean.class.getName(), namedBean.getClass().getName());
        
        Object proxiedNamedBean = childContext.getBean("proxiedNamedBean");
        assertFalse(ParentBean.class.getName().equals(proxiedNamedBean.getClass().getName()));
        assertTrue(proxiedNamedBean instanceof ParentBean);
        
        ParentBean proxied = (ParentBean) proxiedNamedBean;
        System.out.println(proxied.getInstance());
        
        ParentBean next = (ParentBean) childContext.getBean("proxiedNamedBean");
        System.out.println(next.getInstance());
    }
    
}
