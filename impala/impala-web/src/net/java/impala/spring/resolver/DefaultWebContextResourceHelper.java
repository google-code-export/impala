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

	public ClassLoader getWebClassLoader(String parentName, String servletName) {
		ClassLocationResolver classLocationResolver = getClassLocationResolver();

		File[] dir = classLocationResolver.getApplicationPluginClassLocations(parentName + "-" + servletName);

		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();
		return new ParentClassLoader(existingClassLoader, dir);
	}

}
