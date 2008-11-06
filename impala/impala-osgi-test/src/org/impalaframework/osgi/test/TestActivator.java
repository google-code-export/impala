package org.impalaframework.osgi.test;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class TestActivator implements BundleActivator {

	private ServiceRegistration registerService;

	public void start(BundleContext bundleContext) throws Exception {
		registerService = bundleContext.registerService(ModuleDefinitionSource.class.getName(), new InjectableModuleDefinitionSource(bundleContext), null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		registerService.unregister();
	}

}
