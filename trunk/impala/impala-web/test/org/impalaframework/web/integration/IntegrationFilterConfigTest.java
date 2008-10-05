package org.impalaframework.web.integration;

import java.util.HashMap;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;

import junit.framework.TestCase;

public class IntegrationFilterConfigTest extends TestCase {

	public void testGetFilterName() {
		final IntegrationFilterConfig config = new IntegrationFilterConfig(new HashMap<String, String>(), 
				EasyMock.createMock(ServletContext.class), 
				"myfilter");
	
		assertEquals("myfilter", config.getFilterName());
	}

}
