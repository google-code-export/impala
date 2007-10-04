package net.java.impala.spring.resolver;

import java.io.File;
import java.util.Properties;

import junit.framework.TestCase;

public class WebPropertyClassLocationResolverTest extends TestCase {

	private Properties props;

	private WebPropertyClassLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		props = new Properties();
		super.setUp();
	}

	public void testGetSpringClassLocations() {
		props.put("workspace.root", System.getProperty("java.io.tmpdir"));
		props.put("impala.plugin.class.dir", "deploy/webclasses");
		resolver = new WebPropertyClassLocationResolver(props);
		File location = resolver.getParentWebClassLocation("wineorder", "servlet1");
		assertEquals(new File(System.getProperty("java.io.tmpdir")
				+ "/wineorder-servlet1/deploy/webclasses"), location);
	}

}
