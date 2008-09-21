package org.impalaframework.web.servlet.wrapper;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.ServletContext;

public class ModuleAwareWrapperServletContext extends
		DelegatingWrapperServletContext {
	
	//MIXME test and flesh out methods to be implemented
	
	private final ClassLoader moduleClassLoader;

	public ModuleAwareWrapperServletContext(ServletContext realContext, String moduleName, ClassLoader moduleClassLoader) {
		super(realContext);
		this.moduleClassLoader = moduleClassLoader;
	}

	@Override
	public Object getAttribute(String name) {
		return super.getAttribute(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration getAttributeNames() {
		return super.getAttributeNames();
	}

	@Override
	public String getRealPath(String path) {
		return super.getRealPath(path);
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		String tempPath = path;
		
		//remove the leading slash
		if (tempPath.startsWith("/")) {
			tempPath = tempPath.substring(1);
		}
		
		URL resource = moduleClassLoader.getResource(tempPath);
		if (resource != null) return resource;
		
		return super.getResource(path);
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return super.getResourceAsStream(path);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set getResourcePaths(String path) {
		return super.getResourcePaths(path);
	}

	@Override
	public void removeAttribute(String name) {
		super.removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		super.setAttribute(name, value);
	}

}
