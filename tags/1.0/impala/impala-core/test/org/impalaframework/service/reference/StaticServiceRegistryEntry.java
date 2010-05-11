package org.impalaframework.service.reference;

import java.util.List;
import java.util.Map;

import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;

public class StaticServiceRegistryEntry extends BasicServiceRegistryEntry {

    public StaticServiceRegistryEntry(Object service, 
            String beanName, 
            String contributingModule, 
            ClassLoader classLoader) {
        super(new StaticServiceBeanReference(service), beanName, contributingModule, classLoader);
    }

    public StaticServiceRegistryEntry(Object service,
            String beanName, 
            String contributingModule,
            List<Class<?>> exportTypes, 
            Map<String, ?> attributes,
            ClassLoader classLoader) {
        super(new StaticServiceBeanReference(service), beanName, contributingModule, exportTypes, attributes,
                classLoader);
    }

}
