package net.java.impala.command.impl;

import net.java.impala.spring.plugin.ParentSpec;
import net.java.impala.testrun.PluginSpecAware;

public abstract class AbstractSpecAwareClass implements PluginSpecAware {

	public ParentSpec getPluginSpec() {
		return null;
	}

}
