package org.impalaframework.web.servlet;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.context.ServletContextAware;

public class SystemPropertyServletContextParamFactoryBean  implements FactoryBean, ServletContextAware {

	private ServletContext servletContext;

	public Object getObject() throws Exception {
		return null;
	}

	public Class<?> getObjectType() {
		return String.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
