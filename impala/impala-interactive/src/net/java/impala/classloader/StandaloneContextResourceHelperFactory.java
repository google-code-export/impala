package net.java.impala.classloader;

import net.java.impala.classloader.ContextResourceHelper;
import net.java.impala.location.ContextResourceHelperFactory;
import net.java.impala.location.ClassLocationResolver;
import net.java.impala.location.StandaloneClassLocationResolverFactory;

@Deprecated
public class StandaloneContextResourceHelperFactory implements ContextResourceHelperFactory {

	public ContextResourceHelper getContextResourceHelper() {
		ClassLocationResolver classLocationResolver = new StandaloneClassLocationResolverFactory()
		.getClassLocationResolver();
		return new DefaultContextResourceHelper(classLocationResolver);
	}

}
