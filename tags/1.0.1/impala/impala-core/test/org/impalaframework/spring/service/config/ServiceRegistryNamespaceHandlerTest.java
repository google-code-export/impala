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

import static org.easymock.classextension.EasyMock.*;

import org.impalaframework.spring.service.proxy.FilteredServiceProxyFactoryBean;
import org.impalaframework.spring.service.proxy.NamedServiceProxyFactoryBean;
import org.impalaframework.spring.service.proxy.TypedServiceProxyFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

import junit.framework.TestCase;

public class ServiceRegistryNamespaceHandlerTest extends TestCase {
    
    private ServiceRegistryNamespaceHandler.ImportBeanDefinitionParser importParser;
    private Element element;
    private BeanDefinitionBuilder builder;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        importParser = new ServiceRegistryNamespaceHandler.ImportBeanDefinitionParser();
        element = createMock(Element.class);
        builder = createMock(BeanDefinitionBuilder.class);
    }

    public void testNoAttributes() throws Exception {
        
        expectAttribute("proxyTypes", "mytypes");
        expectAttribute("exportTypes", null);
        expectAttribute("exportName", null);
        expectAttribute("filterExpression", null);
        expectAttribute("id", "myid");
        
        expect(builder.addPropertyValue("exportName", "myid")).andReturn(builder);

        process();
    }
    
    public void testExportNameWithNoProxyTypes() throws Exception {
        
        expectAttribute("proxyTypes", null);
        expectAttribute("exportTypes",null);
        expectAttribute("exportName", "myExportName");
        expectAttribute("filterExpression", null);

        failWith("The 'exportName' attribute has been specified, requiring also that the 'proxyTypes' be specified, in Impala 'service' namespace, 'import' element");
    }
    
    public void testExportNameWithExportTypes() throws Exception {
        
        expectAttribute("proxyTypes", "myProxyTypes");
        expectAttribute("exportTypes", "myExportTypes");
        expectAttribute("exportName", "myExportName");
        expectAttribute("filterExpression", null);

        process();        
    }

    public void testExportNameWithFilterExpression() throws Exception {
        
        expectAttribute("proxyTypes", "myProxyTypes");
        expectAttribute("exportTypes", "myExportTypes");
        expectAttribute("exportName", "myExportName");
        expectAttribute("filterExpression", "myFilterExpression");

        failWith("The 'exportName' attribute has been specified, which cannot be used with the 'filterExpression' attribute , in Impala 'service' namespace, 'import' element");
    }

    public void testExportWithNameAndProxyTypes() throws Exception {
        
        expectAttribute("proxyTypes", "myProxyTypes");
        expectAttribute("exportTypes", null);
        expectAttribute("exportName", "myExportName");
        expectAttribute("filterExpression", null);

        process();    
    }

    public void testExportWithTypesAndFilter() throws Exception {
        
        expectAttribute("proxyTypes", "myProxyTypes");
        expectAttribute("exportTypes", "myExportTypes");
        expectAttribute("exportName", null);
        expectAttribute("filterExpression", "myFilterExpression");

        process();    
    }

    public void testExportWithTypesOnly() throws Exception {
        
        expectAttribute("proxyTypes", null);
        expectAttribute("exportTypes", "myExportTypes");
        expectAttribute("exportName", null);
        expectAttribute("filterExpression", null);

        process();    
    }
    
    public void testClassnameForNameOnly() throws Exception {

        expectAttribute("filterExpression", null);
        expectAttribute("exportTypes", null);

        classNameFor(NamedServiceProxyFactoryBean.class);    
    }
    
    public void testClassnameForNameAndProxyTypes() throws Exception {
        
        expectAttribute("filterExpression", null);
        expectAttribute("exportTypes", "sometype");

        classNameFor(TypedServiceProxyFactoryBean.class);    
    }

    public void testClassnameForTypesAndFilter() throws Exception {
        
        expectAttribute("filterExpression", "myFilterExpression");

        classNameFor(FilteredServiceProxyFactoryBean.class);    
    }

    public void testClassnameForTypesOnly() throws Exception {

        expectAttribute("filterExpression", null);
        expectAttribute("exportTypes", "mytype");

        classNameFor(TypedServiceProxyFactoryBean.class);     
    }

    private void classNameFor(Class<?> expectedClass) {
        replay(element);
        assertEquals(expectedClass, importParser.getBeanClass(element));
        verify(element);
    }

    private void process() {
        replay(builder);
        replay(element);
        importParser.postProcess(builder, element);
        verify(builder);
        verify(element);
    }

    private void failWith(String expected) {
        replay(element);
        try {
            importParser.postProcess(null, element);
            fail();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(expected, e.getMessage());
        }
        verify(element);
    }

    private void expectAttribute(String attributeName, String value) {
        expect(element.getAttribute(attributeName)).andReturn(value);
    }
    
}
