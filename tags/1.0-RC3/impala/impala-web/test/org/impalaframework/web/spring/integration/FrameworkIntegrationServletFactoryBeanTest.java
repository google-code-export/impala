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

package org.impalaframework.web.spring.integration;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.util.CollectionStringUtils;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

public class FrameworkIntegrationServletFactoryBeanTest extends TestCase {

    private FrameworkIntegrationServletFactoryBean factoryBean;
    private ServletContext servletContext;
    private ModuleDefinition moduleDefinition;
    private WebApplicationContext applicationContext;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        factoryBean = new FrameworkIntegrationServletFactoryBean();
        servletContext = createMock(ServletContext.class);
        moduleDefinition = createMock(ModuleDefinition.class);
        applicationContext = createMock(WebApplicationContext.class);
        factoryBean.setServletContext(servletContext);
        factoryBean.setServletClass(TestFrameworkServlet.class);
        factoryBean.setModuleDefinition(moduleDefinition);
        factoryBean.setApplicationContext(applicationContext);
    }

    public void testDefaults() {
        assertEquals(InternalFrameworkIntegrationServlet.class, factoryBean.getObjectType());
        assertEquals(true, factoryBean.isSingleton());
    }
    
    public void testAfterPropertiesSetDefault() throws Exception {
        
        expect(moduleDefinition.getName()).andReturn("servletName");
        servletContext.setAttribute("someattribute", applicationContext);
        servletContext.log(isA(String.class));
        //expect(servletContext.getAttribute("someattribute")).andReturn(applicationContext);
        
        replay(servletContext);
        replay(moduleDefinition);
        
        factoryBean.afterPropertiesSet();
        assertTrue(factoryBean.getObject() instanceof InternalFrameworkIntegrationServlet);
    
        verify(moduleDefinition);
        verify(servletContext);
    }
    
    public void testAfterPropertiesSet() throws Exception {
        
        factoryBean.setServletName("myServlet");
        factoryBean.setInitParameters(CollectionStringUtils.parsePropertiesFromString("contextAttribute=myattribute"));
       
        servletContext.setAttribute("myattribute", applicationContext);
        servletContext.log(isA(String.class));
        
        replay(servletContext);
        replay(moduleDefinition);
        
        factoryBean.afterPropertiesSet();
        assertTrue(factoryBean.getObject() instanceof InternalFrameworkIntegrationServlet);
    
        verify(moduleDefinition);
        verify(servletContext);
    }

}

class TestFrameworkServlet extends FrameworkServlet {

    private static final long serialVersionUID = 1L;
    
    public TestFrameworkServlet() {
        super();
    }

    @Override
    protected WebApplicationContext initWebApplicationContext()
            throws BeansException {
        return null;
    }

    @Override
    protected void initFrameworkServlet() throws ServletException,
            BeansException {
    }

    @Override
    protected void doService(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    }
}
