package net.java.impala.command.impl;

import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.testrun.SpringContextSpecAware;

public abstract class AbstractSpecAwareClass implements SpringContextSpecAware {

	public SpringContextSpec getPluginSpec() {
		return null;
	}

}
