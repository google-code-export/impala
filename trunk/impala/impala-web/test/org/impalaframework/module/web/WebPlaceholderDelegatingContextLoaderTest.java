package org.impalaframework.module.web;

import junit.framework.TestCase;

import org.impalaframework.module.web.WebPlaceholderDelegatingContextLoader;
import org.springframework.context.ConfigurableApplicationContext;

public class WebPlaceholderDelegatingContextLoaderTest extends TestCase {

	public final void testLoadApplicationContext() {
		WebPlaceholderDelegatingContextLoader loader = new WebPlaceholderDelegatingContextLoader();
		ConfigurableApplicationContext context = loader.loadApplicationContext(null, null);
		assertNotNull(context);
		assertTrue(context.isActive());
	}

}
