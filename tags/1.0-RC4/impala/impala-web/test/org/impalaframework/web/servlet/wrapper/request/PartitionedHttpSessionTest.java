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

package org.impalaframework.web.servlet.wrapper.request;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.impalaframework.util.CollectionStringUtils;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.impalaframework.web.servlet.wrapper.session.PartitionedHttpSession;

import junit.framework.TestCase;

public class PartitionedHttpSessionTest extends TestCase {

    private String moduleName;
    private WebAttributeQualifier webAttributeQualifier;
    private String applicationId;
    private HttpSession httpSession;
    private PartitionedHttpSession session;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        moduleName = "mymodule";
        webAttributeQualifier = createMock(WebAttributeQualifier.class);
        applicationId = "id";
        httpSession = createMock(HttpSession.class);
        session = new PartitionedHttpSession(httpSession, webAttributeQualifier, applicationId, moduleName);
    }
    
    public void testSetAttribute() throws Exception {

        expect(webAttributeQualifier.getQualifiedAttributeName("myattribute", applicationId, moduleName)).andReturn("anotherattribute");
        httpSession.setAttribute("anotherattribute","some_value");
        
        replay(webAttributeQualifier, httpSession);
        
        session.setAttribute("myattribute", "some_value");
        
        verify(webAttributeQualifier, httpSession);
    }
    
    public void testGetAttribute() throws Exception {

        expect(webAttributeQualifier.getQualifiedAttributeName("myattribute", applicationId, moduleName)).andReturn("anotherattribute");
        expect(httpSession.getAttribute("anotherattribute")).andReturn("some value");
        
        replay(webAttributeQualifier, httpSession);
        
        assertEquals("some value", session.getAttribute("myattribute"));
        
        verify(webAttributeQualifier, httpSession);
    }
    
    public void testGetEnumeration() throws Exception {

        final Enumeration<String> initialNames = Collections.enumeration(CollectionStringUtils.parseStringList("one,two"));
        final Enumeration<String> expectedNames = Collections.enumeration(CollectionStringUtils.parseStringList("one,two"));
        expect(httpSession.getAttributeNames()).andReturn(initialNames);
        
        expect(webAttributeQualifier.filterAttributeNames(initialNames, applicationId, moduleName)).andReturn(expectedNames);
        
        replay(webAttributeQualifier, httpSession);
        
        assertEquals(expectedNames, session.getAttributeNames());
        
        verify(webAttributeQualifier, httpSession);
    }
    
}
