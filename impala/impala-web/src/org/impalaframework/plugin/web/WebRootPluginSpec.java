package org.impalaframework.plugin.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.springframework.util.Assert;

public class WebRootPluginSpec extends SimplePluginSpec {

	private static final long serialVersionUID = 1L;

	private List<String> contextLocations;

	@Override
	public String getType() {
		return WebPluginTypes.WEB_ROOT;
	}

	public WebRootPluginSpec(PluginSpec pluginSpec, String name, String[] contextLocations) {
		super(pluginSpec, name);
		Assert.notEmpty(contextLocations);

		this.contextLocations = new ArrayList<String>();
		for (int i = 0; i < contextLocations.length; i++) {
			this.contextLocations.add(contextLocations[i]);
		}
	}

	public List<String> getContextLocations() {
		return Collections.unmodifiableList(contextLocations);
	}

}
