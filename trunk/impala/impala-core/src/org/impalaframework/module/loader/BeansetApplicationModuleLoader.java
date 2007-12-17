package org.impalaframework.module.loader;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.impalaframework.classloader.FileSystemClassLoader;
import org.impalaframework.classloader.NonDelegatingResourceClassLoader;
import org.impalaframework.module.beanset.BeanSetPropertiesReader;
import org.impalaframework.module.beanset.DebuggingImportingBeanDefinitionDocumentReader;
import org.impalaframework.module.spec.BeansetModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

public class BeansetApplicationModuleLoader extends ApplicationModuleLoader {

	public BeansetApplicationModuleLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition moduleDefinition) {
		if (moduleDefinition instanceof BeansetModuleDefinition) {
			BeansetModuleDefinition beanSetSpec = (BeansetModuleDefinition) moduleDefinition;
			Map<String, Set<String>> overrides = beanSetSpec.getOverrides();

			ClassLoader classLoader = context.getClassLoader();
			if (classLoader instanceof FileSystemClassLoader) {
				classLoader = new NonDelegatingResourceClassLoader((FileSystemClassLoader) classLoader);
			}

			Properties properties = new BeanSetPropertiesReader().readBeanSetSpec(classLoader, overrides);			
			
			final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
			final DebuggingImportingBeanDefinitionDocumentReader documentReader = new DebuggingImportingBeanDefinitionDocumentReader(properties);

			XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ModuleUtils.castToBeanDefinitionRegistry(beanFactory)){
				protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
					return documentReader;
				}
			};
			return xmlReader;
		}
		else {
			return super.newBeanDefinitionReader(context, moduleDefinition);
		}
	}
	
}
