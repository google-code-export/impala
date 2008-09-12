package org.impalaframework.web.integration;

import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.spring.module.ModuleDefinitionAware;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

public class ServletFactoryBean implements FactoryBean, ServletContextAware, InitializingBean, DisposableBean, ModuleDefinitionAware, ApplicationContextAware {

	private ServletContext servletContext;
	private Map<String,String> initParameters;
	private HttpServlet servlet;
	private String servletName;
	private Class<?> servletClass;
	private ApplicationContext applicationContext;
	private ModuleDefinition moduleDefintion;

	public Object getObject() throws Exception {
		return servlet;
	}

	public Class<?> getObjectType() {
		return HttpServlet.class;
	}

	public boolean isSingleton() {
		return true;
	}

	/* ***************** InitializingBean implementation **************** */
	
	public void afterPropertiesSet() throws Exception {
		if (servletName == null) {
			servletName = moduleDefintion.getName();
		}
		
		servlet = (HttpServlet) BeanUtils.instantiateClass(servletClass);
		Map<String, String> emptyMap = Collections.emptyMap();
		Map<String,String> parameterMap = (initParameters != null ? initParameters : emptyMap);
		IntegrationServletConfig config = new IntegrationServletConfig(parameterMap, this.servletContext, this.servletName);
		
		if (servlet instanceof ApplicationContextAware) {
			ApplicationContextAware awa = (ApplicationContextAware) servlet;
			awa.setApplicationContext(applicationContext);
		}
		
		initServletProperties(servlet);		
		servlet.init(config);
	}

	/**
	 * Hook for subclasses to customise servlet properties
	 */
	protected void initServletProperties(HttpServlet servlet) {
	}

	/* ***************** DisposableBean implementation **************** */
	
	public void destroy() throws Exception {
		servlet.destroy();
	}
	
	/* ***************** injection setters **************** */

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}	

	public void setInitParameters(Map<String, String> initParameters) {
		this.initParameters = initParameters;
	}

	public void setServlet(HttpServlet servlet) {
		this.servlet = servlet;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public void setServletClass(Class<?> servletClass) {
		this.servletClass = servletClass;
	}

	public void setModuleDefinition(ModuleDefinition moduleDefinition) {
		this.moduleDefintion = moduleDefinition;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
