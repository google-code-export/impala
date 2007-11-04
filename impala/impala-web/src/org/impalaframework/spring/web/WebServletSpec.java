package org.impalaframework.spring.web;


import org.impalaframework.spring.plugin.ParentSpec;
import org.impalaframework.spring.plugin.SimplePluginSpec;
import org.springframework.util.Assert;

public class WebServletSpec extends SimplePluginSpec {

	private String[] contextLocations;

	@Override
	public String getType() {
		return "servlet";
	}

	public WebServletSpec(ParentSpec parentSpec, String name, String[] contextLocations) {
		super(parentSpec, name);
		Assert.notEmpty(contextLocations);
		this.contextLocations = contextLocations;
	}

	public String[] getContextLocations() {
		return contextLocations;
	}

}
