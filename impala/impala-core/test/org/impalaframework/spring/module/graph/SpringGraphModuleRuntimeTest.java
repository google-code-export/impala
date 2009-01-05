package org.impalaframework.spring.module.graph;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.facade.Impala;
import org.impalaframework.file.FileMonitor;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.module.source.InternalModuleDefinitionSource;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.spring.module.SpringModuleUtils;
import org.springframework.context.ApplicationContext;

public class SpringGraphModuleRuntimeTest extends TestCase implements ModuleDefinitionSource {

	public void setUp() {
		Impala.clear();
		System.setProperty("classloader.type", "graph");
	}

	public void tearDown() {
		try {
			Impala.clear();
		}
		catch (Exception e) {
		}
		System.clearProperty("classloader.type");
	}

	public void testGraphInheritanceStrategies() throws Exception {

		Impala.init();
		System.out.println(getModuleDefinition());
		
		Impala.init(this);
		ModuleStateHolder moduleStateHolder = Impala.getFacade().getModuleManagementFacade().getModuleStateHolder();
		
		BaseBeanGraphInheritanceStrategy strategy = new ParentFirstBeanGraphInheritanceStrategy();
		assertTrue(strategy.getDelegateGetBeanCallsToParent());

		checkExpected(moduleStateHolder, strategy, "sample-module4", 0);
		checkExpected(moduleStateHolder, strategy, "sample-module6", 3);
		checkExpected(moduleStateHolder, strategy, "sample-module5", 0);
		checkExpected(moduleStateHolder, strategy, "sample-module2", 0);
		checkExpected(moduleStateHolder, strategy, "impala-core", 0);
		
		strategy = new GraphOrderedBeanInheritanceStrategy();
		assertFalse(strategy.getDelegateGetBeanCallsToParent());

		checkExpected(moduleStateHolder, strategy, "sample-module4", 2);
		checkExpected(moduleStateHolder, strategy, "sample-module6", 4);
		checkExpected(moduleStateHolder, strategy, "sample-module5", 0);
		checkExpected(moduleStateHolder, strategy, "sample-module2", 1);
		checkExpected(moduleStateHolder, strategy, "impala-core", 0);
		
		RuntimeModule runtimeModule = Impala.getRuntimeModule("sample-module6");
		FileMonitor bean = (FileMonitor) runtimeModule.getBean("bean4");
		bean.lastModified(new File("./"));
	}

	private void checkExpected(ModuleStateHolder moduleStateHolder,
			BaseBeanGraphInheritanceStrategy strategy, String moduleName, int expected) {
		ModuleDefinition definition = moduleStateHolder.getModuleDefinition().findChildDefinition(moduleName, true);
		
		RuntimeModule parentModule = null;
		if (definition.getParentDefinition() != null) parentModule = moduleStateHolder.getModule(definition.getParentDefinition().getName());
		
		ApplicationContext parent = SpringModuleUtils.getModuleSpringContext(parentModule);
		
		List<ApplicationContext> contexts = strategy.getDependentApplicationContexts(definition, parent, (GraphModuleStateHolder)moduleStateHolder);
		System.out.println(contexts);
		assertEquals(expected, contexts.size());
		
		for (ApplicationContext applicationContext : contexts) {
			System.out.println(applicationContext.getDisplayName());
		}
	}

	public RootModuleDefinition getModuleDefinition() {
		return new InternalModuleDefinitionSource(TypeReaderRegistryFactory.getTypeReaderRegistry(), 
				Impala.getFacade().getModuleManagementFacade().getModuleLocationResolver(), 
				new String[] { "impala-core", "sample-module4",	"sample-module5", "sample-module6" }).getModuleDefinition();
	}
}
