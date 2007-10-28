package net.java.impala.command.impl;

import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.testrun.PluginSpecAware;

public abstract class AbstractSpecAwareClass implements PluginSpecAware {

	public SpringContextSpec getPluginSpec() {
		return null;
	}

}
