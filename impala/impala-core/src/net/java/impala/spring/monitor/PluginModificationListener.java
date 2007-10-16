package net.java.impala.spring.monitor;

public interface PluginModificationListener {
	boolean pluginModified(PluginModificationEvent event);
}
