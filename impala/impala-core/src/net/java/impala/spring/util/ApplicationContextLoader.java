package net.java.impala.spring.util;

import java.util.Arrays;

import net.java.impala.classloader.ContextResourceHelper;
import net.java.impala.spring.plugin.ApplicationContextSet;
import net.java.impala.spring.plugin.Plugin;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.util.PathUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class ApplicationContextLoader {

	private static final Log log = LogFactory.getLog(ApplicationContextLoader.class);
	
	private ContextResourceHelper contextResourceHelper;
	
	public ApplicationContextLoader(ContextResourceHelper resourceHelper) {
		Assert.notNull(resourceHelper, ContextResourceHelper.class.getName() + " cannot be null");
		this.contextResourceHelper = resourceHelper;
	}

	public ClassLoader newParentClassLoader() {
		ClassLoader contextClassLoader = ClassUtils.getDefaultClassLoader();
		return contextResourceHelper.getParentClassLoader(contextClassLoader, PathUtils.getCurrentDirectoryName());
	}

	public ApplicationContextSet loadParentContext(PluginSpec pluginSpec, ClassLoader classLoader) {

		ApplicationContextSet set = null;
		ConfigurableApplicationContext context = null;

		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();

		try {

			Thread.currentThread().setContextClassLoader(classLoader);
			String[] locations = pluginSpec.getParentContextLocations();
			context = this.loadContextFromClasspath(locations, classLoader);

			set = new ApplicationContextSet(context);

			if (pluginSpec != null) {
				Plugin[] plugins = pluginSpec.getPlugins();
				for (Plugin plugin : plugins) {
					ConfigurableApplicationContext pluginContext = addApplicationPlugin(context, plugin);
					set.getPluginContext().put(plugin.getName(), pluginContext);
				}
			}

		}
		finally {
			Thread.currentThread().setContextClassLoader(existingClassLoader);
		}
		return set;
	}

	public ConfigurableApplicationContext addApplicationPlugin(ApplicationContext parent, Plugin plugin) {

		if (this.contextResourceHelper == null) {
			throw new IllegalStateException(ContextResourceHelper.class.getName() + " not set");
		}

		Resource springLocation = this.contextResourceHelper.getApplicationPluginSpringLocation(plugin.getName());

		// create the class loader
		ClassLoader classLoader = this.contextResourceHelper.getApplicationPluginClassLoader(parent.getClassLoader(), plugin.getName());

		ClassLoader existing = ClassUtils.getDefaultClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(classLoader);

			ConfigurableApplicationContext context = this.loadContextFromResource(parent, springLocation,
					classLoader);
			return context;
		}
		finally {
			Thread.currentThread().setContextClassLoader(existing);
		}

	}

	public ConfigurableApplicationContext loadContextFromClasspath(String[] locations, ClassLoader parent) {

		log.info("Reloading application context from locations " + Arrays.toString(locations));

		DefaultListableBeanFactory beanFactory = newBeanFactory();
		if (parent != null)
			beanFactory.setBeanClassLoader(parent);

		// create the application context, and set the class loader
		GenericApplicationContext context = newApplicationContext(beanFactory);
		if (parent != null)
			context.setClassLoader(parent);

		// create the bean definition reader
		XmlBeanDefinitionReader xmlReader = newBeanDefinitionReader(context);
		xmlReader.setBeanClassLoader(parent);

		for (String location : locations) {
			xmlReader.loadBeanDefinitions(new ClassPathResource(location, parent));
		}

		context.refresh();
		return context;
	}

	public ConfigurableApplicationContext loadContextFromResource(ApplicationContext parent, Resource resource,
			ClassLoader classLoader) {

		log.info("Reloading application context from resource " + resource.getDescription());

		DefaultListableBeanFactory beanFactory = newBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		// create the application context, and set the class loader
		GenericApplicationContext context = new GenericApplicationContext(beanFactory, parent);
		context.setClassLoader(classLoader);

		XmlBeanDefinitionReader xmlReader = newBeanDefinitionReader(context);
		xmlReader.loadBeanDefinitions(resource);

		// refresh the application context - now we're ready to go
		context.refresh();
		return context;
	}

	protected XmlBeanDefinitionReader newBeanDefinitionReader(GenericApplicationContext context) {
		return new XmlBeanDefinitionReader(context);
	}

	protected GenericApplicationContext newApplicationContext(DefaultListableBeanFactory beanFactory) {
		return new GenericApplicationContext(beanFactory);
	}

	protected DefaultListableBeanFactory newBeanFactory() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		return beanFactory;
	}

	public ContextResourceHelper getContextResourceHelper() {
		return contextResourceHelper;
	}
}
