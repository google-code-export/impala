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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.util.ResourceUtils;
import org.impalaframework.util.XMLDomUtils;
import org.impalaframework.web.servlet.invocation.ModuleInvokerContributor;
import org.impalaframework.web.spring.integration.ContextAndServletPath;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WebMappingBeanDefinitionParserTest extends TestCase {

    private TestParser parser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        parser = new TestParser();
    }
    
    public void testGetServletPathFalse() {
        assertEquals(null, parser.getServletPath("/path", "false", "/anotherpath"));
        assertEquals(null, parser.getServletPath("/path", "false", "/path"));
        assertEquals(null, parser.getServletPath("/path", "false", null));
        assertEquals(null, parser.getServletPath("/path", "false", "EMPTY"));
    }    
    
    public void testGetServletPathNull() {
        assertEquals("", parser.getServletPath("/path", null, ""));
        assertEquals("/path", parser.getServletPath("/path", null, "/path"));
        assertEquals("/anotherpath", parser.getServletPath("/path", null, "/anotherpath"));
        assertEquals(null, parser.getServletPath("/path", null, null));
    } 
    
    public void testGetServletPathTrue() {
        assertEquals("/path", parser.getServletPath("/path", "true", null));
        assertEquals("", parser.getServletPath("/path", "true", ""));
        assertEquals("/anotherpath", parser.getServletPath("/path", "true", "/anotherpath"));
    }
    
    public void testGetFilterNames() throws Exception {
        assertEquals(Arrays.asList("one"), Arrays.asList(parser.getFilterNames("one")));
        assertEquals(Arrays.asList("one","two","three"), Arrays.asList(parser.getFilterNames("one, two,  three")));
        assertEquals(null, parser.getFilterNames(null));
        assertEquals(null, parser.getFilterNames(""));
    }
    
    public void testGetServletName() throws Exception {
        assertEquals("one", parser.getServletName("one"));
        assertEquals("one", parser.getServletName("one "));
        assertEquals(null, parser.getServletName(""));
        assertEquals(null, parser.getServletName(null));
    }
    
    public void testSetContextPathAndSetServletPathOnly() throws Exception {
        final Element loadElement = loadElement("setcontextandservletpathonly.txt");
        //eyeball logs to see warning
        parser.parse(loadElement, null);
    }
    
    @SuppressWarnings("unchecked")
    public void testDefinitions() throws Exception {
        Element element = loadElement("definitions.txt");
        parser.parse(element, null);
        List<RootBeanDefinition> definitions = parser.getDefinitions();
        System.out.println(definitions);
        assertEquals(3, definitions.size());
        
        RootBeanDefinition prefixDefinition = definitions.get(0);
        PropertyValue prefixMap = prefixDefinition.getPropertyValues().getPropertyValue("prefixMap");
        Map<String,ContextAndServletPath> actualValue = (Map<String, ContextAndServletPath>) prefixMap.getValue();
        
        Map<String,ContextAndServletPath> expectedValue = new LinkedHashMap<String, ContextAndServletPath>();
        expectedValue.put("/path1", new ContextAndServletPath(null, null));
        expectedValue.put("/path2", new ContextAndServletPath(null, "/path2"));
        expectedValue.put("/path3", new ContextAndServletPath(null, "/path3modified"));
        expectedValue.put("/path4", new ContextAndServletPath("/path4modified", null));
        expectedValue.put("/path5", new ContextAndServletPath("/path5", null));
        expectedValue.put("/path6", new ContextAndServletPath("/cp", "/sp"));
        
        assertEquals(expectedValue, actualValue);
        
        RootBeanDefinition invokerDefinition = definitions.get(1);
        Class<?> invokerContributor = invokerDefinition.getBeanClass();
        assertTrue(invokerContributor.equals(ModuleInvokerContributor.class));
        
        assertEquals("doServlet", invokerDefinition.getPropertyValues().getPropertyValue("servletName").getValue());
        assertEquals(null, invokerDefinition.getPropertyValues().getPropertyValue("filterNames").getValue());
        
        invokerDefinition = definitions.get(2);
        assertEquals("cssServlet", invokerDefinition.getPropertyValues().getPropertyValue("servletName").getValue());
        String asList = Arrays.toString((String[])invokerDefinition.getPropertyValues().getPropertyValue("filterNames").getValue());
        System.out.println(asList);
        assertEquals("[cssFilter1, cssFilter2]", asList);
    }

    private Element loadElement(String fileName) {
        Document loadDocument = XMLDomUtils.loadDocument(new StringReader(read(fileName)), fileName);
        return loadDocument.getDocumentElement();
    }

    private String read(String file) {
        return ResourceUtils.readText(new ClassPathResource("org.impalaframework.web.spring.config".replace('.', '/') + "/" + file));
    }

}

class TestParser extends WebMappingBeanDefinitionParser {
    private List<RootBeanDefinition> definitions = new ArrayList<RootBeanDefinition>();

    @Override
    void registerDefinition(ParserContext parserContext, RootBeanDefinition definition) {
        this.definitions.add(definition);
    }
    
    public List<RootBeanDefinition> getDefinitions() {
        return definitions;
    }
}
