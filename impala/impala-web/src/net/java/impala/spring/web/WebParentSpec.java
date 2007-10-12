package net.java.impala.spring.web;

import net.java.impala.spring.plugin.SimpleParentSpec;

public class WebParentSpec extends SimpleParentSpec {

	private String name;

	public WebParentSpec(String name, String[] parentContextLocations) {
		super(parentContextLocations);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
