package net.java.impala.spring.web;

import java.util.List;

import javax.servlet.ServletContext;

import net.java.impala.spring.util.ApplicationContextLoader;

import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;

public interface WebApplicationContextLoader extends ApplicationContextLoader {

	WebApplicationContext loadWebContext(WebApplicationContext parent, String servletName, ServletContext context,
			List<Resource> resourceLocations);

}