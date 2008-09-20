package org.impalaframework.web.servlet.invoker;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperHttpServletRequest;
import org.springframework.util.Assert;

public class ServletWrapperInvoker implements HttpServiceInvoker {

	private final Object target;
	private final String moduleName;
	private final ServletContext servletContext;
	
	public ServletWrapperInvoker(Object target, String moduleName,
			ServletContext servletContext) {
		super();
		Assert.notNull(target);
		Assert.notNull(moduleName);
		Assert.notNull(servletContext);
		this.target = target;
		this.moduleName = moduleName;
		this.servletContext = servletContext;
	}

	public void invoke(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//FIXME test
		
		HttpServletRequest wrappedRequest = new ModuleAwareWrapperHttpServletRequest(request, moduleName, servletContext);
		ServletInvokerUtils.invoke(target, wrappedRequest, response);
	}

}
