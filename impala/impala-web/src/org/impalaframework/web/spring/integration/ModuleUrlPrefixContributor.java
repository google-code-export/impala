package org.impalaframework.web.spring.integration;

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.web.integration.PrefixTreeHolder;
import org.impalaframework.web.integration.UrlPrefixRequestModuleMapper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ServletContextAware;

/**
 * Life-cycle handler which can be used to register URL paths on behalf of a particular module.
 * Contributes prefix to module name mappings on behalf of module to a shared {@link PrefixTreeHolder}.
 * 
 * These in turn are used by the {@link UrlPrefixRequestModuleMapper} to map individual requests to this module, based
 * on registered URL prefixes.
 * 
 * Bean definition should use {@link #setPrefixes(Map)} to set the prefixes for requests which should be mapped to this
 * module.
 * 
 * @author Phil Zoio
 */
public class ModuleUrlPrefixContributor implements ModuleDefinitionAware, ServletContextAware, InitializingBean, DisposableBean {
    
    private static final Log logger = LogFactory.getLog(ModuleUrlPrefixContributor.class);

    private ModuleDefinition moduleDefinition;
    
    private ServletContext servletContext;
    
    private Map<String, ContextAndServletPath> prefixMap;
    
    public void afterPropertiesSet() throws Exception {
        
        Assert.notNull(moduleDefinition, "moduleDefinition cannot be null");
        Assert.notNull(servletContext, "servletContext cannot be null");
        Assert.notNull(prefixMap, "prefixes cannot be null");
        
        PrefixTreeHolder holder = getPrefixHolder(); 
        
        if (holder != null) {
            final String name = moduleDefinition.getName();
            Set<String> prefixKeys = prefixMap.keySet();
            
            for (String prefix : prefixKeys) {
                if (logger.isDebugEnabled()) 
                    logger.debug("Contributing to holder: " + ObjectUtils.identityToString(holder) + ": " + name + "-" + prefix);
                
                final ContextAndServletPath paths = prefixMap.get(prefix);
                String servletPath = paths.getServletPath();
                String contextPath = paths.getContextPath();
                holder.add(name, prefix, contextPath, servletPath);
            }
        }
    }

    public void destroy() throws Exception {

        PrefixTreeHolder holder = getPrefixHolder(); 
        
        if (holder != null) {
            final String name = moduleDefinition.getName();
            Set<String> prefixKeys = prefixMap.keySet();
            
            for (String prefix : prefixKeys) {
                holder.remove(name, prefix);
            }
        }
    }

    private PrefixTreeHolder getPrefixHolder() {
        PrefixTreeHolder holder = (PrefixTreeHolder) servletContext.getAttribute(UrlPrefixRequestModuleMapper.PREFIX_HOLDER_KEY);
        if (holder == null) {
            logger.warn("No " + PrefixTreeHolder.class.getSimpleName() + " instance available in servlet context under key " + UrlPrefixRequestModuleMapper.PREFIX_HOLDER_KEY);
        }
        return holder;
    }

    public void setModuleDefinition(ModuleDefinition moduleDefinition) {
        this.moduleDefinition = moduleDefinition;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setPrefixMap(Map<String, ContextAndServletPath> prefixMap) {
        this.prefixMap = prefixMap;
    }
}
