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

package org.impalaframework.web.module;

import java.util.Collections;

import org.impalaframework.web.AttributeServletContext;

import junit.framework.TestCase;

public class WebModuleUtilsTest extends TestCase {
    
    @Override
    protected void setUp() throws Exception {
        clearProperties();
    }

    public void testGetParamValue() {
        AttributeServletContext servletContext = new AttributeServletContext();
        servletContext.setContextPath("/path");
        
        assertNull(WebModuleUtils.getParamValue(servletContext, "name"));
        
        servletContext.setInitParams(Collections.singletonMap("name", "initParamValue"));
        assertEquals("initParamValue", WebModuleUtils.getParamValue(servletContext, "name"));
        
        System.setProperty("name", "systemPropertyValue");
        assertEquals("systemPropertyValue", WebModuleUtils.getParamValue(servletContext, "name"));

        System.setProperty("path.name", "pathSystemPropertyValue");
        assertEquals("pathSystemPropertyValue", WebModuleUtils.getParamValue(servletContext, "name"));    
    }
    
    @Override
    protected void tearDown() throws Exception {
        clearProperties();
    }

    private void clearProperties() {
        System.clearProperty("name");
        System.clearProperty("path.name");
    }

}
