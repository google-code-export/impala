package org.impalaframework.spring.resource;

import org.springframework.core.io.Resource;

/**
 * Defines mechanism for accessing a resource from a given location. Similar to
 * the Spring <code>ResourceLoader</code> interface. The main difference is
 * that the <code>ClassLoader</code> is supplied in the
 * <code>getResource()</code> call. This has the advantage of allowing a
 * single resource loader to be used with different class loaders. It also
 * simplifies the implementation.F
 * @author Phil Zoio
 */
public interface ResourceLoader {
	public Resource getResource(String location, ClassLoader classLoader);
}
