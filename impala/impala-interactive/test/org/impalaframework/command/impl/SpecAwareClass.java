package org.impalaframework.command.impl;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;


public class SpecAwareClass implements PluginSpecProvider {

	public ParentSpec getPluginSpec() {
		return null;
	}

}
