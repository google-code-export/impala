package org.impalaframework.web.loader;

import junit.framework.TestCase;

import org.impalaframework.web.loader.WebPlaceholderDelegatingContextLoader;
import org.springframework.context.ConfigurableApplicationContext;

public class WebPlaceholderDelegatingContextLoaderTest extends TestCase {

	public final void testLoadApplicationContext() {
		WebPlaceholderDelegatingContextLoader loader = new WebPlaceholderDelegatingContextLoader();
		ConfigurableApplicationContext context = loader.loadApplicationContext(null, null);
		assertNotNull(context);
		assertTrue(context.isActive());
	}

}
