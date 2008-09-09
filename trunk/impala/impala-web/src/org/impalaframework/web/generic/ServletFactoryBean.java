package org.impalaframework.web.generic;

import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.impalaframework.util.InstantiationUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

public class ServletFactoryBean implements FactoryBean, ServletContextAware, InitializingBean, DisposableBean {

	private ServletContext servletContext;
	private Map<String,String> initParameters;
	private HttpServlet servlet;
	private String servletName;
	private Class<?> servletClass;

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
		servlet = InstantiationUtils.instantiate(servletClass.getName());
		Map<String, String> emptyMap = Collections.emptyMap();
		Map<String,String> parameterMap = (initParameters != null ? initParameters : emptyMap);
		IntegrationServletConfig config = new IntegrationServletConfig(parameterMap, this.servletContext, this.servletName);
		servlet.init(config);
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

}
