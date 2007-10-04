package net.java.impala.classloader;

import org.springframework.util.ClassUtils;

import junit.framework.TestCase;
import net.java.impala.location.PropertyClassLocationResolver;

public class DefaultContextResourceHelperTest extends TestCase {

	public void testDefaultContextResourceHelper() {
		PropertyClassLocationResolver propertyClassLocationResolver = new PropertyClassLocationResolver();
		DefaultContextResourceHelper resourceHelper = new DefaultContextResourceHelper(propertyClassLocationResolver);
		
		ClassLoader parentClassLoader = resourceHelper.getParentClassLoader(ClassUtils.getDefaultClassLoader(), "impala-core");
		assertTrue(parentClassLoader instanceof ParentClassLoader);
		
		CustomClassLoader pluginClassLoader = resourceHelper.getApplicationPluginClassLoader(parentClassLoader, "plugin1");
		assertTrue(pluginClassLoader instanceof CustomClassLoader);
	
	}

}
