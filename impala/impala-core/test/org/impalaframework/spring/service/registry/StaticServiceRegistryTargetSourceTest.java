package org.impalaframework.spring.service.registry;

import junit.framework.TestCase;

import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.impalaframework.spring.bean.StringFactoryBean;
import org.springframework.util.ClassUtils;

public class StaticServiceRegistryTargetSourceTest extends TestCase {

    public void testWithBean() throws Exception {
        final BasicServiceRegistryEntry reference = new StaticServiceRegistryEntry("bean", "beanName", "moduleName", ClassUtils.getDefaultClassLoader());
        doTest(reference);
    }
    
    public void testWithFactoryBean() throws Exception {
        final StringFactoryBean bean = new StringFactoryBean();
        bean.setValue("bean");
        final BasicServiceRegistryEntry reference = new StaticServiceRegistryEntry(bean, "beanName", "moduleName", ClassUtils.getDefaultClassLoader());
        doTest(reference);
    }

    private void doTest(final BasicServiceRegistryEntry reference)
            throws Exception {
        StaticServiceRegistryTargetSource targetSource = new StaticServiceRegistryTargetSource(reference);
        assertEquals("bean", targetSource.getTarget());
        assertSame(reference, targetSource.getServiceRegistryReference());
        assertEquals(false, targetSource.isStatic());
    }

}
