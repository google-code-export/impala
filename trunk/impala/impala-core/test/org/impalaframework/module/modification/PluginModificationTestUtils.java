package org.impalaframework.module.modification;

import org.impalaframework.module.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.SimpleParentSpec;

public class PluginModificationTestUtils {

	static ParentSpec spec(String contextString, String pluginString) {
		String[] locations = contextString.split(",");
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(new SimpleParentSpec(locations),
				pluginString);
		ParentSpec parentSpec = builder.getPluginSpec();
		return parentSpec;
	}

}
