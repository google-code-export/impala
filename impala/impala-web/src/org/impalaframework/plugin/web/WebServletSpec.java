package org.impalaframework.plugin.web;


import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.springframework.util.Assert;

public class WebServletSpec extends SimplePluginSpec {

	private static final long serialVersionUID = 1L;
	
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
