package org.impalaframework.web.generic;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.springframework.util.Assert;

public class IntegrationServletConfig implements ServletConfig {
	
	private Map<String,String> initParameterMap;
	private ServletContext servletContext;
	private String servletName;
	
	public IntegrationServletConfig(Map<String, String> initParameterMap, ServletContext servletContext, String servletName) {
		super();
		Assert.notNull(initParameterMap);
		Assert.notNull(servletContext);
		Assert.notNull(servletName);
		this.initParameterMap = initParameterMap;
		this.servletContext = servletContext;
		this.servletName = servletName;
	}

	public String getInitParameter(String name) {
		return initParameterMap.get(name);
	}

	public Enumeration<?> getInitParameterNames() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>(this.initParameterMap);
		return hashtable.keys();
	}

	public ServletContext getServletContext() {
		return this.servletContext;
	}

	public String getServletName() {
		return this.servletName;
	}

}
