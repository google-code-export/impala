package org.impalaframework.osgiroot.test;

import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.osgi.test.OsgiIntegrationTest;
import org.impalaframework.osgiroot.MessageService;

public class MessageServiceTest extends OsgiIntegrationTest {
	
	public MessageServiceTest() {
		super();
		System.setProperty("impala.module.class.dir", "target/classes");
	}

	public void testMessageService() throws Exception {
		MessageService messageService = Impala.getBean("messageService", MessageService.class);
		System.out.println(messageService.getMessage());
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("osgi-root", "osgi-module1").getModuleDefinition();
	}
	
}
