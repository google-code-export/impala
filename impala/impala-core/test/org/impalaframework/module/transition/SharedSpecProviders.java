package org.impalaframework.module.transition;

import org.impalaframework.module.builder.SimplePluginSpecBuilder;
import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpec;
import org.impalaframework.module.spec.PluginSpecProvider;
import org.impalaframework.module.spec.SimplePluginSpec;

public class SharedSpecProviders {

	static final String plugin1 = "impala-sample-dynamic-plugin1";

	static final String plugin2 = "impala-sample-dynamic-plugin2";

	static final String plugin3 = "impala-sample-dynamic-plugin3";

	static PluginSpecProvider newTest1() {
		return new Test1();
	}

	static PluginSpecProvider newTest1a() {
		return new Test1a();
	}

	static PluginSpecProvider newTest2() {
		return new Test2();
	}

	static class Test1 implements PluginSpecProvider {
		PluginSpecProvider spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test1() {
		}

		public ParentSpec getPluginSpec() {
			return spec.getPluginSpec();
		}
	}

	static class Test1a implements PluginSpecProvider {
		PluginSpecProvider spec = new SimplePluginSpecBuilder(new String[] { "parentTestContext.xml",
				"extra-context.xml" }, new String[] { plugin1, plugin2 });

		public Test1a() {
		}

		public ParentSpec getPluginSpec() {
			return spec.getPluginSpec();
		}
	}

	static class Test2 implements PluginSpecProvider {
		PluginSpecProvider spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test2() {

			PluginSpec p2 = spec.getPluginSpec().getPlugin(plugin2);
			new SimplePluginSpec(p2, plugin3);
		}

		public ParentSpec getPluginSpec() {
			return spec.getPluginSpec();
		}
	}

}
