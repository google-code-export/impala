package org.impalaframework.spring.plugin;

import java.io.File;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.classloader.CustomClassLoader;
import org.impalaframework.spring.plugin.ApplicationContextSet;
import org.impalaframework.spring.plugin.PluginSpecBuilder;
import org.impalaframework.spring.plugin.PluginUtils;
import org.impalaframework.spring.plugin.SimplePluginSpecBuilder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author Phil Zoio
 */
public class PluginUtilsTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";
	
	private ApplicationContextSet set;

	private GenericApplicationContext context;

	private PluginSpecBuilder spec;

	public void setUp() {
		spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });
		set = new ApplicationContextSet();
		context = new GenericApplicationContext();
		set.getPluginContext().put(spec.getParentSpec().getName(), context);
	}
			
	
	public void testGetParentClassLoader() {
		final CustomClassLoader cl = new CustomClassLoader(new File[]{});
		context.setClassLoader(cl);
		assertEquals(cl, PluginUtils.getParentClassLoader(set, spec.getParentSpec().getPlugin(plugin2)));
	}
	
	public void testCastToBeanDefinitionRegistry() {
		BeanDefinitionRegistry bdr = PluginUtils.castToBeanDefinitionRegistry(new DefaultListableBeanFactory());
		assertNotNull(bdr);
	
		try {
			PluginUtils.castToBeanDefinitionRegistry(EasyMock.createMock(ConfigurableListableBeanFactory.class));
		}
		catch (IllegalStateException e) {
			assertTrue(e.getMessage().contains("is not an instance of BeanDefinitionRegistry"));
		}
	}

}
