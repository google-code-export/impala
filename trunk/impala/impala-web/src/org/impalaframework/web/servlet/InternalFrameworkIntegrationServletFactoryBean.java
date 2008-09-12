package org.impalaframework.web.servlet;

import javax.servlet.http.HttpServlet;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.web.generic.ServletFactoryBean;
import org.springframework.util.Assert;

//FIXME test
public class InternalFrameworkIntegrationServletFactoryBean extends
		ServletFactoryBean {
	
	public Class<?> getObjectType() {
		return InternalFrameworkIntegrationServlet.class;
	}
	
	@Override
	protected void initServletProperties(HttpServlet servlet) {
		super.initServletProperties(servlet);
		Assert.notNull(delegateServlet, "delegateServlet cannot be null");
		
		//FIXME test
		if (!(servlet instanceof InternalFrameworkIntegrationServlet)) {
			throw new ConfigurationException(servlet + " must be an instanceof " + InternalFrameworkIntegrationServlet.class.getName());
		}
		
		InternalFrameworkIntegrationServlet integrationServlet = (InternalFrameworkIntegrationServlet) servlet;
		integrationServlet.setDelegateServlet(delegateServlet);
	}

	private HttpServlet delegateServlet;

	public void setDelegateServlet(HttpServlet servlet) {
		this.delegateServlet = servlet;
	}

}
