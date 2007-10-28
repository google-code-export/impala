package net.java.impala.command.impl;

import net.java.impala.spring.plugin.ParentSpec;
import net.java.impala.testrun.PluginSpecAware;

public class SpecAwareClass implements PluginSpecAware {

	public ParentSpec getPluginSpec() {
		return null;
	}

}
