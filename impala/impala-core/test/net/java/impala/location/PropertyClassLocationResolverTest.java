/*
 * Copyright 2007 the original author or authors.
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

package net.java.impala.location;

import java.io.File;
import java.util.Properties;

import net.java.impala.location.PropertyClassLocationResolver;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class PropertyClassLocationResolverTest extends TestCase {

	private Properties props;

	private PropertyClassLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		props = new Properties();
		super.setUp();
	}

	public void testGetApplicationPluginSpringLocation() {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.plugin.spring.dir", "deploy/spring");
		resolver = new PropertyClassLocationResolver(props);
		File location = resolver.getApplicationPluginSpringLocation("myplugin");
		assertEquals(new File(System.getProperty("java.io.tmpdir") + "/myplugin/deploy/spring/myplugin-context.xml"),
				location);
	}

	public void testGetSystemPluginLocations() {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.system.plugin.dir", "sysplugins");
		props.put("impala.plugin.spring.dir", "deploy/spring");
		resolver = new PropertyClassLocationResolver(props);
		File location = resolver.getSystemPluginSpringLocation("myplugin");
		assertEquals(new File(System.getProperty("java.io.tmpdir")
				+ "/sysplugins/deploy/spring/myplugin/myplugin-context.xml"), location);

		location = resolver.getSystemPluginClassLocation("myplugin");
		assertEquals(new File(System.getProperty("java.io.tmpdir") + "/sysplugins/deploy/spring/myplugin"), location);
	}

	public void testNoSystemPluginLocations() {
		System.clearProperty("impala.system.plugin.dir");
		props.clear();
		resolver = new PropertyClassLocationResolver(props);
		try {
			resolver.getSystemPluginSpringLocation("myplugin");
			fail("Should fail because property 'impala.system.plugin.dir' not set");
		}
		catch (IllegalStateException e) {
			assertEquals("Property 'impala.system.plugin.dir' not set. You need this to use system plugins", e.getMessage());
		}
	}

	public void testGetPluginClassLocations() {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.plugin.class.dir", "deploy/classes");
		resolver = new PropertyClassLocationResolver(props);
		File[] locations = resolver.getApplicationPluginClassLocations("myplugin");
		File location = locations[0];
		assertEquals(new File(System.getProperty("java.io.tmpdir") + "/myplugin/deploy/classes"), location);
	}

	public void testGetParentTestLocations() {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.plugin.prefix", "myprefix");
		props.put("impala.parent.test.dir", "deploy/testclasses");
		resolver = new PropertyClassLocationResolver(props);
		File[] locations = resolver.getTestClassLocations("project");
		File location = locations[0];
		assertEquals(new File(System.getProperty("java.io.tmpdir") + "/project/deploy/testclasses"), location);
	}

	public void testGetPath() {
		resolver = new PropertyClassLocationResolver(props);
		assertEquals("/", resolver.getPath("", ""));
		assertEquals("/myprefix/deploy/classes", resolver.getPath("/myprefix", "deploy/classes"));
		assertEquals("/myprefix/deploy/classes", resolver.getPath("/myprefix", "/deploy/classes"));
	}

	public void testDefaultRootProperty() {
		resolver = new PropertyClassLocationResolver(props);
		File file = new File("../");
		assertEquals(file, resolver.getRootDirectory());
	}

	public void testValidRootDirectory() {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		resolver = new PropertyClassLocationResolver(props);
		File file = new File(System.getProperty("java.io.tmpdir"));
		assertEquals(file, resolver.getRootDirectory());
	}

	public void testRootDirectoryNotFile() {
		props.put("workspace.root", ".classpath");
		resolver = new PropertyClassLocationResolver(props);

		expectIllegalState("'workspace.root' (.classpath) is not a directory");
	}

	public void testRootDirectoryNotExists() {
		props.put("workspace.root", "a file that does not exist");
		resolver = new PropertyClassLocationResolver(props);

		expectIllegalState("'workspace.root' (a file that does not exist) does not exist");
	}

	public void testMergePropertyDefault() {
		resolver = new PropertyClassLocationResolver(props);
		resolver.mergeProperty("property.name", "default property value", null);
		assertEquals("default property value", resolver.getProperty("property.name"));
	}

	public void testMergePropertySupplied() {
		props.put("property.name", "supplied property value");
		resolver = new PropertyClassLocationResolver(props);
		resolver.mergeProperty("property.name", "supplied property value", null);
		assertEquals("supplied property value", resolver.getProperty("property.name"));
	}

	public void testMergePropertySystem() {
		props.put("property.name", "supplied property value");
		try {
			System.setProperty("property.name", "system property value");
			resolver = new PropertyClassLocationResolver(props);
			resolver.mergeProperty("property.name", "system property value", null);
			assertEquals("system property value", resolver.getProperty("property.name"));
		}
		finally {
			System.clearProperty("property.name");
		}
	}

	public void testMergePropertyAddSuffix() {
		props.put("property.name", "supplied property value");
		resolver = new PropertyClassLocationResolver(props);
		resolver.mergeProperty("property.name", "supplied property value-", "-");
		assertEquals("supplied property value-", resolver.getProperty("property.name"));
	}

	public void testInit() {
		resolver = new PropertyClassLocationResolver(props);
		assertNull(resolver.getProperty(PropertyClassLocationResolver.SYSTEM_PLUGIN_DIR));
		assertNotNull(resolver.getProperty(PropertyClassLocationResolver.PLUGIN_CLASS_DIR_PROPERTY));
		assertNotNull(resolver.getProperty(PropertyClassLocationResolver.PLUGIN_SPRING_DIR_PROPERTY));
		assertNotNull(resolver.getProperty(PropertyClassLocationResolver.PARENT_TEST_DIR));
	}

	private void expectIllegalState(String expected) {
		try {
			resolver.getRootDirectory();
			fail();
		}
		catch (IllegalStateException e) {
			try {
				assertTrue(e.getMessage().contains(expected));
			}
			catch (AssertionFailedError ee) {
				System.out.println("Actual: " + e.getMessage());
				System.out.println("Expected: " + expected);
				throw ee;
			}
		}
	}

}
