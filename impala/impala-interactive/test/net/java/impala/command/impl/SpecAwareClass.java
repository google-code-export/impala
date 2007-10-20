package net.java.impala.command.impl;

import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.testrun.SpringContextSpecAware;

public class SpecAwareClass implements SpringContextSpecAware {

	public SpringContextSpec getPluginSpec() {
		return null;
	}

}
