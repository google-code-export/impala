package org.impalaframework.plugin.spec.modification;

import java.util.Collections;

import org.impalaframework.plugin.spec.ParentSpec;

public class PluginModificationCalculator {
	
	@SuppressWarnings("unchecked")
	public PluginTransitionSet getTransitions(ParentSpec originalSpec, ParentSpec newSpec) {
		
		if (originalSpec == null && newSpec == null) {
			return new PluginTransitionSet(Collections.EMPTY_LIST, null);
		}
		if (originalSpec != null && newSpec == null) {
			return new PluginTransitionSet(Collections.EMPTY_LIST, originalSpec);
		}
		if (newSpec != null && originalSpec == null) {
			return new PluginTransitionSet(Collections.EMPTY_LIST, newSpec);
		}
		return new PluginTransitionSet(Collections.EMPTY_LIST, null);
	}
}
