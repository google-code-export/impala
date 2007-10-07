package net.java.impala.spring.plugin;

import java.util.Map;

public interface BeansetAwarePlugin extends Plugin {
	Map<String, String> getOverrides();
}
