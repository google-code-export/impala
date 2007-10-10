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

package net.java.impala.spring.util;

import junit.framework.TestCase;
import net.java.impala.classloader.DefaultContextResourceHelper;
import net.java.impala.location.PropertyClassLocationResolver;
import net.java.impala.monitor.FileMonitor;
import net.java.impala.spring.plugin.ApplicationContextSet;
import net.java.impala.spring.plugin.NoServiceException;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SimpleParentSpec;
import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.spring.plugin.SimplePluginSpec;
import net.java.impala.spring.plugin.SimpleSpringContextSpec;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class DefaultApplicationContextLoaderTest extends TestCase {

	private DefaultApplicationContextLoader loader;

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";
	
	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	public void setUp() {
		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		loader = new DefaultApplicationContextLoader(new DefaultContextResourceHelper(locationResolver));
	}

	public void testNewParentClassLoader() {
		assertNotNull(loader.newParentClassLoader());
	}

	public void testLoadUnloadPlugins() {

		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		loader = new DefaultApplicationContextLoader(new DefaultContextResourceHelper(locationResolver));
		SpringContextSpec spec = new SimpleSpringContextSpec("parentTestContext.xml",  
				new String[] { plugin1, plugin2 });
		
		ApplicationContextSet loaded = loader.loadParentContext(spec, this.getClass().getClassLoader());
		PluginSpec root = spec.getParentSpec();

		ConfigurableApplicationContext parent = loaded.getContext();
		assertNotNull(parent);
		assertEquals(2, loaded.getPluginContext().size());

		FileMonitor bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified(null));

		FileMonitor bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified(null));

		// shutdown plugin and check behaviour has gone
		ConfigurableApplicationContext child2 = loaded.getPluginContext().get(plugin2);
		child2.close();

		try {
			bean2.lastModified(null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// bean 2 still works
		assertEquals(999L, bean1.lastModified(null));

		ConfigurableApplicationContext child1 = loaded.getPluginContext().get(plugin1);
		child1.close();

		try {
			bean1.lastModified(null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// now reload the plugin, and see that behaviour returns
		ConfigurableApplicationContext applicationPlugin2 = loader.addApplicationPlugin(parent, new SimplePluginSpec(plugin2));
		bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified(null));

		loader.addApplicationPlugin(parent, new SimplePluginSpec(plugin1));
		bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified(null));

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		try {
			bean3.lastModified(null);
			fail();
		}
		catch (NoServiceException e) {
		}
		
		PluginSpec p2 = root.getPlugin(plugin2);
		
		ConfigurableApplicationContext child3 = loader.addApplicationPlugin(applicationPlugin2, 
				new SimplePluginSpec(p2, plugin3));
		assertEquals(100L, bean3.lastModified(null));
		
		child3.close();
		
		try {
			bean3.lastModified(null);
			fail();
		}
		catch (NoServiceException e) {
		}
	}


	public void testLoadAll() {

		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		loader = new DefaultApplicationContextLoader(new DefaultContextResourceHelper(locationResolver));
		SpringContextSpec spec = new SimpleSpringContextSpec("parentTestContext.xml",  
				new String[] { plugin1, plugin2 });
		final PluginSpec p2 = spec.getParentSpec().getPlugin(plugin2);
		new SimplePluginSpec(p2, plugin3);
		
		ApplicationContextSet loaded = loader.loadParentContext(spec, this.getClass().getClassLoader());

		ConfigurableApplicationContext parent = loaded.getContext();
		assertNotNull(parent);

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		bean3.lastModified(null);
		
		//check that all three plugins have loaded
		assertEquals(3, loaded.getPluginContext().size());
	}	
	
	public void testLoadContextFromClasspath() {
		final SimpleParentSpec parentSpec = new SimpleParentSpec(new String[] { "childcontainer/parent-context.xml" });
		ConfigurableApplicationContext parent = loader.loadContextFromClasspath(parentSpec, this.getClass()
				.getClassLoader());
		assertNotNull(parent.getBean("parent"));

		final Resource[] resources = new Resource[] { new ClassPathResource("beanset/imported-context.xml") };
		ConfigurableApplicationContext child = loader.loadContextFromResources(parent, resources, this.getClass()
				.getClassLoader());
		assertNotNull(child.getBean("bean2"));
	}

	public void testLoadContextFromResource() {
		final Resource[] resources = new Resource[] { new ClassPathResource("beanset/imported-context.xml") };
		ConfigurableApplicationContext context = loader.loadContextFromResources(null, resources, this.getClass()
				.getClassLoader());
		assertNotNull(context.getBean("bean2"));
	}

}
