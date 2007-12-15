package org.impalaframework.plugin.builder;

import junit.framework.TestCase;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimpleBeansetPluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.springframework.core.io.ClassPathResource;

public class XmlPluginSpecBuilderTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	private static final String plugin4 = "impala-sample-dynamic-plugin4";

	private XmlPluginSpecBuilder builder;
	
	@Override
	protected void setUp() throws Exception {
		builder = new XmlPluginSpecBuilder();
	}
	
	public final void testGetParentOnlySpec() {
		builder.setResource(new ClassPathResource("xmlspec/parent-only-spec.xml"));
		ParentSpec actual = builder.getPluginSpec();
		assertEquals(0, actual.getPlugins().size());

		ParentSpec expected = new SimpleParentSpec(new String[] { "parentTestContext.xml", "extra-context.xml" });
		assertEquals(expected, actual);
	}
	
	public final void testGetParentSpec() {
		builder.setResource(new ClassPathResource("xmlspec/pluginspec.xml"));
		ParentSpec actual = builder.getPluginSpec();
		assertEquals(3, actual.getPlugins().size());

		ParentSpec expected = new SimpleParentSpec(new String[] { "parentTestContext.xml", "extra-context.xml" });
		assertEquals(expected, actual);
		
		PluginSpec spec1 = new SimplePluginSpec(expected, plugin1);
		PluginSpec spec2 = new SimplePluginSpec(expected, plugin2);
		PluginSpec spec3 = new SimplePluginSpec(spec2, plugin3);
		PluginSpec spec4 = new SimpleBeansetPluginSpec(expected, plugin4, "alternative: myImports");
		
		assertEquals(spec1, actual.findPlugin(plugin1, true));
		assertEquals(spec2, actual.findPlugin(plugin2, true));
		assertEquals(spec3, actual.findPlugin(plugin3, true));
		assertEquals(spec4, actual.findPlugin(plugin4, true));
	}
	
	public void testIsBeanSetSpec() throws Exception {
		assertFalse(builder.isBeanSetSpec(null, null));
		assertFalse(builder.isBeanSetSpec("other", null));
		assertTrue(builder.isBeanSetSpec(null, "overrides"));
		assertTrue(builder.isBeanSetSpec("APPLICATION_WITH_BEANSETS", null));
		assertTrue(builder.isBeanSetSpec("application_with_beansets", null));
	}

}
