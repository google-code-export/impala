package org.impalaframework.plugin.transition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.plugin.spec.modification.PluginTransition;
import org.springframework.util.Assert;

public class TransitionProcessorRegistry {

	private Map<PluginTransition, TransitionProcessor> transitionProcessors;

	public TransitionProcessor getTransitionProcessor(PluginTransition transition) {
		Assert.notNull(transitionProcessors, "transitionProcessors cannot be null");
		TransitionProcessor processor = transitionProcessors.get(transition);

		if (processor == null) {
			throw new NoServiceException("No " + TransitionProcessor.class.getSimpleName() + " set up for transition "
					+ transition);
		}

		return processor;
	}

	public void addTransitionProcessor(PluginTransition transition, TransitionProcessor processor) {
		if (transitionProcessors == null) {
			transitionProcessors = new LinkedHashMap<PluginTransition, TransitionProcessor>();
		}
		transitionProcessors.put(transition, processor);
	}

	public void setTransitionProcessors(Map<PluginTransition, TransitionProcessor> transitionProcessors) {
		this.transitionProcessors = transitionProcessors;
	}

	public void setTransitionProcessorMap(Map<String, TransitionProcessor> transitionProcessors) {
		Set<String> keySet = transitionProcessors.keySet();
		for (String transitionName : keySet) {
			PluginTransition transition = PluginTransition.valueOf(transitionName);
			addTransitionProcessor(transition, transitionProcessors.get(transitionName));
		}
	}

}
