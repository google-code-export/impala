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

package org.impalaframework.web.spring.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.impalaframework.web.AttributeServletContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.GenericWebApplicationContext;

import junit.framework.TestCase;

public class ContextListenerBeanDefinitionParserTest extends TestCase {

    public void testParser() {
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.setServletContext(new AttributeServletContext());
        
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new ClassPathResource("org/impalaframework/web/spring/config/listenerbeancontext.xml"));
        
        context.refresh();
        
        TestContextListener listener = (TestContextListener) context.getBean("listener");
        assertTrue(listener.isInitialized());
        assertFalse(listener.isDestroyed());
        
        context.close();
        assertTrue(listener.isDestroyed());
    }

}

class TestContextListener implements ServletContextListener {
    
    private boolean initialized;
    private boolean destroyed;

    public void contextInitialized(ServletContextEvent event) {
        this.initialized = true;
    }

    public void contextDestroyed(ServletContextEvent event) {
        this.destroyed = true;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
}
