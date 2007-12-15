package org.impalaframework.plugin.modification;

import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;

public class PluginModificationTestUtils {

	static ParentSpec spec(String contextString, String pluginString) {
		String[] locations = contextString.split(",");
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(new SimpleParentSpec(locations),
				pluginString);
		ParentSpec parentSpec = builder.getPluginSpec();
		return parentSpec;
	}

}
