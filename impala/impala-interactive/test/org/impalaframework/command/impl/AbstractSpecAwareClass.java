package org.impalaframework.command.impl;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;


public abstract class AbstractSpecAwareClass implements PluginSpecProvider {

	public ParentSpec getPluginSpec() {
		return null;
	}

}
