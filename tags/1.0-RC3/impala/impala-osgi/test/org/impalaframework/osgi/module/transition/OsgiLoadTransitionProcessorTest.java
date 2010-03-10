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

package org.impalaframework.osgi.module.transition;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.spring.module.ApplicationContextLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.springframework.core.io.Resource;

public class OsgiLoadTransitionProcessorTest extends TestCase {
    
    private OsgiLoadTransitionProcessor processor;
    private BundleContext bundleContext;
    private ModuleLocationResolver moduleLocationResolver;
    private ModuleLoader moduleLoader;
    private ApplicationContextLoader applicationContextLoader;
    private Bundle bundle;
    private Resource resource;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        bundle = createMock(Bundle.class);
        applicationContextLoader = createMock(ApplicationContextLoader.class);
        bundleContext = createMock(BundleContext.class);
        moduleLocationResolver = createMock(ModuleLocationResolver.class);
        moduleLoader = createMock(ModuleLoader.class);
        resource = createMock(Resource.class);
        
        initProcessor(bundle);
    }

    private void initProcessor(Bundle bundle) {
        processor = new TestLoadProcessor(bundle);
        processor.setBundleContext(bundleContext);
        processor.setModuleLocationResolver(moduleLocationResolver);
    }
    
    public void testFindAndStartActiveBundle() throws BundleException, IOException {
        final SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("myModule");
        final Resource[] classLocations = new Resource[] { resource };
        expectBundleUpdate();
        
        expect(bundle.getState()).andReturn(Bundle.ACTIVE);
        
        expectGetClassLocations(moduleDefinition, classLocations);      
        
        replayMocks();
        
        processor.findAndStartBundle(new SimpleModuleDefinition("myModule"));
        
        verifyMocks();
    }
    
    public void testFindAndStartResolvedBundle() throws BundleException, IOException {
        
        final SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("myModule");
        
        expectBundleUpdate();
        expect(bundle.getState()).andReturn(Bundle.RESOLVED);
        expectBundleStart();

        final Resource[] classLocations = new Resource[] { resource };
        expectGetClassLocations(moduleDefinition, classLocations);      
        
        replayMocks();
        
        processor.findAndStartBundle(new SimpleModuleDefinition("myModule"));
        
        verifyMocks();
    }
    
    public void testNoBundleEmptyClassLocations() throws BundleException {
        final SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("myModule");
        final Resource[] classLocations = new Resource[0];
        
        expectGetClassLocations(moduleDefinition, classLocations);
        initProcessor(null);
        
        replayMocks();
        
        try {
            processor.findAndStartBundle(moduleDefinition);
            fail();
        } catch (InvalidStateException e) {
            assertTrue(e.getMessage().contains("returned empty bundle class location array. Cannot install bundle for module 'myModule'"));
        }
        
        verifyMocks();
    }
    
    public void testNoBundle() throws BundleException, IOException {
        final SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("myModule");
        final Resource[] classLocations = new Resource[] { resource };
        
        expectGetClassLocations(moduleDefinition, classLocations);
        expectBundleInstall();
        
        //this wouldn't happen, but just to simplify the test
        expect(bundle.getState()).andReturn(Bundle.ACTIVE);
        
        initProcessor(null);
        
        replayMocks();
        
        processor.findAndStartBundle(moduleDefinition);
        
        verifyMocks();
    }
    
    public void testFindAndStartResolvedBundleThrowsException() throws BundleException, IOException {
        final SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("myModule");
        final Resource[] classLocations = new Resource[] { resource };
        
        expectGetClassLocations(moduleDefinition, classLocations);
        expectBundleUpdate();
        
        expect(bundle.getState()).andReturn(Bundle.RESOLVED);
        
        expectBundleStart();
        expectLastCall().andThrow(new BundleException("it broke"));
        expect(bundle.getSymbolicName()).andReturn("symbolicname");
        
        replayMocks();
        
        try {
            processor.findAndStartBundle(new SimpleModuleDefinition("myModule"));
            fail();
        } catch (ExecutionException e) {
            assertEquals("Unable to start bundle with symbolic name 'symbolicname': it broke", e.getMessage());
        }
        
        verifyMocks();
    }

    private void expectBundleInstall() throws IOException,
            MalformedURLException, BundleException {

        expect(resource.exists()).andReturn(true);
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        expect(resource.getURL()).andReturn(new URL("file:./"));
        expect(resource.getInputStream()).andReturn(stream);
        expect(bundleContext.installBundle("file:./", stream)).andReturn(bundle);
    }

    private void expectBundleUpdate() throws IOException, BundleException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);

        expect(resource.exists()).andReturn(true);
        expect(resource.getInputStream()).andReturn(stream);
        bundle.update(stream);
    }
    
    private void expectGetClassLocations(
            final SimpleModuleDefinition moduleDefinition,
            final Resource[] classLocations) {
        final List<Resource> asList = classLocations != null ? Arrays.asList(classLocations) : null;
        expect(moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName())).andReturn(asList);
    }

    @SuppressWarnings("unchecked")
    private void expectBundleStart() throws BundleException {
        expect(bundle.getHeaders()).andReturn(new Hashtable());
        bundle.start();
    }
    
    private void replayMocks() {
        replay(bundleContext);
        replay(moduleLocationResolver);
        replay(moduleLoader);
        replay(applicationContextLoader);
        replay(bundle);
        replay(resource);
    }
    
    private void verifyMocks() {
        verify(bundleContext);
        verify(moduleLocationResolver);
        verify(moduleLoader);
        verify(applicationContextLoader);
        verify(bundle); 
        verify(resource);
    }

}

class TestLoadProcessor extends OsgiLoadTransitionProcessor {

    private Bundle bundle;

    public TestLoadProcessor(Bundle bundle) {
        super();
        this.bundle = bundle;
    }

    @Override
    Bundle findBundle(ModuleDefinition moduleDefinition) {
        return bundle;
    }
    
}
