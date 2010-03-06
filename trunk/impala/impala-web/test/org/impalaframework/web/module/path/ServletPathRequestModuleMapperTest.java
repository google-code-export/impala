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

package org.impalaframework.web.module.path;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static org.easymock.EasyMock.*;

import org.impalaframework.web.integration.IntegrationFilterConfig;
import org.impalaframework.web.integration.IntegrationServletConfig;
import org.impalaframework.web.integration.ServletPathRequestModuleMapper;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;

import junit.framework.TestCase;

public class ServletPathRequestModuleMapperTest extends TestCase {
    
    private HashMap<String, String> initParameters;
    private ServletContext servletContext;
    private ServletPathRequestModuleMapper mapper;
    private HttpServletRequest request;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initParameters = new HashMap<String, String>();
        servletContext = createMock(ServletContext.class);
        mapper = new ServletPathRequestModuleMapper();
        request = createMock(HttpServletRequest.class);
    }
    
    public void testGetModuleForRequest() {
        expect(request.getRequestURI()).andReturn("/app/mymodule/resource.htm");
        mapper.init(new IntegrationServletConfig(initParameters, servletContext, "filterServlet"));
        
        replay(request);
        assertEquals(new RequestModuleMapping("/mymodule", "mymodule", null), mapper.getModuleForRequest(request));
        verify(request);
    }

    public void testGetModuleForRequestWithPrefix() {
        initParameters.put("modulePrefix", "someprefix");
        
        expect(request.getRequestURI()).andReturn("/app/mymodule/resource.htm");
        mapper.init(new IntegrationServletConfig(initParameters, servletContext, "filterServlet"));
        
        replay(request);
        assertEquals(new RequestModuleMapping("/mymodule", "someprefixmymodule", null), mapper.getModuleForRequest(request));
        verify(request);
    }
    

    public void testGetModuleWithFilter() {
        initParameters.put("modulePrefix", "anotherprefix");
        mapper.init(new IntegrationFilterConfig(initParameters, servletContext, "filterServlet"));
        
        expect(request.getRequestURI()).andReturn("/app/mymodule/resource.htm");
        mapper.init(new IntegrationServletConfig(initParameters, servletContext, "filterServlet"));
        
        replay(request);
        assertEquals(new RequestModuleMapping("/mymodule", "anotherprefixmymodule", null), mapper.getModuleForRequest(request));
        verify(request);
    }

}
