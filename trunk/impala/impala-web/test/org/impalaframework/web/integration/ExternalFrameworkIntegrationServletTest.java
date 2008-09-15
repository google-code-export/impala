package org.impalaframework.web.integration;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.web.helper.FrameworkServletContextCreator;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

public class ExternalFrameworkIntegrationServletTest extends TestCase {

	private ExternalFrameworkIntegrationServlet servlet;
	private ServletContext servletContext;
	private WebApplicationContext applicationContext;
	private HttpServlet delegateServlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FrameworkServletContextCreator creator;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servlet = new ExternalFrameworkIntegrationServlet(){
			private static final long serialVersionUID = 1L;

			@Override
			protected void publishContext(WebApplicationContext wac) {}

			@Override
			protected void initFrameworkServlet() throws ServletException, BeansException {}			
		};
		
		servletContext = createMock(ServletContext.class);
		applicationContext = createMock(WebApplicationContext.class);
		delegateServlet = createMock(HttpServlet.class);
		creator = createMock(FrameworkServletContextCreator.class);
		servlet.setFrameworkContextCreator(creator);
		
		request = createMock(HttpServletRequest.class);
		response = createMock(HttpServletResponse.class);
	}

	public void testService() throws ServletException, IOException {
		
		servletContext.log(isA(String.class));
		expect(creator.createWebApplicationContext()).andReturn(applicationContext);
		expect(applicationContext.getClassLoader()).andReturn(null);
		expect(applicationContext.getBean("myServletBeanName")).andReturn(delegateServlet);		
		
		delegateServlet.service(request, response);

		replayMocks();
		HashMap<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("delegateServletBeanName", "myServletBeanName");
		servlet.init(new IntegrationServletConfig(
				initParameters, servletContext, "myservlet"));
		servlet.doService(request, response);
		verifyMocks();
	}

	private void verifyMocks() {
		verify(servletContext);
		verify(applicationContext);
		verify(delegateServlet);
		verify(creator);
	}

	private void replayMocks() {
		replay(servletContext);
		replay(applicationContext);
		replay(delegateServlet);
		replay(creator);
	}
}
