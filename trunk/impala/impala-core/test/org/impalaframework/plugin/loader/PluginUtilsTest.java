package org.impalaframework.plugin.loader;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author Phil Zoio
 */
public class PluginUtilsTest extends TestCase {
	
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
