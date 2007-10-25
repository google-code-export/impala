package net.java.impala.spring.plugin;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.java.impala.classloader.FileSystemClassLoader;
import net.java.impala.classloader.NonDelegatingResourceClassLoader;
import net.java.impala.location.ClassLocationResolver;
import net.java.impala.spring.beanset.BeanSetPropertiesReader;
import net.java.impala.spring.beanset.DebuggingBeanSetDefinitionDocumentReader;
import net.java.impala.spring.beanset.DebuggingBeanSetImportDelegate;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;

public class BeansetApplicationPluginLoader extends ApplicationPluginLoader {

	public BeansetApplicationPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, PluginSpec pluginSpec) {
		
		XmlBeanDefinitionReader xmlReader = super.newBeanDefinitionReader(context, pluginSpec);
		
		if (pluginSpec instanceof BeansetPluginSpec) {
			BeansetPluginSpec beanSetSpec = (BeansetPluginSpec) pluginSpec;
			Map<String, Set<String>> overrides = beanSetSpec.getOverrides();
			
			ClassLoader classLoader = context.getClassLoader();
			if (classLoader instanceof FileSystemClassLoader) {
				classLoader = new NonDelegatingResourceClassLoader((FileSystemClassLoader) classLoader);
			}
			
			Properties properties = new BeanSetPropertiesReader().readBeanSetSpec(classLoader, overrides);
			
			DebuggingBeanSetImportDelegate delegate = new DebuggingBeanSetImportDelegate(properties);
			delegate.initBeanDefinitionReader(xmlReader);
			delegate.beforeRefresh(context);
			xmlReader.setDocumentReaderClass(DebuggingBeanSetDefinitionDocumentReader.class);
			
			//FIXME call afterRefresh
		}
		
		return xmlReader;
	}

	

}
