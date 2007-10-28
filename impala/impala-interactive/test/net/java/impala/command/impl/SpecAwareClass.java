package net.java.impala.command.impl;

import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.testrun.PluginSpecAware;

public class SpecAwareClass implements PluginSpecAware {

	public SpringContextSpec getPluginSpec() {
		return null;
	}

}
