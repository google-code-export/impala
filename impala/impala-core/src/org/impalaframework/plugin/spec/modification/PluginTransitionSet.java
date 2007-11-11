package org.impalaframework.plugin.spec.modification;

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.plugin.spec.ParentSpec;


public class PluginTransitionSet {
	
	private Collection<PluginTransition> pluginTransitions = new ArrayList<PluginTransition>();
	
	private ParentSpec originalSpec;
	private ParentSpec newSpec;

	
	
	public ParentSpec getNewSpec() {
		return newSpec;
	}

	public ParentSpec getOriginalSpec() {
		return originalSpec;
	}

	public Collection<PluginTransition> getPluginTransitions() {
		return pluginTransitions;
	}
	
	
	
	
	
	
}
