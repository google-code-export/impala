package org.impalaframework.osgi.util;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.net.URL;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
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
	
	@SuppressWarnings("unchecked")
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
	
	/*

	public void testInstallBundle() {
		fail("Not yet implemented");
	}

	public void testStartBundle() {
		fail("Not yet implemented");
	}

	public void testUpdateBundle() {
		fail("Not yet implemented");
	}

	public void testStopBundle() {
		fail("Not yet implemented");
	}
	
	*/
	
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
