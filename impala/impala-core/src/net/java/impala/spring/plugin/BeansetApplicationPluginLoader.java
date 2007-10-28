package net.java.impala.spring.plugin;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.java.impala.classloader.FileSystemClassLoader;
import net.java.impala.classloader.NonDelegatingResourceClassLoader;
import net.java.impala.location.ClassLocationResolver;
import net.java.impala.spring.beanset.BeanSetPropertiesReader;
import net.java.impala.spring.beanset.DebuggingImportingBeanDefinitionDocumentReader;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

public class BeansetApplicationPluginLoader extends ApplicationPluginLoader {

	public BeansetApplicationPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	//FIXME add test
	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, PluginSpec pluginSpec) {
		if (pluginSpec instanceof BeansetPluginSpec) {
			BeansetPluginSpec beanSetSpec = (BeansetPluginSpec) pluginSpec;
			Map<String, Set<String>> overrides = beanSetSpec.getOverrides();

			ClassLoader classLoader = context.getClassLoader();
			if (classLoader instanceof FileSystemClassLoader) {
				classLoader = new NonDelegatingResourceClassLoader((FileSystemClassLoader) classLoader);
			}

			Properties properties = new BeanSetPropertiesReader().readBeanSetSpec(classLoader, overrides);			
			
			final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
			final DebuggingImportingBeanDefinitionDocumentReader documentReader = new DebuggingImportingBeanDefinitionDocumentReader(properties);

			XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(PluginUtils.castToBeanDefinitionRegistry(beanFactory)){
				protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
					return documentReader;
				}
			};
			return xmlReader;
		}
		else {
			return super.newBeanDefinitionReader(context, pluginSpec);
		}
	}
	
}
