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

package org.impalaframework.osgi.extender;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.impalaframework.exception.ExecutionException;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.facade.SimpleOperationsFacade;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.definition.ConstructedModuleDefinitionSource;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.source.InternalXmlModuleDefinitionSource;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.osgi.startup.OsgiContextStarter;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.context.ApplicationContext;

public class ImpalaActivatorTest extends TestCase {

    private BundleContext bundleContext;
    private OsgiContextStarter contextStarter;
    private ImpalaActivator activator;
    private ApplicationContext applicationContext;
    private ModuleManagementFacade moduleManagementFacade;
    private ModuleDefinitionSource moduleDefinitionSource;
    private InternalOperationsFacade operationsFacade;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bundleContext = createMock(BundleContext.class);
        contextStarter = createMock(OsgiContextStarter.class);
        applicationContext = createMock(ApplicationContext.class);
        moduleManagementFacade = createMock(ModuleManagementFacade.class);
        operationsFacade = createMock(InternalOperationsFacade.class);
        moduleDefinitionSource = new ConstructedModuleDefinitionSource(new SimpleRootModuleDefinition("root", "root.xml"));
        initActivator(moduleDefinitionSource);
    }

    @SuppressWarnings("unchecked")
    public void testActivate() throws Exception {
        
        contextStarter.setBundleContext(bundleContext); 
        expect(contextStarter.startContext((List<String>) isA(Object.class))).andReturn(null);
        expect(bundleContext.getBundle()).andReturn(createMock(Bundle.class));
        replayMocks();
        
        activator.start(bundleContext);
        
        verifyMocks();
    }
    
    public void testGetBootstrapLocations() throws Exception {
        activator = new TestActivator(null, null){

            @Override
            protected URL getBootstrapLocationsResourceURL(
                    BundleContext bundleContext) {
                try {
                    return new URL("file:./duff");
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        };
        
        replayMocks();
        
        try {
            activator.getBootstrapLocations(bundleContext);
            fail();
        } catch (ExecutionException e) {
        }
        
        verifyMocks();
    }
    
    public void testInitApplicationContextNullFacade() throws Exception {
        expect(applicationContext.getBean("moduleManagementFacade")).andReturn(null);
        expect(applicationContext.getDisplayName()).andReturn("ctx");
        
        replayMocks();
        
        try {
            activator.initApplicationContext(bundleContext, applicationContext);
            fail();
        } catch (InvalidStateException e) {assertEquals("Application context 'ctx' does not contain bean named 'moduleManagementFacade'", e.getMessage());
        }
        
        verifyMocks();
    }
    
    @SuppressWarnings("unchecked")
    public void testInitApplicationContext() throws Exception {
        initActivator(null);
        
        expect(applicationContext.getBean("moduleManagementFacade")).andReturn(moduleManagementFacade);
        expect(moduleManagementFacade.getApplicationManager()).andReturn(TestApplicationManager.newApplicationManager());
        
        expect(bundleContext.registerService(eq(OperationsFacade.class.getName()), isA(SimpleOperationsFacade.class), (Dictionary) isNull())).andReturn(null);
        
        replayMocks();
        
        activator.initApplicationContext(bundleContext, applicationContext);
        
        verifyMocks();
    }

    public void testMaybeGetModuleDefinitionSourceNull() throws Exception {

        activator = new TestActivator(contextStarter, null);
        
        replayMocks();
        
        activator.maybeGetModuleDefinitionSource(bundleContext, moduleManagementFacade);
        
        verifyMocks();
    }

    public void testMaybeGetModuleDefinitionSource() throws Exception {
        activator = new ImpalaActivator();
        
        final ServiceReference serviceReference = createMock(ServiceReference.class);
        
        expect(bundleContext.getServiceReference(ModuleDefinitionSource.class.getName())).andReturn(serviceReference);
        expect(bundleContext.getService(serviceReference)).andReturn(moduleDefinitionSource);
        
        replay(serviceReference);
        replayMocks();
        
        assertSame(moduleDefinitionSource, activator.maybeGetModuleDefinitionSource(bundleContext, moduleManagementFacade));

        verify(serviceReference);
        verifyMocks();
    }
    
    public void testMaybeLoadXmlResourceNull() throws Exception {
        
        activator = new ImpalaActivator() {

            @Override
            protected URL getModuleDefinitionsResourceURL(BundleContext bundleContext) {
                return null;
            }
            
        };
        
        replayMocks();
        assertNull(activator.maybeLoadXmlResource(bundleContext, moduleManagementFacade));
        verifyMocks();
    }
    
    public void testMaybeLoadXmlResource() throws Exception {
        final ModuleLocationResolver resolver = EasyMock.createMock(ModuleLocationResolver.class);
        final TypeReaderRegistry registry = EasyMock.createMock(TypeReaderRegistry.class);
        expect(moduleManagementFacade.getModuleLocationResolver()).andReturn(resolver);
        expect(moduleManagementFacade.getTypeReaderRegistry()).andReturn(registry);
        
        replayMocks();
        
        assertTrue(activator.maybeLoadXmlResource(bundleContext, moduleManagementFacade) instanceof InternalXmlModuleDefinitionSource);

        verifyMocks();
    }
    
    public void testStopNull() throws Exception {
        
        replayMocks();
        activator.stop(bundleContext);
        verifyMocks();
    }
    
    public void testStop() throws Exception {
        
        activator = new ImpalaActivator() {

            @Override
            InternalOperationsFacade newOperationsFacade(ModuleManagementFacade facade) {
                return operationsFacade;
            }
            
        };
        
        activator.setNewOperationsFacade(null);
        
        //expectations
        operationsFacade.unloadRootModule();
        expect(operationsFacade.getModuleManagementFacade()).andReturn(moduleManagementFacade);
        moduleManagementFacade.close();
        
        replayMocks();
        
        activator.stop(bundleContext);
        
        verifyMocks();
    }
    
    private void initActivator(ModuleDefinitionSource moduleDefinitionSource) {
        activator = new TestActivator(contextStarter, moduleDefinitionSource);
    }
    
    private void replayMocks() {
        replay(bundleContext);
        replay(contextStarter);
        replay(applicationContext);
        replay(operationsFacade);
        replay(moduleManagementFacade);
    }
    
    private void verifyMocks() {
        verify(bundleContext);
        verify(contextStarter); 
        verify(applicationContext); 
        verify(operationsFacade); 
        verify(moduleManagementFacade); 
    }

}

class TestActivator extends ImpalaActivator {

    private OsgiContextStarter contextStarter;
    private ModuleDefinitionSource moduleDefinitionSource;

    public TestActivator(OsgiContextStarter contextStarter, ModuleDefinitionSource moduleDefinitionSource) {
        super();
        this.contextStarter = contextStarter;
        this.moduleDefinitionSource = moduleDefinitionSource;
    }

    @Override
    OsgiContextStarter newContextStarter() {
        return contextStarter;
    }

    @Override
    protected URL getBootstrapLocationsResourceURL(BundleContext bundleContext) {
        return this.getClass().getClassLoader().getResource("impala.properties");
    }   
    
    protected URL getModuleDefinitionsResourceURL(BundleContext bundleContext) {
        return this.getClass().getClassLoader().getResource("moduledefinitions.xml");
    }

    @Override
    ModuleDefinitionSource maybeGetModuleDefinitionSource(
            BundleContext bundleContext, ModuleManagementFacade facade) {
        return moduleDefinitionSource;
    }
    
}
