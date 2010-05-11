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

package org.impalaframework.osgi.util;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.exception.ExecutionException;
import org.impalaframework.exception.InvalidStateException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.osgi.io.OsgiBundleResource;

public class OsgiUtilsTest extends TestCase {

    private BundleContext bundleContext;
    private Bundle bundle;
    private Resource resource;
    
    @SuppressWarnings("unchecked")
    private Dictionary headers;

    @SuppressWarnings("unchecked")
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bundleContext = createMock(BundleContext.class);
        bundle = createMock(Bundle.class);
        resource = createMock(Resource.class);
        headers = new Hashtable();
    }
    
    public void testFindResources() throws Exception {
        String[] names = {"name1", "name2"};
        
        expect(bundleContext.getBundle()).andReturn(bundle);
        expect(bundle.getResource("name1")).andReturn(new URL("file:./name1"));
        expect(bundleContext.getBundle()).andReturn(bundle);
        expect(bundle.getResource("name2")).andReturn(new URL("file:./name2"));
        
        replayMocks();
        
        final URL[] resources = OsgiUtils.findResources(bundleContext, names );
        assertEquals(2, resources.length);
        assertEquals("./name1", resources[0].getFile());
        
        verifyMocks();
    }

    public void testFindResource() throws Exception {
        
        expect(bundleContext.getBundle()).andReturn(bundle);
        expect(bundle.getResource("name1")).andReturn(new URL("file:./name1"));
        
        replayMocks();
        
        final URL resource = OsgiUtils.findResource(bundleContext, "name1");
        assertEquals("./name1", resource.getFile());
        
        verifyMocks();
    }

    public void testFindResourceNotInHostBundle() throws Exception {
        
        expect(bundleContext.getBundle()).andReturn(bundle);
        expect(bundle.getResource("name1")).andReturn(null);
        expect(bundleContext.getBundles()).andReturn(new Bundle[]{ bundle });
        expect(bundle.getResource("name1")).andReturn(new URL("file:./name1"));
        
        replayMocks();
        
        final URL resource = OsgiUtils.findResource(bundleContext, "name1");
        assertEquals("./name1", resource.getFile());
        
        verifyMocks();
    }

    public void testFindResourceNull() throws Exception {
        
        expect(bundleContext.getBundle()).andReturn(bundle);
        expect(bundle.getResource("name1")).andReturn(null);
        expect(bundleContext.getBundles()).andReturn(new Bundle[]{ bundle });
        expect(bundle.getResource("name1")).andReturn(null);
        
        replayMocks();
        
        assertNull(OsgiUtils.findResource(bundleContext, "name1"));
        
        verifyMocks();
    }

    public void testFindBundleNone() {
        
        expect(bundleContext.getBundles()).andReturn(new Bundle[]{});
        
        replayMocks();
        
        assertNull(OsgiUtils.findBundle(bundleContext, "bundleName"));
        
        verifyMocks();
    }

    public void testFindBundleNotPresent() {
        
        expect(bundleContext.getBundles()).andReturn(new Bundle[]{ bundle });
        expect(bundle.getHeaders()).andReturn(headers);
        
        replayMocks();
        
        assertNull(OsgiUtils.findBundle(bundleContext, "bundleName"));
        
        verifyMocks();
    }
    
    @SuppressWarnings("unchecked")
    public void testFindBundle() {
        
        headers.put(Constants.BUNDLE_NAME, "bundleName");
        expect(bundleContext.getBundles()).andReturn(new Bundle[]{ bundle });
        expect(bundle.getHeaders()).andReturn(headers);
        
        replayMocks();
        
        assertSame(bundle, OsgiUtils.findBundle(bundleContext, "bundleName"));
        
        verifyMocks();
    }

    public void testGetBundleResources() {
        String[] names = {"name1", "name2"};
        
        replayMocks();
        
        final Resource[] resources = OsgiUtils.getBundleResources(bundle, Arrays.asList(names));
        assertTrue(resources.length == 2);
        
        assertTrue(resources[0] instanceof OsgiBundleResource);
        assertTrue(resources[0].getFilename().equals("name1"));
        
        verifyMocks();
    }

    public void testGetBundleLocation() throws Exception {
        expect(resource.getURL()).andReturn(new URL("file:./%20ok"));
        
        replayMocks();
        
        final String location = OsgiUtils.getBundleLocation(resource);
        assertEquals("file:./ ok", location);
        
        verifyMocks();
    }
    
    public void testGetBundleLocationThrowsException() throws Exception {
        expect(resource.getURL()).andThrow(new RuntimeException());
        expect(resource.getDescription()).andReturn("desc");
        
        replayMocks();
        
        final String location = OsgiUtils.getBundleLocation(resource);
        assertEquals("desc", location);
        
        verifyMocks();
    }
    
    public void testInstallBundle() throws Exception {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        final URL url = new URL("file:./");

        expect(resource.exists()).andReturn(true);
        expect(resource.getInputStream()).andReturn(stream);
        expect(resource.getURL()).andReturn(url);
        expect(bundleContext.installBundle("file:./", stream)).andReturn(bundle);
        
        replayMocks();
    
        assertSame(bundle, OsgiUtils.installBundle(bundleContext, resource));
        
        verifyMocks();
    }
    
    public void testNotInstallFromDirectory() throws Exception {
        final URL url = new URL("file:./");

        expect(resource.exists()).andReturn(false);
        expect(resource.getURL()).andReturn(url);
        
        replayMocks();
    
        assertNull(OsgiUtils.installBundle(bundleContext, resource));
        
        verifyMocks();
    }
    
    public void testInstallFromDirectory() throws Exception {
        
        //because we pass in a FileSystemResource with a valid directory, it will attempt to install from there
        expect(bundleContext.installBundle(EasyMock.isA(String.class))).andReturn(bundle);
        
        replayMocks();
    
        assertSame(bundle, OsgiUtils.installBundle(bundleContext, new FileSystemResource(new File("./"))));
        
        verifyMocks();
    }
    
    public void testInstallNullResource() throws Exception {

        expect(resource.getURL()).andReturn(new URL("file:./"));
        expect(resource.exists()).andReturn(true);
        expect(resource.getInputStream()).andReturn(null);
        expect(resource.getDescription()).andReturn("resourceDesc");
        
        replayMocks();
    
        try {
            OsgiUtils.installBundle(bundleContext, resource); 
            fail();
        } catch (InvalidStateException e) {
            assertEquals("Unable to get stream when attempting to install bundle from resource: resourceDesc", e.getMessage());
        }
        
        verifyMocks();
    }

    public void testStartBundle() throws Exception {

        expect(bundle.getHeaders()).andReturn(headers);
        bundle.start();
        
        replayMocks();
        
        OsgiUtils.startBundle(bundle); 
        
        verifyMocks();
    }
    
    public void testStartBundleFails() throws Exception {

        expect(bundle.getHeaders()).andReturn(headers);
        bundle.start();
        expectLastCall().andThrow(new BundleException("it broke"));
        expect(bundle.getSymbolicName()).andReturn("symname");
        
        replayMocks();
        
        try {
            OsgiUtils.startBundle(bundle);
            fail();
        } catch (ExecutionException e) {
            assertEquals("Unable to start bundle with symbolic name 'symname': it broke", e.getMessage());
        } 
        
        verifyMocks();
    }

    @SuppressWarnings("unchecked")
    public void testAttemptStartFragment() throws Exception {

        headers.put(Constants.FRAGMENT_HOST, "host");
        
        expect(bundle.getHeaders()).andReturn(headers);
        
        //start not called
        //bundle.start();
            
        replayMocks();
        
        OsgiUtils.startBundle(bundle); 
        
        verifyMocks();
    }

    public void testUpdateBundle() throws Exception {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);

        expect(resource.exists()).andReturn(true);
        expect(resource.getInputStream()).andReturn(stream);
        bundle.update(stream);
        
        replayMocks();
        
        OsgiUtils.updateBundle(bundle, resource); 
        
        verifyMocks();
    }

    public void testNotUpdateBundleFromDirectory() throws Exception {
        
        expect(resource.exists()).andReturn(false);
        
        replayMocks();
        
        OsgiUtils.updateBundle(bundle, resource); 
        
        verifyMocks();
    }
    
    public void testUpdateBundleFromDirectory() throws Exception {
        
        bundle.update();
        
        replayMocks();
        
        OsgiUtils.updateBundle(bundle, new FileSystemResource("./")); 
        
        verifyMocks();
    }

    public void testUpdateBundleStreamNull() throws Exception {
        
        expect(resource.exists()).andReturn(true);
        expect(resource.getInputStream()).andReturn(null);
        expect(bundle.getSymbolicName()).andReturn("symname");
        expect(resource.getDescription()).andReturn("resourceDesc");
        
        replayMocks();
        
        try {   
            OsgiUtils.updateBundle(bundle, resource); 
            fail();
        } catch (InvalidStateException e) {
            assertEquals("Unable to get stream when attempting to update bundle 'symname' from resource: resourceDesc", e.getMessage());
        }
        
        verifyMocks();
    }
    
    public void testUpdateBundleThrowException() throws Exception {
        
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);

        expect(resource.exists()).andReturn(true);
        expect(resource.getInputStream()).andReturn(stream);
        bundle.update(stream);
        expectLastCall().andThrow(new BundleException("update failed"));
        
        expect(bundle.getSymbolicName()).andReturn("symname");
        expect(resource.getDescription()).andReturn("resourceDesc");
        
        replayMocks();
        
        try {   
            OsgiUtils.updateBundle(bundle, resource); 
            fail();
        } catch (ExecutionException e) {
            assertEquals("Unable to update bundle 'symname' from resource 'resourceDesc': update failed", e.getMessage());
        }
        
        verifyMocks();
    }

    public void testStopBundle() throws BundleException {
        
        bundle.stop();
        
        replayMocks();
        
        OsgiUtils.stopBundle(bundle); 
        
        verifyMocks();
    }
    
    public void testStopBundleFails() throws Exception {

        bundle.stop();
        expectLastCall().andThrow(new BundleException("it broke"));
        expect(bundle.getSymbolicName()).andReturn("symname");
        
        replayMocks();
        
        try {
            OsgiUtils.stopBundle(bundle);
            fail();
        } catch (ExecutionException e) {
            assertEquals("Unable to stop bundle with symbolic name 'symname': it broke", e.getMessage());
        } 
        
        verifyMocks();
    }
    
    private void replayMocks() {
        replay(bundleContext);
        replay(bundle);
        replay(resource);
    }
    
    private void verifyMocks() {
        verify(bundleContext);
        verify(bundle); 
        verify(resource);
    }

}
