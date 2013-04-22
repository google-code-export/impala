package org.impalaframework.jmx.spring;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Exposes JMX-related memory operations
 * @author Phil Zoio
 */
@ManagedResource(objectName = "impala:service=memoryOperations", description = "MBean exposing memory related operations")
public class MemoryOperations {
    
    /**
     * Runs garbage collection     
     */
    @ManagedOperation(description = "Operation to reload a module")
    public void runGarbageCollection() {
        System.gc();
    }

}
