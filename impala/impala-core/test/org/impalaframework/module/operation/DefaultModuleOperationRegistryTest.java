package org.impalaframework.module.operation;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.impalaframework.module.bootstrap.ModuleManagementFactory;

import junit.framework.TestCase;

public class DefaultModuleOperationRegistryTest extends TestCase {

	public final void testDefaultModuleOperationRegistry() {
		ModuleManagementFactory factory = EasyMock.createMock(ModuleManagementFactory.class);
		
		DefaultModuleOperationRegistry registry = new DefaultModuleOperationRegistry(factory);
		assertEquals(8, registry.getOperations().size());
		assertTrue(registry.getOperation(ModuleOperationConstants.AddModuleOperation) instanceof AddModuleOperation);
	
		Map<String, ModuleOperation> contributions = new HashMap<String, ModuleOperation>();
		ModuleOperation moduleOp2 = EasyMock.createMock(ModuleOperation.class);
		contributions.put(ModuleOperationConstants.AddModuleOperation, moduleOp2);
		
		registry.setContributedOperations(contributions);
		assertSame(moduleOp2, registry.getOperation(ModuleOperationConstants.AddModuleOperation));
	}

}
