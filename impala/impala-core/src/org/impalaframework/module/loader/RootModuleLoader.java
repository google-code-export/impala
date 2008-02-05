package org.impalaframework.module.loader;


import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class RootModuleLoader extends BaseModuleLoader {

	private ModuleLocationResolver moduleLocationResolver;

	public RootModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super();
		Assert.notNull("moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		Resource[] rootClassLoader = ModuleUtils.getRootClassLocations(moduleLocationResolver);
		return new ModuleClassLoader(ClassUtils.getDefaultClassLoader(), ResourceUtils.getFiles(rootClassLoader));
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		return ModuleUtils.getRootClassLocations(moduleLocationResolver);
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition definition) {
		// FIXME return tweaked version of BeanDefinitionReader which
		// will not add bean definitions if the applicationContext is not active
		return super.newBeanDefinitionReader(context, definition);
	}

}
