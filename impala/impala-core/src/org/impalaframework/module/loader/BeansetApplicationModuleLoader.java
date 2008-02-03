package org.impalaframework.module.loader;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.impalaframework.module.beanset.BeanSetPropertiesReader;
import org.impalaframework.module.beanset.RecordingImportingBeanDefinitionDocumentReader;
import org.impalaframework.module.definition.BeansetModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

public class BeansetApplicationModuleLoader extends ApplicationModuleLoader {

	public BeansetApplicationModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition definition) {
		if (definition instanceof BeansetModuleDefinition) {
			BeansetModuleDefinition beanSetDefinition = (BeansetModuleDefinition) definition;
			Map<String, Set<String>> overrides = beanSetDefinition.getOverrides();

			ClassLoader classLoader = context.getClassLoader();

			Properties properties = new BeanSetPropertiesReader().readBeanSetDefinition(classLoader, overrides);			
			
			final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
			final RecordingImportingBeanDefinitionDocumentReader documentReader = new RecordingImportingBeanDefinitionDocumentReader(properties);

			XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ModuleUtils.castToBeanDefinitionRegistry(beanFactory)){
				protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
					return documentReader;
				}
			};
			return xmlReader;
		}
		else {
			return super.newBeanDefinitionReader(context, definition);
		}
	}
	
}
