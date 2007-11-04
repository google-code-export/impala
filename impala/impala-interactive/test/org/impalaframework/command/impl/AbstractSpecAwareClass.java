package org.impalaframework.command.impl;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.testrun.PluginSpecAware;


public abstract class AbstractSpecAwareClass implements PluginSpecAware {

	public ParentSpec getPluginSpec() {
		return null;
	}

}
