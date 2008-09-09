package org.impalaframework.web.generic;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.web.WebConstants;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class IntegrationServletFactoryBeanTest extends TestCase {

	private HttpServletRequest request;
	private ServletContext context;
	private IntegrationServletFactoryBean factoryBean;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		request = createMock(HttpServletRequest.class);
		context = createMock(ServletContext.class);
		factoryBean = new IntegrationServletFactoryBean();
		factoryBean.setInitParameters(null);
		factoryBean.setServletName("myservlet");
		factoryBean.setServletClass(ModuleRedirectingServlet.class);
		factoryBean.setServletContext(context);
	}
	
	public void testGetObject() throws Exception {
		
		factoryBean.afterPropertiesSet();
		ModuleRedirectingServlet servlet = (ModuleRedirectingServlet) factoryBean.getObject();
		
		expect(request.getServletPath()).andReturn("/somepath/morebits");
		expect(context.getAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE+"somepath")).andReturn(null);
		
		replay(request);
		replay(context);
		
		servlet.service(request, null);
		
		verify(request);
		verify(context);
	}
	
	public void testWithPrefix() throws Exception {
		
		factoryBean.setInitParameters(Collections.singletonMap("modulePrefix", "pathprefix-"));
		factoryBean.afterPropertiesSet();
		ModuleRedirectingServlet servlet = (ModuleRedirectingServlet) factoryBean.getObject();
		
		expect(request.getServletPath()).andReturn("/somepath/morebits");
		expect(context.getAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE+"pathprefix-somepath")).andReturn(null);
		
		replay(request);
		replay(context);
		
		servlet.service(request, null);
		
		verify(request);
		verify(context);
	}

}
