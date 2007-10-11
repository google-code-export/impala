package net.java.impala.spring.resolver;

import java.io.File;

import net.java.impala.classloader.DefaultContextResourceHelper;
import net.java.impala.classloader.ParentClassLoader;
import net.java.impala.location.ClassLocationResolver;

import org.springframework.util.ClassUtils;

public class DefaultWebContextResourceHelper extends DefaultContextResourceHelper implements WebContextResourceHelper {

	public DefaultWebContextResourceHelper(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	public ClassLoader getWebClassLoader(String pluginName) {
		ClassLocationResolver classLocationResolver = getClassLocationResolver();

		File[] dir = classLocationResolver.getApplicationPluginClassLocations(pluginName);

		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();
		
		//note the use of ParentClassLoader.
		//FIXME can you explain why this is used instead of CustomClassLoader
		return new ParentClassLoader(existingClassLoader, dir);
	}

}
