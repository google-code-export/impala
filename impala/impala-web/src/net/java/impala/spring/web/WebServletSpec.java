package net.java.impala.spring.web;

import net.java.impala.spring.plugin.SimpleParentSpec;

public class WebServletSpec extends SimpleParentSpec {

	private String name;

	@Override
	public String getType() {
		return "servlet";
	}

	public WebServletSpec(String name, String[] parentContextLocations) {
		super(parentContextLocations);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
