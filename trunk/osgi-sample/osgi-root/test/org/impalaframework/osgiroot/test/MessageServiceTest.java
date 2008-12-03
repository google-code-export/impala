package org.impalaframework.osgiroot.test;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.osgi.test.OsgiIntegrationTest;
import org.impalaframework.osgiroot.MessageService;
import org.osgi.framework.ServiceReference;
import org.springframework.context.ApplicationContext;

public class MessageServiceTest extends OsgiIntegrationTest {
	
	public MessageServiceTest() {
		super();
		//FIXME figure out where to put these
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, "../,../osgi-repository/dist");
		System.setProperty("application.version", "1.0");
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
