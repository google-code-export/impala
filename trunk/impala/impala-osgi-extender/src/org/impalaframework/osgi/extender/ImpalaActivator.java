package org.impalaframework.osgi.extender;

import java.net.URL;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.builder.InternalModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContext;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.util.ObjectUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

//FIXME test
public class ImpalaActivator implements BundleActivator {

	private ModuleManagementFacade facade;

	public void start(BundleContext bundleContext) throws Exception {

		//FIXME use fragment to find bootstrap locations
		
		//TODO see this as 
		URL[] resources = OsgiUtils.findResources(bundleContext, new String[] {
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-osgi-bootstrap.xml"
				});
		
		Resource[] configResources = new Resource[resources.length];
		for (int i = 0; i < configResources.length; i++) {
			configResources[i] = new UrlResource(resources[i]);
		}
		
		ImpalaOsgiApplicationContext applicationContext = null;
		
		if (resources != null) {
			Thread currentThread = Thread.currentThread();
			ClassLoader oldTCCL = currentThread.getContextClassLoader();
			
			try
			{
				ClassLoader classLoader = BundleDelegatingClassLoader.createBundleClassLoaderFor(bundleContext.getBundle());
				currentThread.setContextClassLoader(classLoader);
				
				applicationContext = new ImpalaOsgiApplicationContext();
				
				applicationContext.setConfigResources(configResources);
				applicationContext.setBundleContext(bundleContext);
				applicationContext.refresh();
			}
			finally {
				currentThread.setContextClassLoader(oldTCCL);
			}
		} else {
			System.out.println("Could not find impala-bootstrap.xml resource");
		}
		
		if (applicationContext != null) {
			
			facade = ObjectUtils.cast(applicationContext.getBean("moduleManagementFacade"),
					ModuleManagementFacade.class);
			
			//USE test itself to instantiate bundles
			
			//TODO this needs to be picked up by a fragment
			ModuleDefinitionSource moduleDefinitionSource = new InternalModuleDefinitionSource(
					facade.getTypeReaderRegistry(), 
					facade.getModuleLocationResolver(), 
					new String[]{"osgi-root", "osgi-module1"});
			
			ModuleOperation operation = facade.getModuleOperationRegistry().getOperation(
					ModuleOperationConstants.IncrementalUpdateRootModuleOperation);
			operation.execute(new ModuleOperationInput(moduleDefinitionSource, null, null));
		
		}
			
	}

	public void stop(BundleContext bundleContext) throws Exception {
		
		//FIXME unload all Impala modules and shut down
	}
}

