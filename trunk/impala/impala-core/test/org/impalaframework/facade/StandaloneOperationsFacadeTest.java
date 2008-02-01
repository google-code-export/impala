package org.impalaframework.facade;

import junit.framework.TestCase;

public class StandaloneOperationsFacadeTest extends TestCase {

	public final void testStandaloneOperationsFacade() {
		StandaloneOperationsFacade facade = new StandaloneOperationsFacade();
		assertNotNull(facade.getModuleStateHolder());
	}

}
