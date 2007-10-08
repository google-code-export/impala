package net.java.impala.spring.plugin;

public interface ParentSpec {

	public String[] getParentContextLocations();

	boolean containsAll(ParentSpec alternative);

}
