package org.impalaframework.plugin.web;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;

public class ImpalaPluginServlet extends ImpalaRootServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected PluginSpec newPluginSpec(String pluginName, ParentSpec newSpec) {
		return new ServletPluginSpec(newSpec, pluginName, getSpringConfigLocations());
	}

}
