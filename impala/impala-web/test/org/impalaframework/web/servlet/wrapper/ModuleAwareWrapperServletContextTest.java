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

package org.impalaframework.web.servlet.wrapper;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.springframework.util.ClassUtils;

public class ModuleAwareWrapperServletContextTest extends TestCase {

    private ServletContext servletContext;
    private ModuleAwareWrapperServletContext wrapperContext;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = createMock(ServletContext.class);
        wrapperContext = new ModuleAwareWrapperServletContext(servletContext, "mymodule", ClassUtils.getDefaultClassLoader());
    }
    
    public void testGetModuleSpecificAttribute() throws Exception {
        
        expect(servletContext.getAttribute("application__module_mymodule:myattribute")).andReturn("someValue");
        
        replay(servletContext);
        
        assertEquals("someValue", wrapperContext.getAttribute("myattribute"));

        verify(servletContext);
    }
    
    public void testGetSharedAttribute() throws Exception {
        
        expect(servletContext.getAttribute("application__module_mymodule:myattribute")).andReturn(null);
        expect(servletContext.getAttribute("myattribute")).andReturn("someValue2");
        
        replay(servletContext);
        
        assertEquals("someValue2", wrapperContext.getAttribute("myattribute"));

        verify(servletContext);
    }
    
    public void testGetWriteKeyToUse() throws Exception {
        assertEquals("", wrapperContext.getWriteKeyToUse(""));
        assertEquals("mykey", wrapperContext.getWriteKeyToUse("mykey"));
        assertEquals("mykey", wrapperContext.getWriteKeyToUse("shared:mykey"));
    }

}
