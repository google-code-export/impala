package net.java.impala.spring.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;

import javax.servlet.ServletContext;

import junit.framework.TestCase;
import net.java.impala.spring.plugin.ParentSpec;
import net.java.impala.spring.plugin.SpringContextSpec;

public class ImpalaContextLoaderTest extends TestCase {
	
	public void testGetPluginSpec() {
		ServletContext servletContext = createMock(ServletContext.class);
		expect(servletContext.getInitParameter(RegistryBasedImpalaContextLoader.CONFIG_LOCATION_PARAM)).andReturn(
				"context1.xml, context2.xml");
		expect(servletContext.getInitParameter(RegistryBasedImpalaContextLoader.PLUGIN_NAMES_PARAM)).andReturn("p1, p2, p3");

		RegistryBasedImpalaContextLoader contextLoader = new RegistryBasedImpalaContextLoader();

		replay(servletContext);

		final SpringContextSpec pSpec = contextLoader.getPluginSpec(servletContext);
		ParentSpec parentSpec = pSpec.getParentSpec();
		assertTrue(Arrays.equals(new String[] { "context1.xml", "context2.xml" }, parentSpec
				.getContextLocations()));

		assertTrue(Arrays.equals(new String[] { "p1", "p2", "p3" }, parentSpec.getPluginNames().toArray(
				new String[3])));

		verify(servletContext);
	}

}
