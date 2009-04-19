package org.impalaframework.spring.service.registry;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.registry.BasicServiceRegistryReference;
import org.springframework.util.ClassUtils;

public class FilteredServiceRegistryTargetSourceTest extends TestCase {

    private ServiceRegistry serviceRegistry;
    private FilteredServiceRegistryTargetSource targetSource;
    private ServiceReferenceFilter filter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry = createMock(ServiceRegistry.class);
        filter = new LdapServiceReferenceFilter("(name=value)");
        this.targetSource = new FilteredServiceRegistryTargetSource(String.class, filter, serviceRegistry);
    }
    
    public void testGetNull() {
        final List<ServiceRegistryReference> emptyList = Collections.emptyList();
        expect(serviceRegistry.getServices(filter, null)).andReturn(emptyList);
        
        replay(serviceRegistry);
        
        assertNull(this.targetSource.getServiceRegistryReference());
        
        verify(serviceRegistry);
    }
    
    public void testNoAttributes() throws Exception {
        
        final List<ServiceRegistryReference> emptyList = new ArrayList<ServiceRegistryReference>();
        final HashMap<String,?> attributes = new HashMap<String,Object>();
        
        final BasicServiceRegistryReference ref1 = new BasicServiceRegistryReference("bean1", "name1", "module", null, attributes, ClassUtils.getDefaultClassLoader());
        final BasicServiceRegistryReference ref2 = new BasicServiceRegistryReference("bean2", "name2", "module", null, attributes, ClassUtils.getDefaultClassLoader());
        
        emptyList.add(ref1);
        emptyList.add(ref2);
        expect(serviceRegistry.getServices(filter, null)).andReturn(emptyList);
        
        replay(serviceRegistry);
        
        final ServiceRegistryReference reference = this.targetSource.getServiceRegistryReference();
        assertNotNull(reference);
        assertSame(ref1, reference);
        
        verify(serviceRegistry);        
    }
    
    public void testWrongType() throws Exception {
        
        final List<ServiceRegistryReference> emptyList = new ArrayList<ServiceRegistryReference>();
        final HashMap<String,?> attributes = new HashMap<String,Object>();
        
        final BasicServiceRegistryReference ref1 = new BasicServiceRegistryReference(new Integer(1), "name1", "module", null, attributes, ClassUtils.getDefaultClassLoader());
        
        emptyList.add(ref1);
        expect(serviceRegistry.getServices(filter, null)).andReturn(emptyList);
        
        replay(serviceRegistry);
        
        final ServiceRegistryReference serviceRegistryReference = this.targetSource.getServiceRegistryReference();
        assertNull(serviceRegistryReference);
        
        verify(serviceRegistry);        
    }
}
