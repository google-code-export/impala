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
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.reset;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.util.ObjectMapUtils;
import org.impalaframework.web.listener.ServletContextListenerFactoryBean;
import org.impalaframework.web.servlet.invocation.ModuleHttpServiceInvoker;
import org.impalaframework.web.servlet.invocation.ModuleInvokerContributor;
import org.impalaframework.web.spring.integration.FilterFactoryBean;
import org.impalaframework.web.spring.integration.ModuleHttpServiceInvokerBuilder;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;

public class ModuleHttpServiceInvokerBuilderTest extends TestCase {
    
    private ModuleHttpServiceInvokerBuilder builder;
    private ListableBeanFactory beanFactory;
    private ServletContext servletContext;
    private ModuleInvokerContributor moduleInvokerContributor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        builder = new ModuleHttpServiceInvokerBuilder();
        beanFactory = createMock(ListableBeanFactory.class);
        builder.setBeanFactory(beanFactory);
        builder.setModuleDefinition(new SimpleModuleDefinition("mymodule"));
        servletContext = createMock(ServletContext.class);
        builder.setServletContext(servletContext);
        moduleInvokerContributor = new ModuleInvokerContributor();
        moduleInvokerContributor.setSuffix("exe");

        expect(beanFactory.getBeansOfType(ServletContextListenerFactoryBean.class)).andReturn(null);
    }

    public void testBuilder() throws Exception {
        moduleInvokerContributor.setFilterNames(new String[]{"myfilter"});
        moduleInvokerContributor.setServletName("myservlet");
        
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap("f1", newFilter("myfilter")));
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap("s1", newServlet("myservlet")));
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap("c", moduleInvokerContributor));
        servletContext.setAttribute(eq("shared:"+ModuleHttpServiceInvoker.class.getName()+"."+"mymodule"), isA(ModuleHttpServiceInvoker.class));

        replay(beanFactory, servletContext);
        builder.afterPropertiesSet();
        verify(beanFactory, servletContext);
    }
    
    public void testDestroy() throws Exception {
        reset(beanFactory);
        
        servletContext.removeAttribute("shared:"+ModuleHttpServiceInvoker.class.getName()+"."+"mymodule");
        replay(beanFactory, servletContext);
        builder.destroy();
        verify(beanFactory, servletContext);
    }
    
    public void testInvoker() throws Exception {
        moduleInvokerContributor.setFilterNames(new String[]{"myfilter"});
        moduleInvokerContributor.setServletName("myservlet");
        
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap("f1", newFilter("myfilter")));
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap("s1", newServlet("myservlet")));
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap("c", moduleInvokerContributor));

        replay(beanFactory, servletContext);
        ModuleHttpServiceInvoker invoker = builder.buildInvoker();
        assertFalse(invoker.getFilters().isEmpty());
        assertFalse(invoker.getServlets().isEmpty());
        
        verify(beanFactory, servletContext);
    }
    
    public void testMissingFilter() throws Exception {
        moduleInvokerContributor.setFilterNames(new String[]{"myfilter"});
        
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap("f1", newFilter("anotherfilter")));
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap());
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap("c", moduleInvokerContributor));

        replay(beanFactory, servletContext);
        
        try {
            builder.buildInvoker();
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("Suffix 'exe' has mapping for filter 'myfilter' for which no named filter definition is present in the current module.", e.getMessage());
        }
        
        verify(beanFactory, servletContext);
    }
    
    public void testMissingServlet() throws Exception {
        moduleInvokerContributor.setServletName("myservlet");
        
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap());
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap("s1", newServlet("anotherservlet")));
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap("c", moduleInvokerContributor));

        replay(beanFactory, servletContext);
        
        try {
            builder.buildInvoker();
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("Suffix 'exe' has mapping for servlet 'myservlet' for which no named servlet definition is present in the current module.", e.getMessage());
        }
        
        verify(beanFactory, servletContext);
    }
    
    public void testNoContributionsWithModuleNameServlet() throws Exception {
        //note that this one gets ignored
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap("f1", newFilter("mymodule")));
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap("s1", newServlet("mymodule")));
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap());

        replay(beanFactory, servletContext);
        ModuleHttpServiceInvoker invoker = builder.buildInvoker();
        assertEquals(0, invoker.getFilters().size());
        assertEquals(1, invoker.getServlets().size());
        
        verify(beanFactory, servletContext);
    }
    
    public void testNoContributionsWithModuleNameFilter() throws Exception {
        //note that this one gets ignored
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap("f1", newFilter("mymodule")));
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap("s1", newServlet("someservlet")));
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap());

        replay(beanFactory, servletContext);
        ModuleHttpServiceInvoker invoker = builder.buildInvoker();
        assertEquals(1, invoker.getFilters().size());
        assertEquals(0, invoker.getServlets().size());
        
        verify(beanFactory, servletContext);
    }

    public void testNoContributionsWithSingleServlet() throws Exception {
        //note that this one gets ignored
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap("f1", newFilter("somefilter")));
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap("s1", newServlet("someservlet")));
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap());

        replay(beanFactory, servletContext);
        ModuleHttpServiceInvoker invoker = builder.buildInvoker();
        assertEquals(0, invoker.getFilters().size());
        assertEquals(1, invoker.getServlets().size());
        
        verify(beanFactory, servletContext);
    }

    public void testNoContributionsWithSingleFilter() throws Exception {
        //note that this one gets ignored
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap("f1", newFilter("somefilter")));
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap());
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap());

        replay(beanFactory, servletContext);
        ModuleHttpServiceInvoker invoker = builder.buildInvoker();
        assertEquals(1, invoker.getFilters().size());
        assertEquals(0, invoker.getServlets().size());
        
        verify(beanFactory, servletContext);
    }

    public void testNoContributionsWithMultipleServlet() throws Exception {
        //note that this one gets ignored
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap());
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap("s1", newServlet("someservlet"), "s2", newServlet("anotherservlet")));
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap());

        replay(beanFactory, servletContext);        
        
        try {
            builder.buildInvoker();
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("Cannot determine default servlet for module 'mymodule' as more than one servlet is registered for this module.", e.getMessage());
        }
        verify(beanFactory, servletContext);
    }

    public void testNoContributionsWithMultipleFilters() throws Exception {
        //note that this one gets ignored
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap("f1", newFilter("somefilter"), "f2", newFilter("anotherfilter")));
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap());
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap());

        replay(beanFactory, servletContext);        
        
        try {
            builder.buildInvoker();
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("Cannot determine default filter for module 'mymodule' as more than one filter is registered for this module.", e.getMessage());
        }
        verify(beanFactory, servletContext);
    }
    
    public void testNoContributions() throws Exception {
        //just logs a warning
        
        expect(beanFactory.getBeansOfType(FilterFactoryBean.class)).andReturn(ObjectMapUtils.newMap());
        expect(beanFactory.getBeansOfType(ServletFactoryBean.class)).andReturn(ObjectMapUtils.newMap());
        expect(beanFactory.getBeansOfType(ModuleInvokerContributor.class)).andReturn(ObjectMapUtils.newMap());

        replay(beanFactory, servletContext);
        ModuleHttpServiceInvoker invoker = builder.buildInvoker();
        assertEquals(0, invoker.getFilters().size());
        assertEquals(0, invoker.getServlets().size());
        
        verify(beanFactory, servletContext);
    }

    private ServletFactoryBean newServlet(String name) {
        ServletFactoryBean servlet = new ServletFactoryBean();
        servlet.setServletName(name);
        return servlet;
    }

    private FilterFactoryBean newFilter(String name) {
        FilterFactoryBean filter = new FilterFactoryBean();
        filter.setFilterName(name);
        return filter;
    }
    
}
