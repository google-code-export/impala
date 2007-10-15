package net.java.impala.spring.plugin;

import java.io.File;

import net.java.impala.classloader.CustomClassLoader;
import net.java.impala.location.ClassLocationResolver;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ApplicationPluginLoader implements PluginLoader {

	private ClassLocationResolver classLocationResolver;

	public ApplicationPluginLoader(ClassLocationResolver classLocationResolver) {
		super();
		Assert.notNull("classLocationResolver cannot be null");
		this.classLocationResolver = classLocationResolver;
	}

	public GenericApplicationContext newApplicationContext(ApplicationContext parent, ClassLoader classLoader) {
		Assert.notNull(parent, "parent cannot be null");
		Assert.notNull(classLoader, "classloader cannot be null");
		
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		// create the application context, and set the class loader
		GenericApplicationContext context = new GenericApplicationContext(beanFactory, parent);
		context.setClassLoader(classLoader);
		return context;
	}
	
	public BeanDefinitionReader newBeanDefinitionReader(BeanDefinitionRegistry context) {
		return new XmlBeanDefinitionReader(context);
	}

	public ClassLoader newClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		ClassLoader parentClassLoader = PluginUtils.getParentClassLoader(contextSet, pluginSpec);
		File[] parentClassLocations = classLocationResolver.getApplicationPluginClassLocations(pluginSpec.getName());
		CustomClassLoader cl = new CustomClassLoader(parentClassLoader, parentClassLocations);
		return cl;
	}

	public Resource[] getClassLocations(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		// TODO Auto-generated method stub
		return null;
	}

	public Resource[] getSpringConfigResources(ApplicationContextSet contextSet, PluginSpec pluginSpec,
			ClassLoader classLoader) {
		File springLocation = this.classLocationResolver.getApplicationPluginSpringLocation(pluginSpec.getName());
		return new Resource[] { new FileSystemResource(springLocation) };
	}

}
