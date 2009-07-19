/*
 * Copyright 2007-2008 the original author or authors.
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

import junit.framework.TestCase;

public class WebMappingBeanDefinitionParserTest extends TestCase {

    private WebMappingBeanDefinitionParser parser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        parser = new WebMappingBeanDefinitionParser();
    }
    
    public void testGetServletPathFalse() {
        assertEquals(null, parser.getServletPath("/path", "false", "/anotherpath"));
        assertEquals(null, parser.getServletPath("/path", "false", "/path"));
        assertEquals(null, parser.getServletPath("/path", "false", null));
    }    
    
    public void testGetServletPathNull() {
        assertEquals(null, parser.getServletPath("/path", null, ""));
        assertEquals("/path", parser.getServletPath("/path", null, "/path"));
        assertEquals("/anotherpath", parser.getServletPath("/path", null, "/anotherpath"));
    } 
    
    public void testGetServletPathTrue() {
        assertEquals("/path", parser.getServletPath("/path", "true", null));
        assertEquals("/path", parser.getServletPath("/path", "true", ""));
        assertEquals("/anotherpath", parser.getServletPath("/path", "true", "/anotherpath"));
    }

}
