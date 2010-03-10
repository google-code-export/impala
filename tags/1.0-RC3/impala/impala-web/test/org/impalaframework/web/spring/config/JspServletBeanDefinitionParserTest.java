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

import java.util.Map;

import junit.framework.TestCase;

import org.apache.jasper.servlet.JspServlet;
import org.impalaframework.util.ObjectMapUtils;
import org.impalaframework.web.AttributeServletContext;
import org.impalaframework.web.jsp.JspServletFactoryBean;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class JspServletBeanDefinitionParserTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SuppressWarnings("unchecked")
    public void testServlet() throws Exception {
        
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.setServletContext(new AttributeServletContext());
        
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new ClassPathResource("org/impalaframework/web/spring/config/jspservletbeancontext.xml"));
        
        context.refresh();
        
        final Object factoryBean = context.getBean("&jspServlet");
        assertTrue(factoryBean instanceof JspServletFactoryBean);
        
        final Object bean = context.getBean("jspServlet");
        assertTrue(bean instanceof JspServlet);
        JspServlet servlet = (JspServlet) bean;
        assertEquals("jspServlet", servlet.getServletName());
        
        Map beans = context.getBeansOfType(ExtendedServletFactoryBean.class);
        assertEquals(1, beans.size());
        
        ExtendedServletFactoryBean extendedFactoryBean = (ExtendedServletFactoryBean) ObjectMapUtils.getFirstValue(beans);
        assertEquals("extraValue", extendedFactoryBean.getExtraAttribute());
        
        MyServlet2 myServlet = (MyServlet2) extendedFactoryBean.getObject();
        assertEquals("myServlet2", myServlet.getServletName());
    }
    
}
