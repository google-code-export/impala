package net.java.impala.spring.web;

import javax.servlet.ServletContext;

import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.util.ApplicationContextLoader;

import org.springframework.web.context.WebApplicationContext;

public interface WebApplicationContextLoader extends ApplicationContextLoader {

	WebApplicationContext loadParentWebContext(WebApplicationContext parent, PluginSpec pluginSpec, ServletContext context);

}