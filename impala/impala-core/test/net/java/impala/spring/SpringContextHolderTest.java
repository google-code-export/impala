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

package net.java.impala.spring;

import java.io.File;

import junit.framework.TestCase;
import net.java.impala.classloader.DefaultContextResourceHelper;
import net.java.impala.location.PropertyClassLocationResolver;
import net.java.impala.monitor.FileMonitor;
import net.java.impala.spring.plugin.NoServiceException;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SimplePluginSpec;
import net.java.impala.spring.plugin.SimpleSpringContextSpec;
import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.spring.util.ApplicationContextLoader;
import net.java.impala.spring.util.DefaultApplicationContextLoader;

import org.springframework.context.ApplicationContext;

public class SpringContextHolderTest extends TestCase {

	private SpringContextHolder holder;

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	public void setUp() {
		System.setProperty("impala.plugin.prefix", "impala-sample-dynamic");
		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		ApplicationContextLoader loader = new DefaultApplicationContextLoader(new DefaultContextResourceHelper(
				locationResolver));
		holder = new SpringContextHolder(loader);
	}

	public void testFindPlugin() {
		SpringContextSpec spec = new SimpleSpringContextSpec("parentTestContext.xml", new String[] { plugin1, plugin2 });
		PluginSpec p2 = spec.getParentSpec().getPlugin(plugin2);
		new SimplePluginSpec(p2, plugin3);

		holder.setSpringContextSpec(spec);

		assertNotNull(holder.getPlugin(plugin1));
		assertNotNull(holder.getPlugin(plugin2));
		assertNotNull(holder.getPlugin(plugin3));
		
		assertNull(holder.getPlugin("plugin3"));
		assertNotNull(holder.findPluginLike("plugin3"));
	}


	
	public void testSpringContextHolder() {

		SpringContextSpec spec = new SimpleSpringContextSpec("parentTestContext.xml", new String[] { plugin1, plugin2 });
		holder.loadParentContext(this.getClass().getClassLoader(), spec);
		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));

		ApplicationContext parent = holder.getContext();
		assertNotNull(parent);
		assertEquals(2, holder.getPlugins().size());

		FileMonitor bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File)null));

		FileMonitor bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File)null));

		// shutdown plugin and check behaviour has gone
		holder.removePlugin(plugin2);
		assertFalse(holder.hasPlugin(plugin2));

		try {
			bean2.lastModified((File)null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// bean 2 still works
		assertEquals(999L, bean1.lastModified((File)null));

		holder.removePlugin(plugin1);
		assertFalse(holder.hasPlugin(plugin2));

		try {
			bean1.lastModified((File)null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// now reload the plugin, and see that behaviour returns
		holder.addPlugin(new SimplePluginSpec(plugin2));
		bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File)null));

		holder.addPlugin(new SimplePluginSpec(plugin1));
		bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File)null));

		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));

		// shut parent context and see that NoServiceException comes
		holder.shutParentConext();

		try {
			bean1.lastModified((File)null);
			fail();
		}
		catch (NoServiceException e) {
		}

		try {
			bean2.lastModified((File)null);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

}