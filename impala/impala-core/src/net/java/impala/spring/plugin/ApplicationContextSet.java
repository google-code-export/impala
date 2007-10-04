package net.java.impala.spring.plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class ApplicationContextSet {

	private ConfigurableApplicationContext context;

	private Map<String, ConfigurableApplicationContext> pluginContext = new ConcurrentHashMap<String, ConfigurableApplicationContext>();

	public ApplicationContextSet(ConfigurableApplicationContext context) {
		super();
		Assert.notNull(context);
		this.context = context;
	}

	public ConfigurableApplicationContext getContext() {
		return context;
	}

	public Map<String, ConfigurableApplicationContext> getPluginContext() {
		return pluginContext;
	}

}
