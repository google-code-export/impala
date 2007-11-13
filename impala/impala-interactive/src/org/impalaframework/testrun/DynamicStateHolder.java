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

package org.impalaframework.testrun;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.SerializationUtils;
import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.modification.PluginModificationCalculator;
import org.impalaframework.plugin.spec.modification.PluginStateChange;
import org.impalaframework.plugin.spec.modification.PluginTransition;
import org.impalaframework.plugin.spec.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.transition.PluginStateManager;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.StandaloneClassLocationResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class DynamicStateHolder {

	static final Logger logger = LoggerFactory.getLogger(DynamicStateHolder.class);

	private static PluginStateManager holder = null;

	private static PluginModificationCalculator calculator = null;

	public static void init() {
		if (holder == null) {
			ClassLocationResolver classLocationResolver = new StandaloneClassLocationResolverFactory()
					.getClassLocationResolver();
			ApplicationContextLoader contextLoader = new ContextLoaderFactory().newContextLoader(classLocationResolver,
					false, false);
			setContextLoader(contextLoader);
		}
	}

	public static void setContextLoader(ApplicationContextLoader applicationContextLoader) {
		if (holder == null) {
			holder = new PluginStateManager();
			holder.setApplicationContextLoader(applicationContextLoader);
			calculator = new PluginModificationCalculator();
		}
	}

	public static ApplicationContextLoader getContextLoader() {
		if (holder != null) {
			return holder.getContextLoader();
		}
		return null;
	}

	public static void init(PluginSpecProvider pluginSpecProvider) {
		init();
		ParentSpec providedSpec = getPluginSpec(pluginSpecProvider);

		if (holder.getParentContext() == null) {
			PluginTransitionSet transitions = calculator.getTransitions(null, providedSpec);
			holder.processTransitions(transitions);
		}
		else {
			ParentSpec oldSpec = holder.getParentSpec();
			PluginTransitionSet transitions = calculator.getTransitions(oldSpec, providedSpec);
			holder.processTransitions(transitions);
		}
	}

	private static ParentSpec getPluginSpec(Object test) {
		ParentSpec pluginSpec = null;
		if (test instanceof PluginSpecProvider) {
			PluginSpecProvider p = (PluginSpecProvider) test;
			pluginSpec = p.getPluginSpec();
		}
		// FIXME assert not null
		return pluginSpec;
	}

	public static boolean reload(PluginSpecProvider pluginSpecProvider, String plugin) {
		ParentSpec oldSpec = holder.getParentSpec();
		ParentSpec newSpec = pluginSpecProvider.getPluginSpec();
		PluginTransitionSet transitions = calculator.reload(oldSpec, newSpec, plugin);
		holder.processTransitions(transitions);
		return !transitions.getPluginTransitions().isEmpty();
	}

	public static String reloadLike(PluginSpecProvider pluginSpecProvider, String plugin) {
		ParentSpec oldSpec = holder.getParentSpec();
		ParentSpec newSpec = pluginSpecProvider.getPluginSpec();
		
		//FIXME find plugin here, and return name
		
		PluginTransitionSet transitions = calculator.reloadLike(oldSpec, newSpec, plugin);
		holder.processTransitions(transitions);
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();

		if (!pluginTransitions.isEmpty()) {
			Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();

			// FIXME do we really want to do this
			PluginStateChange change = null;
			for (int i = 0; i < pluginTransitions.size(); i++) {
				change = iterator.next();
				if (PluginTransition.UNLOADED_TO_LOADED.equals(change.getTransition())) {
					break;
				}
			}
			return change.getPluginSpec().getName();
		}
		return null;
	}

	public static boolean remove(String plugin) {
		ParentSpec oldSpec = holder.getParentSpec();
		ParentSpec newSpec = (ParentSpec) SerializationUtils.clone(oldSpec);
		PluginSpec pluginToRemove = newSpec.findPlugin(plugin, true);

		if (pluginToRemove != null) {
			PluginSpec parent = pluginToRemove.getParent();
			if (parent != null) {
				parent.remove(plugin);
				pluginToRemove.setParent(null);

				PluginTransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
				holder.processTransitions(transitions);
				return true;
			}
			// FIXME if parent is null, then we are talking about parent!
		}
		return false;
	}

	public static void addPlugin(final PluginSpec loadedPlugin) {
		ParentSpec oldSpec = holder.getParentSpec();
		ParentSpec newSpec = (ParentSpec) SerializationUtils.clone(oldSpec);

		newSpec.add(loadedPlugin);
		loadedPlugin.setParent(newSpec);

		PluginTransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
		holder.processTransitions(transitions);
	}

	public static ApplicationContext get() {
		return holder.getParentContext();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(PluginSpecProvider test, String string, Class<T> t) {
		init(test);
		ApplicationContext context = get();
		return (T) context.getBean(string);
	}

	public static void setPluginStateManager(PluginStateManager stateManager) {
		holder = stateManager;
	}

	public static void setPluginModificationCalculator(PluginModificationCalculator stateManager) {
		calculator = stateManager;
	}

}
