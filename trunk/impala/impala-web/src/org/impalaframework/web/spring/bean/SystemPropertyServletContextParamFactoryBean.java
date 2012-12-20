package org.impalaframework.web.spring.bean;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

public class SystemPropertyServletContextParamFactoryBean implements
        FactoryBean<Object>, ServletContextAware, InitializingBean {
    
    private static final Log logger = LogFactory.getLog(SystemPropertyServletContextParamFactoryBean.class);

    private ServletContext servletContext;
    
    private String prefix;
    
    private String parameterName;

    private String defaultValue;
    
    private String value;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(parameterName, "parameterName cannot be null");
        
        String candidateValue = System.getProperty(parameterName);          
        if (candidateValue != null) {
            logger.info("Used system property to set value for parameter '" + parameterName + "' to " + candidateValue);
        }
        
        if (candidateValue == null){
            candidateValue = servletContext.getInitParameter(parameterName);
            if (candidateValue != null) {
                logger.info("Used servlet context init parameter to set value for parameter '" + parameterName + "' to " + candidateValue);
            }
        }
        
        if (candidateValue == null) {
            candidateValue = defaultValue;
            if (candidateValue != null) {
                logger.info("Used default value for parameter '" + parameterName + "' to " + candidateValue);
            }
        }

        if (prefix == null)
            this.value = candidateValue;
        else
            this.value = prefix + candidateValue;
    }

    public Object getObject() throws Exception {
        return value;
    }

    public Class<?> getObjectType() {
        return String.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
