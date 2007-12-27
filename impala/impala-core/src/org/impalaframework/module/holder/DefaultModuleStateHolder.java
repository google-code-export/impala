/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.module.holder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.Transition;
import org.impalaframework.module.modification.TransitionSet;
import org.impalaframework.module.transition.TransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class DefaultModuleStateHolder implements ModuleStateHolder {

	final Logger logger = LoggerFactory.getLogger(DefaultModuleStateHolder.class);

	private RootModuleDefinition rootModuleDefinition;

	private TransitionProcessorRegistry transitionProcessorRegistry;

	private Map<String, ConfigurableApplicationContext> moduleContexts = new HashMap<String, ConfigurableApplicationContext>();

	public DefaultModuleStateHolder() {
		super();
	}

	public void processTransitions(TransitionSet transitions) {
		
		try {
			Assert.notNull(transitionProcessorRegistry, TransitionProcessorRegistry.class.getSimpleName() + " cannot be null");

			Collection<? extends ModuleStateChange> changes = transitions.getModuleTransitions();

			for (ModuleStateChange change : changes) {
				Transition transition = change.getTransition();
				ModuleDefinition moduleDefinition = change.getModuleDefinition();

				TransitionProcessor transitionProcessor = transitionProcessorRegistry.getTransitionProcessor(transition);
				transitionProcessor.process(this, rootModuleDefinition, transitions.getNewRootModuleDefinition(), moduleDefinition);
			}
		} finally {
			rootModuleDefinition = transitions.getNewRootModuleDefinition();
		}
	}

	public ConfigurableApplicationContext getParentContext() {
		return moduleContexts.get(RootModuleDefinition.NAME);
	}

	public ConfigurableApplicationContext getModule(String moduleName) {
		return moduleContexts.get(moduleName);
	}

	public RootModuleDefinition getRootModuleDefinition() {
		return rootModuleDefinition;
	}

	public RootModuleDefinition cloneRootModuleDefinition() {
		return (RootModuleDefinition) SerializationUtils.clone(rootModuleDefinition);
	}

	public boolean hasModule(String moduleName) {
		return (rootModuleDefinition.findChildDefinition(moduleName, true) != null);
	}

	public boolean hasParentContext() {
		return getRootModuleDefinition() != null;
	}

	public Map<String, ConfigurableApplicationContext> getModuleContexts() {
		return Collections.unmodifiableMap(moduleContexts);
	}
	
	public void putModule(String name, ConfigurableApplicationContext context) {
		moduleContexts.put(name, context);
	}

	public ConfigurableApplicationContext removeModule(String moduleName) {
		return moduleContexts.remove(moduleName);
	}
	
	public RootModuleDefinition getModuleDefinition() {
		return getRootModuleDefinition();
	}

	/* ************************* protected methods ************************* */

	protected void setParentSpec(RootModuleDefinition rootModuleDefinition) {
		this.rootModuleDefinition = rootModuleDefinition;
	}

	/* ******************** injected setters ******************** */
	
	public void setTransitionProcessorRegistry(TransitionProcessorRegistry transitionProcessorRegistry) {
		this.transitionProcessorRegistry = transitionProcessorRegistry;
	}

}
