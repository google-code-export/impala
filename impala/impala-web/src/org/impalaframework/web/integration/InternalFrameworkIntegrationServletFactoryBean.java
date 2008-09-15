package org.impalaframework.web.integration;

import javax.servlet.http.HttpServlet;

import org.impalaframework.exception.ConfigurationException;
import org.springframework.util.Assert;

/**
 * <code>FactoryBean</code> whose purpose is solely to create an instance of <code>InternalFrameworkIntegrationServlet</code>
 * @author Phil Zoio
 */
public class InternalFrameworkIntegrationServletFactoryBean extends
		ServletFactoryBean {

	private HttpServlet delegateServlet;
	
	public Class<?> getObjectType() {
		return InternalFrameworkIntegrationServlet.class;
	}
	
	@Override
	protected void initServletProperties(HttpServlet servlet) {
		super.initServletProperties(servlet);
		Assert.notNull(delegateServlet, "delegateServlet cannot be null");
		
		if (!(servlet instanceof InternalFrameworkIntegrationServlet)) {
			throw new ConfigurationException(servlet + " must be an instanceof " + InternalFrameworkIntegrationServlet.class.getName());
		}
		
		InternalFrameworkIntegrationServlet integrationServlet = (InternalFrameworkIntegrationServlet) servlet;
		integrationServlet.setDelegateServlet(delegateServlet);
	}
	
	/* *************** Injected setters ***************** */

	public void setDelegateServlet(HttpServlet servlet) {
		this.delegateServlet = servlet;
	}

}
