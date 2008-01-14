package org.impalaframework.command.interactive;

import org.impalaframework.command.GlobalCommandState;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.DynamicContextHolder;

import junit.framework.TestCase;

public class InitContextCommandTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	public void setUp() {
		System.setProperty("impala.parent.project", "impala");
	}

	public final void testExecute() {
		InitContextCommand command = new InitContextCommand();
		Test1 t = new Test1();

		GlobalCommandState.getInstance().addValue("moduleDefinition", t.getModuleDefinition());
		command.execute(null);
		try {
			DynamicContextHolder.get();
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No root application has been loaded", e.getMessage());
		}
	}

	class Test1 implements ModuleDefinitionSource {
		ModuleDefinitionSource source = new SimpleModuleDefinitionSource("parentTestContext.xml",
				new String[] { plugin1 });

		public RootModuleDefinition getModuleDefinition() {
			return source.getModuleDefinition();
		}
	}

}
