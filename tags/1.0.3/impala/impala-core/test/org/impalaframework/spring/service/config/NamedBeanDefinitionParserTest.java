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

package org.impalaframework.spring.service.config;

import junit.framework.TestCase;

import org.impalaframework.spring.service.bean.ProxiedNamedFactoryBean;
import org.impalaframework.spring.service.bean.SingletonAwareNamedFactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NamedBeanDefinitionParserTest extends TestCase {

    public void testNamedBean() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/impalaframework/spring/service/config/named-bean-context.xml");
        
        doBasicTest(context);
        doSingletonFactoryBeanTest(context);
        doPrototypeFactoryBeanTest(context);
        doProxiedFactoryBeanTest(context);
    }

    private void doBasicTest(ClassPathXmlApplicationContext context) {
        final Object singletonHolder = context.getBean("singletonHolder");
        assertNotNull(singletonHolder);
        assertNotNull(context.getBean("prototypeHolder"));
        
        final Object namedHolder = context.getBean("namedSingletonHolder");
        assertSame(namedHolder, singletonHolder);
    }

    private void doProxiedFactoryBeanTest(ClassPathXmlApplicationContext context) {
        final Object bean = context.getBean("&proxiedPrototypeHolder");
        assertTrue(bean instanceof ProxiedNamedFactoryBean);
        ProxiedNamedFactoryBean factoryBean = (ProxiedNamedFactoryBean) bean;
        assertFalse(factoryBean.isSingleton());
        
        final IntegerHolder proxiedHolder = (IntegerHolder) context.getBean("proxiedPrototypeHolder");
        final int number1 = proxiedHolder.getNumber();
        System.out.println(number1);
        final int number2 = proxiedHolder.getNumber();
        System.out.println(number2);
        assertEquals(1, number2 - number1);
    }

    private void doSingletonFactoryBeanTest(ClassPathXmlApplicationContext context) {
        final Object bean = context.getBean("&singletonAwareSingletonHolder");
        
        assertTrue(bean instanceof SingletonAwareNamedFactoryBean);
        SingletonAwareNamedFactoryBean factoryBean = (SingletonAwareNamedFactoryBean) bean;
        assertTrue(factoryBean.isSingleton());
    }

    private void doPrototypeFactoryBeanTest(ClassPathXmlApplicationContext context) {
        final Object bean = context.getBean("&singletonAwarePrototypeHolder");
        
        assertTrue(bean instanceof SingletonAwareNamedFactoryBean);
        SingletonAwareNamedFactoryBean factoryBean = (SingletonAwareNamedFactoryBean) bean;
        assertFalse(factoryBean.isSingleton());
    }
    
}


class IntegerHolder {
    private static int count;
    private int number;
    
    public IntegerHolder() {
        super();
        count++;
        number = count;
    }
    
    public int getNumber() {
        return number;
    }
    
}
