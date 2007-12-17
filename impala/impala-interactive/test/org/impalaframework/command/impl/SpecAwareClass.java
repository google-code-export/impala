package org.impalaframework.command.impl;

import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpecProvider;


public class SpecAwareClass implements PluginSpecProvider {

	public ParentSpec getPluginSpec() {
		return null;
	}

}
