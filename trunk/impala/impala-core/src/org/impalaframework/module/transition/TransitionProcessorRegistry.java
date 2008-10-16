/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.module.transition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.Transition;
import org.impalaframework.module.TransitionProcessor;
import org.springframework.util.Assert;

public class TransitionProcessorRegistry {

	private Map<Transition, TransitionProcessor> transitionProcessors = new LinkedHashMap<Transition, TransitionProcessor>();

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
		transitionProcessors.put(transition, processor);
	}

	void setTransitionProcessorEnum(Map<Transition, TransitionProcessor> transitionProcessors) {
		this.transitionProcessors = transitionProcessors;
	}

	public void setTransitionProcessors(Map<String, TransitionProcessor> transitionProcessors) {
		Set<String> keySet = transitionProcessors.keySet();
		for (String transitionName : keySet) {
			Transition transition = Transition.valueOf(transitionName.toUpperCase());
			addTransitionProcessor(transition, transitionProcessors.get(transitionName));
		}
	}

}
