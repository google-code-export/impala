package org.impalaframework.jmx.bootstrap;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jmx.MBeanServerNotFoundException;

/**
 * Extends Spring {@link MBeanServerFactoryBean}, providing option to explicitly use platform MBean server.
 * Works for both Spring 2.5.x and 3.0.x
 * @author Phil Zoio
 */
public class MBeanServerFactoryBean extends org.springframework.jmx.support.MBeanServerFactoryBean {

    private static final Log logger = LogFactory.getLog(MBeanServerFactoryBean.class);
    
    private boolean preferPlatformMbeanServer;
    
    @Override
    protected MBeanServer locateMBeanServer(String agentId) throws MBeanServerNotFoundException {
        MBeanServer mbeanServer = null;
        if (preferPlatformMbeanServer) {
            try {
                mbeanServer = ManagementFactory.getPlatformMBeanServer();
            }
            catch (SecurityException e) {
                logger.warn("Property '" + JMXBootstrapProperties.JMX_PREFER_PLATFORM_MBEAN_SERVER_DEFAULT + "' set to true, but accessing of platform MBean server not allowed.", e);
            }
        }
        
        if (mbeanServer != null) {
            return mbeanServer;
        }
        return super.locateMBeanServer(agentId);
    }
    
    public void setPreferPlatformMbeanServer(boolean preferPlatformMbeanServer) {
        this.preferPlatformMbeanServer = preferPlatformMbeanServer;
    }
    
}
