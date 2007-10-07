package net.java.impala.spring.plugin;

import java.util.Map;

public interface BeansetPluginSpec extends PluginSpec {
	Map<String, String> getOverrides();
}
