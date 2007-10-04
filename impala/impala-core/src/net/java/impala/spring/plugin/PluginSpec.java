package net.java.impala.spring.plugin;

public interface PluginSpec {

	public abstract String[] getParentContextLocations();

	public abstract String[] getPluginNames();

}