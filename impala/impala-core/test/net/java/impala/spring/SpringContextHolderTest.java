package net.java.impala.spring;

import junit.framework.TestCase;
import net.java.impala.classloader.DefaultContextResourceHelper;
import net.java.impala.location.PropertyClassLocationResolver;
import net.java.impala.monitor.FileMonitor;
import net.java.impala.spring.plugin.NoServiceException;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SimplePluginSpec;
import net.java.impala.spring.util.ApplicationContextLoader;

import org.springframework.context.ApplicationContext;

public class SpringContextHolderTest extends TestCase {

	private SpringContextHolder holder;

	public void setUp() {
		System.setProperty("impala.plugin.prefix", "impala-sample-dynamic");
		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		ApplicationContextLoader loader = new ApplicationContextLoader(new DefaultContextResourceHelper(locationResolver));
		holder = new SpringContextHolder(loader);
	}

	public void testSpringContextHolder() {
		try {

			PluginSpec spec = new SimplePluginSpec("parentTestContext.xml", new String[] { "plugin1", "plugin2" });
			holder.loadParentContext(this.getClass().getClassLoader(), spec);

			ApplicationContext parent = holder.getContext();
			assertNotNull(parent);
			assertEquals(2, holder.getPlugins().size());

			FileMonitor bean1 = (FileMonitor) parent.getBean("bean1");
			assertEquals(999L, bean1.lastModified(null));

			FileMonitor bean2 = (FileMonitor) parent.getBean("bean2");
			assertEquals(100L, bean2.lastModified(null));

			// shutdown plugin and check behaviour has gone
			holder.removePlugin("plugin2");

			try {
				bean2.lastModified(null);
				fail();
			}
			catch (NoServiceException e) {
			}

			// bean 2 still works
			assertEquals(999L, bean1.lastModified(null));

			holder.removePlugin("plugin1");

			try {
				bean1.lastModified(null);
				fail();
			}
			catch (NoServiceException e) {
			}

			// now reload the plugin, and see that behaviour returns
			holder.addPlugin("plugin2");
			bean2 = (FileMonitor) parent.getBean("bean2");
			assertEquals(100L, bean2.lastModified(null));

			holder.addPlugin("plugin1");
			bean1 = (FileMonitor) parent.getBean("bean1");
			assertEquals(999L, bean1.lastModified(null));

			//shut parent context and see that NoServiceException comes
			holder.shutParentConext();

			try {
				bean1.lastModified(null);
				fail();
			}
			catch (NoServiceException e) {
			}

			try {
				bean2.lastModified(null);
				fail();
			}
			catch (NoServiceException e) {
			}
		}
		finally {
			System.clearProperty("impala.plugin.prefix");
		}
	}

}