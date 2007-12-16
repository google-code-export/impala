package org.impalaframework.plugin.operation;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class AddPluginOperation implements PluginOperation {

	final Logger logger = LoggerFactory.getLogger(AddPluginOperation.class);

	private final ImpalaBootstrapFactory factory;

	private final PluginSpec pluginToAdd;

	public AddPluginOperation(final ImpalaBootstrapFactory factory, final PluginSpec pluginToAdd) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginToAdd);
		this.factory = factory;
		this.pluginToAdd = pluginToAdd;
	}

	public boolean execute() {
		
		//FIXME comment and test
		
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		PluginModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationCalculationType.STICKY);
		addPlugin(pluginStateManager, calculator, pluginToAdd);
		return true;
	}
	
	public static void addPlugin(PluginStateManager pluginStateManager, PluginModificationCalculator calculator,
			PluginSpec pluginSpec) {
		
		//FIXME use operations

		ParentSpec oldSpec = pluginStateManager.getParentSpec();
		ParentSpec newSpec = pluginStateManager.cloneParentSpec();

		PluginSpec parent = pluginSpec.getParent();
		
		if (pluginSpec instanceof ParentSpec) {
			newSpec = (ParentSpec) pluginSpec;
		}
		else {

			PluginSpec newParent = null;

			if (parent == null) {
				newParent = newSpec;
			}
			else {
				String parentName = parent.getName();
				newParent = newSpec.findPlugin(parentName, true);

				if (newParent == null) {
					throw new IllegalStateException("Unable to find parent plugin " + parentName + " in " + newSpec);
				}
			}

			newParent.add(pluginSpec);
			pluginSpec.setParent(newParent);
		}

		PluginTransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
		pluginStateManager.processTransitions(transitions);
	}	
	
}
