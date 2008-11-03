package org.impalaframework.osgi.module.transition;

import java.io.IOException;

import org.impalaframework.module.ApplicationContextLoader;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.osgi.util.OsgiUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.springframework.core.io.Resource;
import org.springframework.osgi.context.BundleContextAware;

public class OsgiLoadTransitionProcessor extends LoadTransitionProcessor implements BundleContextAware {

	//private ManagedBundlesRegistry managedBundles;
	
	private BundleContext bundleContext;
	
	private ModuleLoaderRegistry moduleLoaderRegistry;

	public OsgiLoadTransitionProcessor(ApplicationContextLoader contextLoader) {
		super(contextLoader);
	}

	@Override
	public boolean process(ModuleStateHolder moduleStateHolder,
			RootModuleDefinition newRootDefinition,
			ModuleDefinition currentDefinition) {
		
		//FIXME test and robustify!
		
		//find bundle with name
		Bundle bundle = OsgiUtils.findBundle(bundleContext, currentDefinition.getName());
		if (bundle == null) {
			//install if not present
			
			final ModuleLoader moduleLoader = moduleLoaderRegistry.getModuleLoader(currentDefinition.getType());
			final Resource[] bundleLocations = moduleLoader.getClassLocations(currentDefinition);
			
			//
			try {
				bundle = bundleContext.installBundle(currentDefinition.getName(), bundleLocations[0].getInputStream());
			} catch (BundleException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		
		//if bundle is not present
		final int bundleState = bundle.getState();
		if (bundleState != Bundle.ACTIVE) {
			try {
				bundle.start();
			} catch (BundleException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		//wait until has definitely started?
		
		return super.process(moduleStateHolder, newRootDefinition, currentDefinition);
	}

	public void setModuleLoaderRegistry(ModuleLoaderRegistry moduleLoaderRegistry) {
		this.moduleLoaderRegistry = moduleLoaderRegistry;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

}
