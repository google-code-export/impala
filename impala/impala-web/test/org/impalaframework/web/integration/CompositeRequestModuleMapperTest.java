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

package org.impalaframework.web.integration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class CompositeRequestModuleMapperTest extends TestCase {

    private CompositeRequestModuleMapper mapper;
    private RequestModuleMapper requestModuleMapper1;
    private RequestModuleMapper requestModuleMapper2;
    private HttpServletRequest request;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mapper = new CompositeRequestModuleMapper();
        
        List<RequestModuleMapper> mappers = new ArrayList<RequestModuleMapper>();
        requestModuleMapper1 = createMock(RequestModuleMapper.class);
        requestModuleMapper2 = createMock(RequestModuleMapper.class);
        request = createMock(HttpServletRequest.class);
        mappers.add(requestModuleMapper1);
        mappers.add(requestModuleMapper2);
        
        mapper.setRequestModuleMappers(mappers);
    }
    
    public void testGetModuleNull() {
        expect(requestModuleMapper1.getModuleForRequest(request)).andReturn(null);
        expect(requestModuleMapper2.getModuleForRequest(request)).andReturn(null);
        
        replay(requestModuleMapper1, requestModuleMapper2, request);
        
        assertNull(mapper.getModuleForRequest(request));
        
        verify(requestModuleMapper1, requestModuleMapper2, request); 
    }
    
    public void testGetModuleOne() {
        RequestModuleMapping mapping = new RequestModuleMapping("/one", "one", null);
        expect(requestModuleMapper1.getModuleForRequest(request)).andReturn(mapping);
        replay(requestModuleMapper1, requestModuleMapper2, request);
        
        assertEquals(mapping, mapper.getModuleForRequest(request));
        
        verify(requestModuleMapper1, requestModuleMapper2, request); 
    }
    
    public void testGetModuleTwo() {
        expect(requestModuleMapper1.getModuleForRequest(request)).andReturn(null);
        RequestModuleMapping mapping = new RequestModuleMapping("/two", "two", null);
        expect(requestModuleMapper2.getModuleForRequest(request)).andReturn(mapping);
        
        replay(requestModuleMapper1, requestModuleMapper2, request);
        
        assertEquals(mapping, mapper.getModuleForRequest(request));
        
        verify(requestModuleMapper1, requestModuleMapper2, request); 
    }

}
