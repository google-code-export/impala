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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import org.springframework.context.support.GenericApplicationContext;

import junit.framework.TestCase;

public class ApplicationContextExporterTest extends TestCase {

    public void testAfterPropertiesSet() throws Exception {
        final ServletContext servletContext = createMock(ServletContext.class);
        final ApplicationContextExporter exporter = new ApplicationContextExporter();
        final GenericApplicationContext applicationContext = new GenericApplicationContext();
        
        exporter.setServletContext(servletContext);
        exporter.setApplicationContext(applicationContext);
        exporter.setContextAttribute("att");
    
        servletContext.setAttribute("att", applicationContext);
        servletContext.removeAttribute("att");
        
        replay(servletContext);
        
        exporter.afterPropertiesSet();
        exporter.destroy();
        
        verify(servletContext);
    }

}
