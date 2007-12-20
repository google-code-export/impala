package org.impalaframework.module.builder;

import junit.framework.TestCase;

import org.impalaframework.module.builder.XmlModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.core.io.ClassPathResource;

public class XmlModuleDefinitionSourceTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	private static final String plugin4 = "impala-sample-dynamic-plugin4";

	private XmlModuleDefinitionSource builder;
	
	@Override
	protected void setUp() throws Exception {
		builder = new XmlModuleDefinitionSource();
	}
	
	public final void testGetParentOnlySpec() {
		builder.setResource(new ClassPathResource("xmlspec/parent-only-spec.xml"));
		RootModuleDefinition actual = builder.getModuleDefinition();
		assertEquals(0, actual.getModules().size());

		RootModuleDefinition expected = new SimpleRootModuleDefinition(new String[] { "parentTestContext.xml", "extra-context.xml" });
		assertEquals(expected, actual);
	}
	
	public final void testGetParentSpec() {
		builder.setResource(new ClassPathResource("xmlspec/pluginspec.xml"));
		RootModuleDefinition actual = builder.getModuleDefinition();
		assertEquals(3, actual.getModules().size());

		RootModuleDefinition expected = new SimpleRootModuleDefinition(new String[] { "parentTestContext.xml", "extra-context.xml" });
		assertEquals(expected, actual);
		
		ModuleDefinition spec1 = new SimpleModuleDefinition(expected, plugin1);
		ModuleDefinition spec2 = new SimpleModuleDefinition(expected, plugin2);
		ModuleDefinition spec3 = new SimpleModuleDefinition(spec2, plugin3);
		ModuleDefinition spec4 = new SimpleBeansetModuleDefinition(expected, plugin4, "alternative: myImports");
		
		assertEquals(spec1, actual.findChildDefinition(plugin1, true));
		assertEquals(spec2, actual.findChildDefinition(plugin2, true));
		assertEquals(spec3, actual.findChildDefinition(plugin3, true));
		assertEquals(spec4, actual.findChildDefinition(plugin4, true));
	}
	
	public void testIsBeanSetSpec() throws Exception {
		assertFalse(builder.isBeanSetSpec(null, null));
		assertFalse(builder.isBeanSetSpec("other", null));
		assertTrue(builder.isBeanSetSpec(null, "overrides"));
		assertTrue(builder.isBeanSetSpec("APPLICATION_WITH_BEANSETS", null));
		assertTrue(builder.isBeanSetSpec("application_with_beansets", null));
	}

}
