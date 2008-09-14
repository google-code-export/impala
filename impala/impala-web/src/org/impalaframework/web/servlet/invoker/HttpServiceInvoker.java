package org.impalaframework.web.servlet.invoker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpServiceInvoker {

	void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}