package org.impalaframework.web.integration;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.web.spring.module.WebModuleLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class ServletRootModuleLoader extends WebModuleLoader {
    @Override
    public void afterRefresh(ConfigurableApplicationContext context, ModuleDefinition definition) {
        
         getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
 
    }
}
