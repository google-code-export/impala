package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.web.WebXmlBasedImpalaContextLoader;

import junit.framework.TestCase;

public class ImpalaContextLoaderTest extends TestCase {

	private WebXmlBasedImpalaContextLoader contextLoader;
	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		contextLoader = new WebXmlBasedImpalaContextLoader();
		servletContext = createMock(ServletContext.class);
	}

	public void testBootstrapLocations() throws Exception {
		String[] locations = contextLoader.getBootstrapContextLocations(EasyMock.createMock(ServletContext.class));
		assertTrue(locations.length > 0);
	}

	public void testGetPluginSpec() {
		expect(servletContext.getInitParameter(WebXmlBasedImpalaContextLoader.CONFIG_LOCATION_PARAM)).andReturn(
				"context1.xml, context2.xml");
		expect(servletContext.getInitParameter(WebConstants.PLUGIN_NAMES_PARAM)).andReturn("p1, p2, p3");

		WebXmlBasedImpalaContextLoader contextLoader = new WebXmlBasedImpalaContextLoader();

		replay(servletContext);

		ParentSpec parentSpec = contextLoader.getPluginSpec(servletContext);

		List<String> list = new ArrayList<String>();
		list.add("context1.xml");
		list.add("context2.xml");

		assertEquals(list, parentSpec.getContextLocations());

		assertTrue(Arrays.equals(new String[] { "p1", "p2", "p3" }, parentSpec.getPluginNames().toArray(new String[3])));

		verify(servletContext);
	}

	public void testGetChildPluginSpecString() {

		expect(servletContext.getInitParameter(WebConstants.PLUGIN_NAMES_PARAM)).andReturn("plugin1, plugin2");

		replay(servletContext);
		assertEquals("plugin1, plugin2", contextLoader.getPluginDefinitionString(servletContext));
		verify(servletContext);
	}

}
