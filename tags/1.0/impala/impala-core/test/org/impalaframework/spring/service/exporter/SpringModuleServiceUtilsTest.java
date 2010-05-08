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

import org.easymock.EasyMock;
import org.impalaframework.exception.ExecutionException;
import org.impalaframework.service.NamedServiceEndpoint;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringModuleServiceUtilsTest extends TestCase {

    public final void testFindServiceEndPointPresent() {
        ClassPathXmlApplicationContext childOfChild = getContext("contribution/root-with-definition.xml")[0];

        //true because applicationContext is not an instanceof AbstractBeanFactory
        assertNotNull(SpringModuleServiceUtils.findServiceEndpoint(childOfChild, "child"));
        assertNotNull(SpringModuleServiceUtils.findServiceEndpoint(childOfChild.getBeanFactory(), "child"));
    }

    public final void testFindServiceEndPointNotPresent() {
        ClassPathXmlApplicationContext childOfChild = getContext("contribution/root-no-definition.xml")[0];

        NamedServiceEndpoint endPoint = SpringModuleServiceUtils.findServiceEndpoint(childOfChild.getBeanFactory(),
                "child");
        assertNull(endPoint);
    }

    public final void testGetRootBeanFactory() {
        ClassPathXmlApplicationContext[] contexts = getContext("contribution/root-with-definition.xml");
        ClassPathXmlApplicationContext parent = contexts[2];
        assertSame(parent, SpringModuleServiceUtils.getRootBeanFactory(contexts[0]));
        assertSame(parent, SpringModuleServiceUtils.getRootBeanFactory(contexts[1]));
        assertSame(parent, SpringModuleServiceUtils.getRootBeanFactory(contexts[2]));        
    }

    public final void testGetInvalidRootBeanFactory() {
        try {
            SpringModuleServiceUtils.getRootBeanFactory(EasyMock.createMock(BeanFactory.class));
            fail();
        }
        catch (ExecutionException e) {
            String message = e.getMessage();
            assertTrue(message.contains("BeanFactory EasyMock for interface org.springframework.beans.factory.BeanFactory is of type"));
            assertTrue(message.contains(", which is not an instance of org.springframework.beans.factory.HierarchicalBeanFactory"));
        }       
    }
    
    private ClassPathXmlApplicationContext[] getContext(String rootName) {
        ClassPathXmlApplicationContext parent = new ClassPathXmlApplicationContext(rootName);
        ClassPathXmlApplicationContext child = new ClassPathXmlApplicationContext(
                new String[] { "contribution/child.xml" }, parent);
        ClassPathXmlApplicationContext childOfChild = new ClassPathXmlApplicationContext(
                new String[] { "contribution/array-exporter.xml" }, child);
        return new ClassPathXmlApplicationContext[] {childOfChild, child, parent};
    }

}
