package org.impalaframework.web.servlet.wrapper;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.classloader.ClassLoaderUtils;
import org.impalaframework.util.serialize.ClassLoaderAwareSerializationStreamFactory;
import org.impalaframework.util.serialize.SerializationHelper;
import org.springframework.util.Assert;

public class ModuleAwareWrapperHttpSession extends DelegatingWrapperHttpSession {
	
	private static final Log logger = LogFactory.getLog(ModuleAwareWrapperHttpSession.class);

	private final ClassLoader moduleClassLoader;

	public ModuleAwareWrapperHttpSession(HttpSession realSession,
			ClassLoader moduleClassLoader) {
		super(realSession);
		Assert.notNull(moduleClassLoader);
		this.moduleClassLoader = moduleClassLoader;
	}

	@Override
	public Object getAttribute(String name) {
		
		//FIXME test
		Object attribute = super.getAttribute(name);
		
		if (attribute == null) 
			return null;
		
		ClassLoader attributeClassLoader = attribute.getClass().getClassLoader();
		if (!ClassLoaderUtils.isVisibleFrom(attributeClassLoader, moduleClassLoader)) {
			
			if (!(attribute instanceof Serializable)) {
				logger.warn("Object in session under key [" + name + "] is not compatible with the current class loader, and cannot be recovered because it does not implement " + Serializable.class.getName() + ". Attribute will be removed from the session");
				this.removeAttribute(name);
				return null;
			}
			
			SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(moduleClassLoader));
			Object clonedAttribute = helper.clone((Serializable) attribute);
			
			//explicitly set as the session attribute object has changed
			this.setAttribute(name, clonedAttribute);
			return clonedAttribute;
		}
		
		return attribute;
	}

}
