package org.impalaframework.command.impl;

import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpecProvider;


public abstract class AbstractSpecAwareClass implements PluginSpecProvider {

	public ParentSpec getPluginSpec() {
		return null;
	}

}
