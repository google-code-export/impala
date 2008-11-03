package org.impalaframework.osgi.module.transition;

import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.impalaframework.osgi.util.OSGIUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.springframework.osgi.context.BundleContextAware;

public class OsgiUnloadTransitionProcessor extends UnloadTransitionProcessor implements BundleContextAware {

	//private ManagedBundlesRegistry managedBundles;
	
	private BundleContext bundleContext;

	public OsgiUnloadTransitionProcessor() {
		super();
	}

	@Override
	public boolean process(ModuleStateHolder moduleStateHolder,
			RootModuleDefinition newRootDefinition,
			ModuleDefinition currentDefinition) {
		
		boolean process = super.process(moduleStateHolder, newRootDefinition, currentDefinition);
		
		//FIXME test and robustify!
		
		//find bundle with name
		Bundle bundle = OSGIUtils.findBundle(bundleContext, currentDefinition.getName());
		if (bundle != null) {
			try {
				bundle.uninstall();
			} catch (BundleException e) {
				if (process) process = false; 
				e.printStackTrace();
				//FIXME what to do here
			}
		}
		
		return process;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

}
