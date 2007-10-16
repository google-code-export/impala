package net.java.impala.spring.plugin;

import java.io.File;

import junit.framework.TestCase;
import net.java.impala.classloader.CustomClassLoader;

import org.springframework.context.support.GenericApplicationContext;

/**
 * @author Phil Zoio
 */
public class PluginUtilsTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";
	
	private ApplicationContextSet set;

	private GenericApplicationContext context;

	private SpringContextSpec spec;

	public void setUp() {
		spec = new SimpleSpringContextSpec("parentTestContext.xml", new String[] { plugin1, plugin2 });
		set = new ApplicationContextSet();
		context = new GenericApplicationContext();
		set.getPluginContext().put(spec.getParentSpec().getName(), context);
	}
			
	
	public final void testGetParentClassLoader() {
		final CustomClassLoader cl = new CustomClassLoader(new File[]{});
		context.setClassLoader(cl);
		assertEquals(cl, PluginUtils.getParentClassLoader(set, spec.getParentSpec().getPlugin(plugin2)));
	}

}
