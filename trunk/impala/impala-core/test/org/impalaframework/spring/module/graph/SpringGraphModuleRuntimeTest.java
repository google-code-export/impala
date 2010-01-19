package org.impalaframework.spring.module.graph;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.Impala;
import org.impalaframework.file.FileMonitor;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.module.source.InternalModuleDefinitionSource;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.spring.module.SpringModuleUtils;
import org.impalaframework.spring.module.SpringRuntimeModule;
import org.impalaframework.spring.service.bean.ParentFactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class SpringGraphModuleRuntimeTest extends TestCase implements ModuleDefinitionSource {

    public void setUp() {
        Impala.clear();
        System.setProperty("graph.bean.visibility.type", "parentFirst");
    }

    public void tearDown() {
        try {
            Impala.clear();
        }
        catch (Exception e) {
        }
        System.clearProperty("classloader.type");
        System.clearProperty("graph.bean.visibility.type");
    }
    
    public void testGraph() throws Exception {
        System.setProperty("graph.bean.visibility.type", "graphOrdered");
        Impala.init();
        Impala.init(this);

        executeBean("sample-module4", "bean2");
        executeBean("sample-module6", "bean4");
    }
    
    public void testParentFirst() throws Exception {
        System.setProperty("graph.bean.visibility.type", "parentFirst");
        Impala.init();
        Impala.init(this);

        executeBean("sample-module4", "bean2");
        executeBean("sample-module6", "bean4");
    }
    
    public void testParentOnly() throws Exception {
        System.setProperty("graph.bean.visibility.type", "parentOnly");
        Impala.init();
        Impala.init(this);

        executeBean("sample-module4", "bean2");
        executeNoBean("sample-module6", "bean4");
    }
    
    public void testParentBean() throws Exception {
        System.setProperty("graph.bean.visibility.type", "graphOrdered");
        Impala.init();
        Impala.init(new ModuleDefinitionWithParent());

        executeBean("sample-module4", "bean2");    
        RuntimeModule runtimeModule = Impala.getRuntimeModule("sample-module6");
        SpringRuntimeModule mod = (SpringRuntimeModule) runtimeModule;
        final ParentFactoryBean bean = (ParentFactoryBean) mod.getApplicationContext().getBean("&bean4");
        assertSame(bean.getObject(), Impala.getModuleBean("sample-module4", "bean4", Object.class));
    }
    
    public void testNone() throws Exception {
        System.setProperty("graph.bean.visibility.type", "none");
        Impala.init();
        Impala.init(this);

        executeNoBean("sample-module4", "bean2");
        executeNoBean("sample-module6", "bean4");
    }
    
    public void testDuff() throws Exception {
        final SpringGraphModuleRuntime moduleRuntime = new SpringGraphModuleRuntime(){

            @Override
            protected ApplicationContext internalGetParentApplicationContext(
                    Application application, ModuleDefinition definition) {
                return EasyMock.createMock(ApplicationContext.class);
            }
            
        };
        moduleRuntime.setBeanVisibilityType("duff");
        try {
            moduleRuntime.getParentApplicationContext(TestApplicationManager.newApplicationManager().getCurrentApplication(), null);
        } catch (ConfigurationException e) {
            assertEquals("Invalid value for property graph.bean.visibility.type. Permissible values are [none, parentOnly, parentFirst, graphOrdered]", e.getMessage());
        }
    }

    public void testGraphInheritanceStrategies() throws Exception {

        Impala.init();
        System.out.println(getModuleDefinition());
        
        Impala.init(this);
        Application application = Impala.getCurrentApplication();
        ModuleStateHolder moduleStateHolder = application.getModuleStateHolder();
        
        BaseBeanGraphInheritanceStrategy strategy = new ParentFirstBeanGraphInheritanceStrategy();
        assertTrue(strategy.getDelegateGetBeanCallsToParent());

        checkExpected(moduleStateHolder, strategy, "sample-module4", 0);
        checkExpected(moduleStateHolder, strategy, "sample-module6", 3);
        checkExpected(moduleStateHolder, strategy, "sample-module5", 0);
        checkExpected(moduleStateHolder, strategy, "sample-module2", 0);
        checkExpected(moduleStateHolder, strategy, "impala-core", 0);
        
        strategy = new GraphOrderedBeanInheritanceStrategy();
        assertFalse(strategy.getDelegateGetBeanCallsToParent());

        checkExpected(moduleStateHolder, strategy, "sample-module4", 2);
        checkExpected(moduleStateHolder, strategy, "sample-module6", 4);
        checkExpected(moduleStateHolder, strategy, "sample-module5", 0);
        checkExpected(moduleStateHolder, strategy, "sample-module2", 1);
        checkExpected(moduleStateHolder, strategy, "impala-core", 0);
        
        executeBean("sample-module6", "bean4");
    }

    private void executeNoBean(final String moduleName, final String beanName) {
        try {
            executeBean(moduleName, beanName);
            fail();
        } catch (NoSuchBeanDefinitionException e) {
        }
    }

    private void executeBean(String moduleName, String beanName) {
        RuntimeModule runtimeModule = Impala.getRuntimeModule(moduleName);
        FileMonitor bean = (FileMonitor) runtimeModule.getBean(beanName);
        bean.lastModified(new File("./"));
    }

    private void checkExpected(ModuleStateHolder moduleStateHolder,
            BaseBeanGraphInheritanceStrategy strategy, String moduleName, int expected) {
        ModuleDefinition definition = moduleStateHolder.getModuleDefinition().findChildDefinition(moduleName, true);

        final ModuleDefinition parentDefinition = definition.getParentDefinition();
        ApplicationContext parent = getApplicationContext(moduleStateHolder,
                parentDefinition);
        
        List<ApplicationContext> contexts = strategy.getDependentApplicationContexts(definition, parent, (GraphModuleStateHolder)moduleStateHolder);
        System.out.println(contexts);
        assertEquals(expected, contexts.size());
        
        for (ApplicationContext applicationContext : contexts) {
            System.out.println(applicationContext.getDisplayName());
        }
    }

    private ApplicationContext getApplicationContext(ModuleStateHolder moduleStateHolder, final ModuleDefinition parentDefinition) {
        RuntimeModule parentModule = null;
        if (parentDefinition != null) parentModule = moduleStateHolder.getModule(parentDefinition.getName());
        
        return SpringModuleUtils.getModuleSpringContext(parentModule);
    }

    public RootModuleDefinition getModuleDefinition() {
        return new InternalModuleDefinitionSource(TypeReaderRegistryFactory.getTypeReaderRegistry(), 
                Impala.getFacade().getModuleManagementFacade().getModuleLocationResolver(), 
                new String[] { "impala-core", "sample-module4", "sample-module5", "sample-module6" }).getModuleDefinition();
    }
}

class ModuleDefinitionWithParent implements ModuleDefinitionSource {

    public RootModuleDefinition getModuleDefinition() {
        final RootModuleDefinition moduleDefinition = new InternalModuleDefinitionSource(
                TypeReaderRegistryFactory.getTypeReaderRegistry(), 
                Impala.getFacade().getModuleManagementFacade().getModuleLocationResolver(), 
                new String[] { "impala-core", "sample-module4", "sample-module5" }).getModuleDefinition();
        
        final SimpleModuleDefinition childDefinition = (SimpleModuleDefinition) moduleDefinition.findChildDefinition("sample-module5", true);
        
        new SimpleModuleDefinition(
                childDefinition, 
                "sample-module6", 
                ModuleTypes.APPLICATION,
                new String[]{"sample-module6-context.xml", "sample-module6-parent.xml"},
                new String[]{"sample-module4"},
                null,
                "spring");
        return moduleDefinition;
    }}
