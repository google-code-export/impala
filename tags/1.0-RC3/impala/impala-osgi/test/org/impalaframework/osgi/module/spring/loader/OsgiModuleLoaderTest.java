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

package org.impalaframework.osgi.module.spring.loader;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContext;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.service.ServiceRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.osgi.io.OsgiBundleResource;
import org.springframework.util.ClassUtils;

public class OsgiModuleLoaderTest extends TestCase {

    private OsgiModuleLoader moduleLoader;
    private BundleContext bundleContext;
    private ModuleLocationResolver moduleLocationResolver;
    private ServiceRegistry serviceRegistry;
    private Bundle bundle;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bundleContext = createMock(BundleContext.class);
        moduleLocationResolver = createMock(ModuleLocationResolver.class);
        serviceRegistry = createMock(ServiceRegistry.class);
        bundle = createMock(Bundle.class);

        initLoader(bundle);
    }

    private void initLoader(Bundle bundle, ImpalaOsgiApplicationContext applicationContext) {
        moduleLoader = new TestModuleLoader(bundle, applicationContext);
        initLoader();
    }
    
    private void initLoader(Bundle bundle) {
        moduleLoader = new TestModuleLoader(bundle);
        initLoader();
    }

    private void initLoader() {
        moduleLoader.setModuleLocationResolver(moduleLocationResolver);
        moduleLoader.setBundleContext(bundleContext);
    }
    
    public void testGetClassLocations() throws Exception {
        final FileSystemResource resource1 = new FileSystemResource("resource1");
        final FileSystemResource resource2 = new FileSystemResource("resource2");
        
        final List<Resource> asList = Arrays.asList(new Resource[] {resource1, resource2});
        expect(moduleLocationResolver.getApplicationModuleClassLocations("mymodule")).andReturn(asList);
        
        replayMocks();
        
        final Resource[] classLocations = moduleLoader.getClassLocations("id", new SimpleModuleDefinition("mymodule"));
        assertSame(resource1, classLocations[0]);
        assertSame(resource2, classLocations[1]);
        assertEquals(2, classLocations.length);
        
        verifyMocks();
    }
    
    public void testSpringConfigResources() throws Exception {
        replayMocks();
        
        final Resource[] springConfigResources = moduleLoader.getSpringConfigResources("id", new SimpleModuleDefinition("mymodule"), null);
        assertEquals(1, springConfigResources.length);
        assertEquals("mymodule-context.xml", springConfigResources[0].getFilename());
        assertTrue(springConfigResources[0] instanceof OsgiBundleResource);
        
        verifyMocks();
    }
    
    public void testSpringConfigResourceNullBundle() throws Exception {
        initLoader(null);
        
        replayMocks();
        
        try {
            moduleLoader.getSpringConfigResources("id", new SimpleModuleDefinition("mymodule"), null);
            fail();
        } catch (InvalidStateException e) {
            assertEquals("Unable to find bundle with name corresponding with module 'name=mymodule, configLocations=[], type=APPLICATION, dependencies=[], runtime=spring'. Check to see whether this module installed properly.", e.getMessage());
        }
        
        verifyMocks();
    }
    
    public void testNewApplicationContext() throws Exception {
        final ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        
        final SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("mymodule");
        final ImpalaOsgiApplicationContext applicationContext = createMock(ImpalaOsgiApplicationContext.class);
        initLoader(bundle, applicationContext);
    
        expect(bundle.getBundleContext()).andStubReturn(bundleContext);
        applicationContext.setBundleContext(bundleContext);
        applicationContext.setClassLoader(defaultClassLoader);
        applicationContext.setConfigResources((Resource[]) anyObject());
        applicationContext.setDisplayName(isA(String.class));
        applicationContext.startRefresh();
        
        replayMocks();
        replay(applicationContext);
        
        final ConfigurableApplicationContext newContext = moduleLoader.newApplicationContext(null, null, moduleDefinition, defaultClassLoader);
        assertSame(applicationContext, newContext);

        verifyMocks();
        verify(applicationContext);
    }
    
    public void testConstructNewApplicationContext() throws Exception {
        
        ApplicationManager applicationManager = TestApplicationManager.newApplicationManager(null, null, serviceRegistry);
        Application application = applicationManager.getCurrentApplication();
        
        final ImpalaOsgiApplicationContext newApplicationContext = moduleLoader.newApplicationContext(application, null, new SimpleModuleDefinition("mymodule"));
        final String className = newApplicationContext.getClass().getName();
        assertFalse(className.equals(ImpalaOsgiApplicationContext.class.getName()));
        assertTrue(newApplicationContext instanceof ImpalaOsgiApplicationContext);
    }
    
    public void testNewBeanDefinitionReader() throws Exception {
        //does nothing
        assertNull(moduleLoader.newBeanDefinitionReader("id", null, null));
    }
    
    public void testDoRefresh() throws Exception {
        final ImpalaOsgiApplicationContext applicationContext = createMock(ImpalaOsgiApplicationContext.class);
        
        applicationContext.completeRefresh();
        
        replayMocks();
        replay(applicationContext);
        
        moduleLoader.handleRefresh("id", applicationContext, new SimpleModuleDefinition("moduleName"));

        verifyMocks();
        verify(applicationContext);
    }
    
    private void replayMocks() {
        replay(serviceRegistry);
        replay(moduleLocationResolver);
        replay(bundleContext);
        replay(bundle);
    }

    private void verifyMocks() {
        verify(serviceRegistry);
        verify(moduleLocationResolver);
        verify(bundleContext);
        verify(bundle);
    }

}

class TestModuleLoader extends OsgiModuleLoader {

    private Bundle bundle;
    private ImpalaOsgiApplicationContext applicationContext;

    public TestModuleLoader(Bundle bundle) {
        super();
        this.bundle = bundle;
    }   
    
    public TestModuleLoader(Bundle bundle,  ImpalaOsgiApplicationContext applicationContext) {
        super();
        this.bundle = bundle;
        this.applicationContext = applicationContext;
    }

    @Override
    Bundle findBundle(ModuleDefinition moduleDefinition) {
        return bundle;
    }

    @Override
    ImpalaOsgiApplicationContext newApplicationContext(
            Application application, ApplicationContext parent, ModuleDefinition moduleDefinition) {
        if (applicationContext != null) {
            return applicationContext;
        }
        return super.newApplicationContext(application, parent, moduleDefinition);
    }
    
}

