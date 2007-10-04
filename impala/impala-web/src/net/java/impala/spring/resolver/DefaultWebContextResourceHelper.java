package net.java.impala.spring.resolver;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import net.java.impala.classloader.DefaultContextResourceHelper;
import net.java.impala.classloader.ParentClassLoader;
import net.java.impala.location.ClassLocationResolver;

public class DefaultWebContextResourceHelper extends DefaultContextResourceHelper implements WebContextResourceHelper {

	public DefaultWebContextResourceHelper(WebClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	public ClassLoader getWebClassLoader(String parentName, String servletName) {
		WebClassLocationResolver webClassLocationResolver = getWebClassLocationResolver();

		File dir = webClassLocationResolver.getParentWebClassLocation(parentName, servletName);

		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();
		ParentClassLoader cl = new ParentClassLoader(existingClassLoader, new File[] { dir });
		return cl;
	}

	protected WebClassLocationResolver getWebClassLocationResolver() {
		ClassLocationResolver classLocationResolver = this.getClassLocationResolver();

		if (!(classLocationResolver instanceof WebClassLocationResolver)) {
			throw new IllegalStateException(ClassLocationResolver.class.getSimpleName() + " "
					+ classLocationResolver.getClass().getName() + " not instance of "
					+ WebClassLocationResolver.class.getSimpleName());
		}

		WebClassLocationResolver webClassLocationResolver = (WebClassLocationResolver) classLocationResolver;
		return webClassLocationResolver;
	}

	public Resource getParentWebClassLocation(String parentName, String servletName) {
		WebClassLocationResolver webClassLocationResolver = getWebClassLocationResolver();

		// FIXME convert to Resource
		File dir = webClassLocationResolver.getParentWebClassLocation(parentName, servletName);
		return new FileSystemResource(dir);
	}

}
