package org.impalaframework.web.servlet.invoker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletInvokerUtils {
	
	public static void invoke(Object target, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (target instanceof HttpServiceInvoker) {
			HttpServiceInvoker invoker = (HttpServiceInvoker) target;
			invoker.invoke(request, response);
		} else if (target instanceof HttpServlet) {
			HttpServlet servlet = (HttpServlet) target;
			servlet.service(request, response);
		}
		
	}
}
