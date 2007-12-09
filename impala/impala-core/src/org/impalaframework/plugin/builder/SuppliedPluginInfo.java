package org.impalaframework.plugin.builder;

import java.util.List;

public class SuppliedPluginInfo {
	private final String name;

	private final List<String> contextLocations;

	private final String overrides;

	private final String type;

	public SuppliedPluginInfo(final String name, final List<String> contextLocations, final String overrides,
			final String type) {
		super();
		this.name = name;
		this.contextLocations = contextLocations;
		this.overrides = overrides;
		this.type = type;
	}

	public List<String> getContextLocations() {
		return contextLocations;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getOverrides() {
		return overrides;
	}

}
