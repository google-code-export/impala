package org.impalaframework.service.registry.exporttype;

import java.util.List;

/**
 * Represents the mechanims for deriving the export types for a particular service implementation
 * @author Phil Zoio
 */
public interface ExportTypeDeriver {
    
    public List<Class<?>> deriveExportTypes(Object service, String beanName, List<Class<?>> classes);

}
