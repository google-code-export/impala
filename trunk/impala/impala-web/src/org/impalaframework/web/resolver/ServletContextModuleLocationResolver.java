package org.impalaframework.web.resolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;

public class ServletContextModuleLocationResolver implements ModuleLocationResolver, ServletContextAware, InitializingBean {

	private String[] rootProjectsArray;

	private String applicationVersion;

	private String relativeModuleRootLocation = "/WEB-INF/modules";

	private ServletContext servletContext;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(rootProjectsArray, "rootProjects cannot be null");
		Assert.notNull(relativeModuleRootLocation);
	}

	public Resource getRootDirectory() {
		return new ServletContextResource(servletContext, relativeModuleRootLocation);
	}
	
	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		String applicationVersionString = StringUtils.hasText(applicationVersion) ? "-" + applicationVersion : "";
		String fullResourceName = relativeModuleRootLocation + "/" + moduleName + applicationVersionString + ".jar";
		Resource servletContextResource = new ServletContextResource(servletContext, fullResourceName);
		return Collections.singletonList(servletContextResource);
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		throw new UnsupportedOperationException();
	}

	public List<String> getRootProjects() {
		Assert.notNull(rootProjectsArray);
		return Arrays.asList(rootProjectsArray);
	}

	public void setRootProjectsArray(String[] rootProjectsArray) {
		this.rootProjectsArray = rootProjectsArray;
	}

	public void setRootProjectsString(String rootProjects) {
		//FIXME tests
		this.rootProjectsArray = StringUtils.tokenizeToStringArray(rootProjects, " ,");
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public void setRelativeModuleRootLocation(String relativeModuleRootLocation) {
		this.relativeModuleRootLocation = relativeModuleRootLocation;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
