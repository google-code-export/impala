package org.impalaframework.service;

import java.util.List;
import java.util.Map;

public interface ServiceRegistryEntry {

    /**
     * Returns the bean backing the service reference
     */
    ServiceBeanReference getServiceBeanReference();

    /**
     * The export types of the service. May return empty list but will not return null.
     */
    List<Class<?>> getExportTypes();
    
    /**
     * Returns the name of the bean 
     */
    String getBeanName();

    /**
     * Returns the name of the contributing module for the service reference
     */
    String getContributingModule();

    /**
     * Returns the arbitrary attributes attached to the service reference. May return empty map but will not return null.
     */
    Map<String, ?> getAttributes();
    
    /**
     * Returns the class loader used to load the bean
     */
    ClassLoader getBeanClassLoader();

}
