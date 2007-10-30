package net.java.impala.spring.web;

import net.java.impala.spring.plugin.ParentSpec;
import net.java.impala.spring.plugin.SimplePluginSpec;

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
