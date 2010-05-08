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

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;

import org.impalaframework.util.ObjectMapUtils;
import org.impalaframework.web.AttributeServletContext;
import org.impalaframework.web.spring.integration.FilterFactoryBean;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationFilterFactoryBean;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class FilterBeanDefinitionParserTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testFilter() throws Exception {
        
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.setServletContext(new AttributeServletContext());
        
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new ClassPathResource("org/impalaframework/web/spring/config/filterbeancontext.xml"));
        
        context.refresh();
        
        assertEquals("initValue", MyFilter2.getConfigParam());
        
        Map factoryBeans = context.getBeansOfType(FilterFactoryBean.class);
        assertEquals(3, factoryBeans.size());
        
        Map s = context.getBeansOfType(Filter.class);
        assertEquals(3, s.size());
        
        Map beans = context.getBeansOfType(InternalFrameworkIntegrationFilterFactoryBean.class);
        assertEquals(1, beans.size());
        
        InternalFrameworkIntegrationFilterFactoryBean firstValue = (InternalFrameworkIntegrationFilterFactoryBean) ObjectMapUtils.getFirstValue(beans);
        assertEquals("delegator", firstValue.getFilterName());

        beans = context.getBeansOfType(ExtendedFilterFactoryBean.class);
        assertEquals(1, beans.size());
        
        ExtendedFilterFactoryBean extendedFactoryBean = (ExtendedFilterFactoryBean) ObjectMapUtils.getFirstValue(beans);
        assertEquals("extraValue", extendedFactoryBean.getExtraAttribute());
        
        MyFilter2 myFilter = (MyFilter2) extendedFactoryBean.getObject();
       assertNotNull(myFilter);
    }
    
}

class MyFilter1 implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest request, 
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    }

    public void init(FilterConfig config) throws ServletException {
    }
}

class MyFilter2 implements Filter {
    
    private static String configParam;

    private static final long serialVersionUID = 1L;
    
    public void init(FilterConfig config) throws ServletException {
        configParam = config.getInitParameter("initParam");
    }
    
    public void doFilter(ServletRequest request, 
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    }
    
    public void destroy() {
    }
    
    public static String getConfigParam() {
        return configParam;
    }
    
    public static void reset() {
        configParam = null;
    }
}

class ExtendedFilterFactoryBean extends FilterFactoryBean {
    
    private String extraAttribute;

    public String getExtraAttribute() {
        return extraAttribute;
    }

    public void setExtraAttribute(String extraAttribute) {
        this.extraAttribute = extraAttribute;
    }
}
