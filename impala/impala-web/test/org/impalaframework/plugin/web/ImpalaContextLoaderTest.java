package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.web.RegistryBasedImpalaContextLoader;

import junit.framework.TestCase;

public class ImpalaContextLoaderTest extends TestCase {

	public void testGetPluginSpec() {
		
		ServletContext servletContext = createMock(ServletContext.class);
		expect(servletContext.getInitParameter(RegistryBasedImpalaContextLoader.CONFIG_LOCATION_PARAM)).andReturn(
				"context1.xml, context2.xml");
		expect(servletContext.getInitParameter(RegistryBasedImpalaContextLoader.PLUGIN_NAMES_PARAM)).andReturn(
				"p1, p2, p3");

		RegistryBasedImpalaContextLoader contextLoader = new RegistryBasedImpalaContextLoader();

		replay(servletContext);

		ParentSpec parentSpec = contextLoader.getPluginSpec(servletContext);

		List<String> list = new ArrayList<String>();
		list.add("context1.xml");
		list.add("context2.xml");

		assertEquals(list, parentSpec.getContextLocations());

		assertTrue(Arrays.equals(new String[] { "p1", "p2", "p3" }, parentSpec.getPluginNames().toArray(new String[3])));

		verify(servletContext);
	}

}
