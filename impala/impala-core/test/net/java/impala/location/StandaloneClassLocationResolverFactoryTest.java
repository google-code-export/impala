package net.java.impala.location;

import net.java.impala.location.PropertyClassLocationResolver;
import net.java.impala.location.StandaloneClassLocationResolverFactory;
import junit.framework.TestCase;

public class StandaloneClassLocationResolverFactoryTest extends TestCase {

	private StandaloneClassLocationResolverFactory factory;

	@Override
	protected void setUp() throws Exception {
		factory = new StandaloneClassLocationResolverFactory();
	}

	public void testFactoryDefault() throws Exception {
		PropertyClassLocationResolver resolver = (PropertyClassLocationResolver) factory.getClassLocationResolver();
		assertEquals("bin", resolver.getProperty("impala.plugin.class.dir"));
	}

	public void testFactoryWithPathNotExists() throws Exception {
		try {
			System.setProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH,
					"resources/locations/filethatdoesnotexist");
			factory.getClassLocationResolver();
		}
		catch (IllegalStateException e) {
			assertEquals("System property 'impala.execution.file.path' points to location which does not exist: resources/locations/filethatdoesnotexist", e.getMessage());
		}
		finally {
			System.clearProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH);
		}
	}

	public void testFactoryWithNameNotExists() throws Exception {
		try {
			System.setProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME,
					"resources/locations/filethatdoesnotexist");
			factory.getClassLocationResolver();
		}
		catch (IllegalStateException e) {
			assertEquals("System property 'impala.execution.file.name' points to classpath location which could not be found: resources/locations/filethatdoesnotexist", e.getMessage());
		}
		finally {
			System.clearProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME);
		}
	}

	public void testFactoryWithPath() throws Exception {
		try {
			System.setProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME,
					"locations/execution-2.properties");
			PropertyClassLocationResolver resolver = (PropertyClassLocationResolver) factory.getClassLocationResolver();
			assertEquals("classdir2", resolver.getProperty("impala.plugin.class.dir"));
		}
		finally {
			System.clearProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH);
		}
	}

	public void testFactoryWithName() throws Exception {
		try {
			System.setProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH,
					"resources/locations/execution-1.properties");
			System.setProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME,
					"locations/execution-2.properties");
			PropertyClassLocationResolver resolver = (PropertyClassLocationResolver) factory.getClassLocationResolver();
			assertEquals("classdir1", resolver.getProperty("impala.plugin.class.dir"));
		}
		finally {
			System.clearProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_PATH);
			System.clearProperty(StandaloneClassLocationResolverFactory.EXECUTION_PROPERTIES_FILE_NAME);
		}
	}
}
