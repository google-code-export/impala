package org.impalaframework.module.loader;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.module.loader.ModuleUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author Phil Zoio
 */
public class ModuleUtilsTest extends TestCase {
	
	public void testCastToBeanDefinitionRegistry() {
		BeanDefinitionRegistry bdr = ModuleUtils.castToBeanDefinitionRegistry(new DefaultListableBeanFactory());
		assertNotNull(bdr);
	
		try {
			ModuleUtils.castToBeanDefinitionRegistry(EasyMock.createMock(ConfigurableListableBeanFactory.class));
		}
		catch (IllegalStateException e) {
			assertTrue(e.getMessage().contains("is not an instance of BeanDefinitionRegistry"));
		}
	}

}
