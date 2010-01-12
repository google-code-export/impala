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

package org.impalaframework.web.spring.loader;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.web.WebConstants;
import org.springframework.web.context.WebApplicationContext;

public class BaseImpalaContextLoaderTest extends TestCase {

    private ServletContext servletContext;
    private ModuleManagementFacade facade;
    private ModuleOperationRegistry moduleOperationRegistry;
    private ModuleOperation moduleOperation;
    private ApplicationManager applicationManager;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = createMock(ServletContext.class);
        facade = createMock(ModuleManagementFacade.class);
        moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
        moduleOperation = createMock(ModuleOperation.class);
        applicationManager = createMock(ApplicationManager.class);
    }

    public final void testClose() {
        BaseImpalaContextLoader contextLoader = newContextLoader();

        servletContext.log("Closing modules and root application context hierarchy");
        servletContext.log("Closing Spring root WebApplicationContext");
        servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        
        expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(facade);

        expect(facade.getApplicationManager()).andReturn(applicationManager);
        expect(applicationManager.close()).andReturn(true);
        
        facade.close();

        replayMocks();
        
        contextLoader.closeWebApplicationContext(servletContext);

        verifyMocks();
    }
    
    public final void testCloseParentNull() {
        BaseImpalaContextLoader contextLoader = newContextLoader();

        servletContext.log("Closing modules and root application context hierarchy");
        expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(facade);

        expect(facade.getApplicationManager()).andReturn(applicationManager);
        expect(applicationManager.close()).andReturn(true);

        servletContext.log("Closing Spring root WebApplicationContext");
        facade.close();
        servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        replayMocks();
        
        contextLoader.closeWebApplicationContext(servletContext);

        verifyMocks();
    }
    
    public final void testFactoryNull() {
        BaseImpalaContextLoader contextLoader = newContextLoader();

        servletContext.log(isA(String.class));
        servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(null);

        replayMocks();
        
        contextLoader.closeWebApplicationContext(servletContext);

        verifyMocks();
    }

    private BaseImpalaContextLoader newContextLoader() {
        BaseImpalaContextLoader contextLoader = new BaseImpalaContextLoader() {
            @Override
            public ModuleDefinitionSource getModuleDefinitionSource(ServletContext servletContext, ModuleManagementFacade factory) {
                return null;
            }

            @Override
            public String[] getBootstrapContextLocations(ServletContext servletContext) {
                return new String[0];
            }
        };
        return contextLoader;
    }

    private void verifyMocks() {
        verify(servletContext);
        verify(facade);
        verify(moduleOperationRegistry);
        verify(applicationManager);
        verify(moduleOperation);
    }

    private void replayMocks() {
        replay(servletContext);
        replay(facade);
        replay(moduleOperationRegistry);
        replay(applicationManager);
        replay(moduleOperation);
    }
    

}
