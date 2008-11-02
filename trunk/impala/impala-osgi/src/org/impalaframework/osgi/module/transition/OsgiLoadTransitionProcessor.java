package org.impalaframework.osgi.module.transition;

import java.io.IOException;

import org.impalaframework.module.ApplicationContextLoader;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.springframework.core.io.Resource;

public class OsgiLoadTransitionProcessor extends LoadTransitionProcessor {

	//private ManagedBundlesRegistry managedBundles;
	
	public OsgiLoadTransitionProcessor(ApplicationContextLoader contextLoader) {
		super(contextLoader);
	}

	@Override
	public boolean process(ModuleStateHolder moduleStateHolder,
			RootModuleDefinition newRootDefinition,
			ModuleDefinition currentDefinition) {
		
		ModuleLoader loader = null;
		final Resource[] classLocations = loader.getClassLocations(currentDefinition);
		
		//find bundle with name
		
		//if bundle is not present
		
		//
		BundleContext bundleContext = null;
		try {
			bundleContext.installBundle("", classLocations[0].getInputStream());
		} catch (BundleException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//wait until has definitely started
		
		//
		
		return super.process(moduleStateHolder, newRootDefinition, currentDefinition);
	}

}
