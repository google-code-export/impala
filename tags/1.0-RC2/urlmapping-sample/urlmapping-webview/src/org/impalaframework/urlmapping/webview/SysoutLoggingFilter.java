package org.impalaframework.urlmapping.webview;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SysoutLoggingFilter implements Filter {
    public void destroy() {
    }

    public void init(javax.servlet.FilterConfig filterConfig)
            throws javax.servlet.ServletException {
    };

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        System.out.println("In filter ... " + System.currentTimeMillis());
        chain.doFilter(request, response);
    }
}
