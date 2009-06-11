package org.impalaframework.web.integration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

public class MappedRequestModuleMapper implements RequestModuleMapper {

	public String getModuleForRequest(HttpServletRequest request) {
		
		String path = request.getServletPath();
		System.out.println(path);
        String uri = request.getRequestURI();
        String toCheck = uri.startsWith("/") ? uri.substring(1) : uri;
    
	    String[] split = toCheck.split("/");
	    
	    String servletPath = split[1];
	    if (servletPath.equals("spring")) {
	    	return "springfaces-web";
	    }
	    return null;
	}

	public void init(ServletConfig servletConfig) {
		
	}

	public void init(FilterConfig filterConfig) {
		
	}

}
