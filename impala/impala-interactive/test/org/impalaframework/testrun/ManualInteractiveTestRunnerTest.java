package org.impalaframework.testrun;

import junit.framework.TestCase;

import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;

public class ManualInteractiveTestRunnerTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";
	
	public void setUp() {
		System.setProperty("impala.parent.project", "impala");
	}

	public final void testExecute() {
		new InteractiveTestRunner().start(Test1.class);
	}

	class Test1 implements ModuleDefinitionSource {
		ModuleDefinitionSource source = new SimpleModuleDefinitionSource("parentTestContext.xml",
				new String[] { plugin1 });

		public RootModuleDefinition getModuleDefinition() {
			return source.getModuleDefinition();
		}
	}
}
