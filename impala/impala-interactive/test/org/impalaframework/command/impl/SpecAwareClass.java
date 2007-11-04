package org.impalaframework.command.impl;

import org.impalaframework.spring.plugin.ParentSpec;
import org.impalaframework.testrun.PluginSpecAware;


public class SpecAwareClass implements PluginSpecAware {

	public ParentSpec getPluginSpec() {
		return null;
	}

}
