package tapestry5;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.PathUtils;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationFilter;

public class PathModificationIntegrationFilter extends InternalFrameworkIntegrationFilter {
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		this.prefix = config.getInitParameter("prefix");
	}

	private String prefix;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		final HttpServletRequest httpServletRequest = ObjectUtils.cast(request, HttpServletRequest.class);
		HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpServletRequest) {

			@Override
			public String getServletPath() {
				return removePrefix(super.getServletPath());
			}
			
			@Override
			public String getPathInfo() {
				return removePrefix(super.getPathInfo());
			}
			
			private String removePrefix(final String value) {
				return PathUtils.trimPrefix(value, prefix);
			}
			
		};
		
		super.doFilter(wrappedRequest, response, chain);
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	
}
