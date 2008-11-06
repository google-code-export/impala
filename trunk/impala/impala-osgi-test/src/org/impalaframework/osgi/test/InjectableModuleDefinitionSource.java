package org.impalaframework.osgi.test;

import java.io.Serializable;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.serialize.ClassLoaderAwareSerializationStreamFactory;
import org.impalaframework.util.serialize.SerializationHelper;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

public class InjectableModuleDefinitionSource implements ModuleDefinitionSource {

	private RootModuleDefinition source;

	private BundleContext bundleContext;

	public InjectableModuleDefinitionSource(BundleContext bundleContext) {
		super();
		this.bundleContext = bundleContext;
	}

	public void inject(Object o) {
		//FIXME check is serializable
		
		//FIXME add error handling
		
		final BundleDelegatingClassLoader classLoader = BundleDelegatingClassLoader.createBundleClassLoaderFor(bundleContext.getBundle());
		SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(classLoader));
		final Object clone = helper.clone((Serializable) o);
		
		source = ObjectUtils.cast(clone, RootModuleDefinition.class);
	}

	public RootModuleDefinition getModuleDefinition() {
		return source;
	}

}
