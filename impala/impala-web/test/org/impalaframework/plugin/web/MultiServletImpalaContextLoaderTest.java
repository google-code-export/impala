package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.plugin.spec.ParentSpec;

@Deprecated
public class MultiServletImpalaContextLoaderTest extends TestCase {

	public void testGetWebApplicationSpec() {
		ServletContext servletContext = createMock(ServletContext.class);
		expect(servletContext.getInitParameter(WebConstants.WEBAPP_LOCATION_PARAM)).andReturn(
				"servlet-context1.xml, servlet-context2.xml");

		MultiServletImpalaContextLoader contextLoader = new MultiServletImpalaContextLoader();

		replay(servletContext);

		List<String> list = new ArrayList<String>();
		list.add("servlet-context1.xml");
		list.add("servlet-context2.xml");

		final ParentSpec webApplicationSpec = contextLoader.getWebApplicationSpec(servletContext);
		assertEquals(list, webApplicationSpec.getContextLocations());

		verify(servletContext);
	}
}
