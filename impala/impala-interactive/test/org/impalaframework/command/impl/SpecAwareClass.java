package org.impalaframework.command.impl;

import org.impalaframework.plugin.plugin.ParentSpec;
import org.impalaframework.testrun.PluginSpecAware;


public class SpecAwareClass implements PluginSpecAware {

	public ParentSpec getPluginSpec() {
		return null;
	}

}
