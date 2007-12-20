package org.impalaframework.module.transition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.modification.Transition;
import org.springframework.util.Assert;

public class TransitionProcessorRegistry {

	private Map<Transition, TransitionProcessor> transitionProcessors;

	public TransitionProcessor getTransitionProcessor(Transition transition) {
		Assert.notNull(transitionProcessors, "transitionProcessors cannot be null");
		TransitionProcessor processor = transitionProcessors.get(transition);

		if (processor == null) {
			throw new NoServiceException("No " + TransitionProcessor.class.getSimpleName() + " set up for transition "
					+ transition);
		}

		return processor;
	}

	public void addTransitionProcessor(Transition transition, TransitionProcessor processor) {
		if (transitionProcessors == null) {
			transitionProcessors = new LinkedHashMap<Transition, TransitionProcessor>();
		}
		transitionProcessors.put(transition, processor);
	}

	public void setTransitionProcessors(Map<Transition, TransitionProcessor> transitionProcessors) {
		this.transitionProcessors = transitionProcessors;
	}

	public void setTransitionProcessorMap(Map<String, TransitionProcessor> transitionProcessors) {
		Set<String> keySet = transitionProcessors.keySet();
		for (String transitionName : keySet) {
			Transition transition = Transition.valueOf(transitionName.toUpperCase());
			addTransitionProcessor(transition, transitionProcessors.get(transitionName));
		}
	}

}
