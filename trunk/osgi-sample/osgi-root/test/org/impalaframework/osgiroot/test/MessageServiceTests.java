package org.impalaframework.osgiroot.test;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.osgi.test.OsgiIntegrationTest;
import org.impalaframework.osgiroot.MessageService;
import org.impalaframework.util.ReflectionUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class MessageServiceTests extends OsgiIntegrationTest {
	
	protected void postProcessBundleContext(BundleContext context) throws Exception {
		Impala.init();
		RootModuleDefinition definition = getModuleDefinition();
		
		ServiceReference serviceReference = context.getServiceReference(ModuleDefinitionSource.class.getName());
		
		Object service = context.getService(serviceReference);
		System.out.println(service);
		
		Method method = ReflectionUtils.findMethod(service.getClass(), "inject", new Class[]{Object.class});
		ReflectionUtils.invokeMethod(method, service, new Object[]{definition});
		
		//now fire up extender bundle 
		
		File distDirectory = new File("../osgi-repository/dist");
		File[] distBundles = distDirectory.listFiles(new FileFilter(){

			public boolean accept(File pathname) {
				if (pathname.getName().contains("sources")) return false;
				return pathname.getName().contains("extender");
			} 
			
		});
		
		File mainDirectory = new File("../osgi-repository/main");
		File[] mainBundles = mainDirectory.listFiles(new FileFilter(){

			public boolean accept(File pathname) {
				if (pathname.getName().contains("sources")) return false;
				return pathname.getName().contains("extender");
			} 
			
		});
		
		List<Resource> arrayList = new ArrayList<Resource>();
		addResources(arrayList, distBundles);
		Resource[] addResources = addResources(arrayList, mainBundles);
		
		for (Resource resource : addResources) {
			Bundle bundle = installBundle(context, resource);
			startBundle(bundle);
		}
		
		super.postProcessBundleContext(context);
	}
	
	private Resource[] addResources(List<Resource> resources, File[] listFiles) {
		for (int i = 0; i < listFiles.length; i++) {
			resources.add(new FileSystemResource(listFiles[i]));
		}
		return resources.toArray(new Resource[0]);
	}
	
	public void testMessageService() throws Exception {
		
		ServiceReference[] serviceReference = bundleContext.getServiceReferences(ApplicationContext.class.getName(), "(org.springframework.context.service.name=org.impalaframework.osgiroot)");	
		Object service = bundleContext.getService(serviceReference[0]);
		ApplicationContext context = (ApplicationContext) service;
		MessageService messageService = (MessageService) context.getBean("messageService");
		System.out.println(messageService.getMessage());
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("osgi-root", "osgi-module1").getModuleDefinition();
	}
	
}
