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

package org.impalaframework.web.spring.module;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;

public class WebRootModuleLoaderTest extends TestCase {

    private String projectNames = "p1";
    
    private ServletContext servletContext;
    private WebRootModuleLoader loader;

    public void setUp() {
        servletContext = createMock(ServletContext.class);
        loader = new WebRootModuleLoader();
        loader.setServletContext(servletContext);
    }
    
    public final void testNewApplicationContext() {
        final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        final GenericApplicationContext parent = new GenericApplicationContext();
        final GenericWebApplicationContext applicationContext = loader.newApplicationContext(null, parent, new SimpleRootModuleDefinition(projectNames, new String[]{"loc"}), classLoader);

        assertNotNull(applicationContext);
        assertNotNull(applicationContext.getBeanFactory());
        assertSame(classLoader, applicationContext.getClassLoader());
        assertSame(servletContext, applicationContext.getServletContext());
    }
    
    public void testGetSpringLocations() throws MalformedURLException {
        final String[] locations = new String[] {"context1", "context2"};
        SimpleModuleDefinition definition = new SimpleModuleDefinition(new SimpleRootModuleDefinition(projectNames, new String[]{"loc"}), "name", locations);
        loader.setServletContext(servletContext);
        
        expect(servletContext.getResource("/context1")).andReturn(new URL("file:file1"));
        expect(servletContext.getResource("/context1")).andReturn(new URL("file:file1"));
        expect(servletContext.getResource("/context2")).andReturn(new URL("file:file2"));
        expect(servletContext.getResource("/context2")).andReturn(new URL("file:file2"));
        
        replay(servletContext);
        
        final Resource[] resources = loader.getSpringConfigResources("id", definition, ClassUtils.getDefaultClassLoader());
        assertEquals(2, resources.length);
        for (int i = 0; i < resources.length; i++) {
            assertTrue(resources[i] instanceof ServletContextResource);
            assertEquals(locations[i], resources[i].getFilename());
        }

        verify(servletContext);
    }

}
