package org.impalaframework.plugin.builder;

import java.util.List;

public class SuppliedPluginInfo {
	private final String name;

	private final List<String> contextLocations;

	private final String overrides;

	private final String factoryName;

	public SuppliedPluginInfo(final String name, final List<String> contextLocations, final String overrides,
			final String factoryName) {
		super();
		this.name = name;
		this.contextLocations = contextLocations;
		this.overrides = overrides;
		this.factoryName = factoryName;
	}

	public List<String> getContextLocations() {
		return contextLocations;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public String getName() {
		return name;
	}

	public String getOverrides() {
		return overrides;
	}

}
