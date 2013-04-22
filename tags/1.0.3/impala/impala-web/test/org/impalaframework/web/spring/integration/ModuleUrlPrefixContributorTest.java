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

package org.impalaframework.web.spring.integration;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.web.integration.ModuleNameWithPath;
import org.impalaframework.web.integration.PrefixTreeHolder;
import org.impalaframework.web.integration.UrlPrefixRequestModuleMapper;

public class ModuleUrlPrefixContributorTest extends TestCase {
    
    private ModuleUrlPrefixContributor contributor;
    private ModuleDefinition moduleDefinition;
    private ServletContext servletContext;
    private PrefixTreeHolder holder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        contributor = new ModuleUrlPrefixContributor();
        
        moduleDefinition = createMock(ModuleDefinition.class);
        servletContext = createMock(ServletContext.class);
        contributor.setModuleDefinition(moduleDefinition);
        contributor.setServletContext(servletContext);
        
        final HashMap<String, ContextAndServletPath> prefixMap = new HashMap<String, ContextAndServletPath>();
        prefixMap.put("/p1", new ContextAndServletPath(null, null));
        prefixMap.put("/p2", new ContextAndServletPath(null, "/p2path"));
        contributor.setPrefixMap(prefixMap);
        
        holder = new PrefixTreeHolder();        
    }

    public void testAfterPropertiesSet() throws Exception {
        expect(servletContext.getAttribute(UrlPrefixRequestModuleMapper.PREFIX_HOLDER_KEY)).andReturn(holder);
        expect(moduleDefinition.getName()).andReturn("one");        
        replay(moduleDefinition, servletContext);
        
        contributor.afterPropertiesSet();

        verify(moduleDefinition, servletContext);
        
        ModuleNameWithPath p1 = holder.getModuleForURI("/p1stuff").getValue();
        assertEquals("one", p1.getModuleName());
        assertEquals(null, p1.getServletPath());
        
        ModuleNameWithPath p2 = holder.getModuleForURI("/p2stuff").getValue();
        assertEquals("one", p2.getModuleName());
        assertEquals("/p2path", p2.getServletPath());
    }

    public void testAfterPropertiesSetNull() throws Exception {
        expect(servletContext.getAttribute(UrlPrefixRequestModuleMapper.PREFIX_HOLDER_KEY)).andReturn(null);
        
        replay(moduleDefinition, servletContext);
        
        contributor.afterPropertiesSet();

        verify(moduleDefinition, servletContext);
    }

    public void testDestroy() throws Exception {
        
        holder.add("one", "/p1", null, null);
        holder.add("one", "/p2", null, null);
        
        expect(servletContext.getAttribute(UrlPrefixRequestModuleMapper.PREFIX_HOLDER_KEY)).andReturn(holder);
        expect(moduleDefinition.getName()).andReturn("one");        
        
        replay(moduleDefinition, servletContext);
        
        contributor.destroy();

        verify(moduleDefinition, servletContext);

        assertEquals(null, holder.getModuleForURI("/p1stuff"));
        assertEquals(null, holder.getModuleForURI("/p2stuff"));
    }

    public void testDestroyNull() throws Exception {
        expect(servletContext.getAttribute(UrlPrefixRequestModuleMapper.PREFIX_HOLDER_KEY)).andReturn(null);
        
        replay(moduleDefinition, servletContext);
        
        contributor.destroy();

        verify(moduleDefinition, servletContext);
    }

}
