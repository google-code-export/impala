/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.spring.facade;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidBeanTypeException;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.facade.Impala;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.file.FileMonitor;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.source.SimpleModuleDefinitionSource;
import org.impalaframework.spring.module.SpringModuleUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class ImpalaTest extends TestCase {

    private static final String plugin1 = "sample-module1";

    private static final String plugin2 = "sample-module2";

    private static final String plugin3 = "sample-module3";

    public void setUp() {
        Impala.clear();
    }

    public void tearDown() {
        try {
            Impala.clear();
        }
        catch (Exception e) {
        }
    }

    public void testNoInit() {
        try {
            Impala.getRootRuntimeModule();
            fail();
        }
        catch (NoServiceException e) {
        }
    }

    public void testInit() {

        final Test1 test1 = new Test1();
        Impala.init(test1);
        assertEquals(test1.getModuleDefinition(), Impala.getRootModuleDefinition());

        assertTrue(Impala.hasModule(plugin1));
        final ApplicationContext context1 = SpringModuleUtils.getModuleSpringContext(Impala.getRootRuntimeModule());
        final ConfigurableApplicationContext p11 = getModule(plugin1);      
        ApplicationContext moduleContext = SpringModuleUtils.getModuleSpringContext(Impala.getRuntimeModule(plugin1));
        assertSame(p11, moduleContext);
        
        assertNotNull(p11);
        noServiceException(plugin2);
        noServiceException(plugin3);

        FileMonitor f1 = (FileMonitor) context1.getBean("bean1");
        FileMonitor f2 = (FileMonitor) context1.getBean("bean2");
        FileMonitor f3 = (FileMonitor) context1.getBean("bean3");
        
        FileMonitor pluginBean = Impala.getModuleBean(plugin1, "bean1", FileMonitor.class);
        assertEquals("classes.FileMonitorBean1", pluginBean.getClass().getName());
        
        noServiceException("unknownPlugin");

        service(f1);
        noService(f2);
        noService(f3);

        final Test2 test2 = new Test2();
        Impala.init(test2);
        assertEquals(test2.getModuleDefinition(), Impala.getRootModuleDefinition());

        assertTrue(Impala.hasModule(plugin1));
        assertTrue(Impala.hasModule(plugin2));
        final ApplicationContext context2 = SpringModuleUtils.getModuleSpringContext(Impala.getRootRuntimeModule());
        final ConfigurableApplicationContext p12 = getModule(plugin1);
        assertNotNull(p12);
        assertSame(p11, p12);
        final ConfigurableApplicationContext p22 = getModule(plugin2);
        assertNotNull(p22);
        noServiceException(plugin3);

        f1 = (FileMonitor) context2.getBean("bean1");
        f2 = (FileMonitor) context2.getBean("bean2");
        f3 = (FileMonitor) context2.getBean("bean3");

        service(f1);
        service(f2);
        noService(f3);

        // context still same
        assertSame(context1, context2);
        assertTrue(Impala.hasModule(plugin1));
        assertTrue(Impala.hasModule(plugin2));

        // now load plugin 3 as well
        final Test3 test3 = new Test3();
        Impala.init(test3);
        assertEquals(test3.getModuleDefinition(), Impala.getRootModuleDefinition());

        final ApplicationContext context3 = SpringModuleUtils.getModuleSpringContext(Impala.getRootRuntimeModule());
        final ConfigurableApplicationContext p13 = getModule(plugin1);
        assertSame(p11, p13);
        final ConfigurableApplicationContext p23 = getModule(plugin2);
        assertSame(p22, p23);
        final ConfigurableApplicationContext p33 = getModule(plugin3);
        assertNotNull(p33);

        f1 = (FileMonitor) context3.getBean("bean1");
        f2 = (FileMonitor) context3.getBean("bean2");
        f3 = (FileMonitor) context3.getBean("bean3");

        FileMonitor f3PluginBean = Impala.getModuleBean(plugin1, "bean3", FileMonitor.class);
        assertSame(f3, f3PluginBean);

        // context still same
        assertSame(context1, context3);

        service(f3);
        assertTrue(Impala.hasModule(plugin1));
        assertTrue(Impala.hasModule(plugin2));
        assertTrue(Impala.hasModule(plugin3));
        
        assertTrue(Impala.hasModule(plugin1));
        assertTrue(Impala.hasModule(plugin2));
        assertTrue(Impala.hasModule(plugin3));

        // show that this will return false
        assertFalse(Impala.reloadModule("unknown"));

        // now reload plugin1
        assertTrue(Impala.reloadModule(plugin1));
        assertTrue(Impala.hasModule(plugin1));

        final ConfigurableApplicationContext p13reloaded = getModule(plugin1);
        assertNotSame(p13reloaded, p13);
        FileMonitor f1reloaded = (FileMonitor) context3.getBean("bean1");

        assertEquals(f1.lastModified((File) null), f1reloaded.lastModified((File) null));
        service(f1reloaded);
        assertSame(f1reloaded, f1);

        // now reload plugin2, which will also reload plugin3
        assertTrue(Impala.reloadModule(plugin2));
        assertTrue(Impala.hasModule(plugin2));

        final ConfigurableApplicationContext p23reloaded = getModule(plugin2);
        assertNotSame(p23reloaded, p23);

        final ConfigurableApplicationContext p33reloaded = getModule(plugin3);
        assertNotSame(p33reloaded, p33);

        FileMonitor f3reloaded = (FileMonitor) context3.getBean("bean3");

        assertEquals(f3.lastModified((File) null), f3reloaded.lastModified((File) null));
        service(f3reloaded);
        assertSame(f3reloaded, f3);

        // show that this will return null
        assertNull(Impala.reloadModuleLike("unknown"));

        // now test reloadLike
        assertEquals(plugin2, Impala.reloadModuleLike("module2"));
        f3reloaded = (FileMonitor) context3.getBean("bean3");
        service(f3reloaded);

        // now remove plugin2 (and by implication, child plugin3)
        assertFalse(Impala.removeModule("unknown"));
        assertTrue(Impala.removeModule(plugin2));
        assertFalse(Impala.hasModule(plugin2));
        // check that the child is gone too
        assertFalse(Impala.hasModule(plugin3));

        final ModuleDefinition test3RootDefinition = Impala.getRootModuleDefinition();
        assertTrue(test3RootDefinition.hasChildModuleDefinition(plugin1));
        assertFalse(test3RootDefinition.hasChildModuleDefinition(plugin2));

        f3reloaded = (FileMonitor) context3.getBean("bean3");
        FileMonitor f2reloaded = (FileMonitor) context3.getBean("bean2");
        noService(f3reloaded);
        noService(f2reloaded);
    }

    public void testAdd() {
        final Test1 test1 = new Test1();
        Impala.init(test1);

        final ApplicationContext context1 = SpringModuleUtils.getModuleSpringContext(Impala.getRootRuntimeModule());
        FileMonitor f1 = (FileMonitor) context1.getBean("bean1");
        FileMonitor f2 = (FileMonitor) context1.getBean("bean2");

        service(f1);
        noService(f2);
        Impala.addModule(new SimpleModuleDefinition(plugin2));
        service(f1);
        service(f2);
    }

    public void testReloadParent() {
        final Test1 test1 = new Test1();
        Impala.init(test1);

        final ApplicationContext context1a = SpringModuleUtils.getModuleSpringContext(Impala.getRootRuntimeModule());
        FileMonitor f1 = Impala.getBean("bean1", FileMonitor.class);
        service(f1);
        Impala.reloadRootModule();
        final ApplicationContext context1b = SpringModuleUtils.getModuleSpringContext(Impala.getRootRuntimeModule());
        f1 = Impala.getBean("bean1", FileMonitor.class);
        service(f1);

        assertFalse(context1a == context1b);
    }

    public void testGetBean() {
        final Test1 test1 = new Test1();
        Impala.init(test1);
        try {
            Impala.getBean("bean1", OperationsFacade.class);
        }
        catch (InvalidBeanTypeException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("Class loader of bean: "));
            assertTrue(e.getMessage().contains("Class loader of required type: "));
            assertTrue(e.getMessage().contains("Bean named 'bean1' must be of type [org.impalaframework.facade.OperationsFacade], but was actually of type ["));
        }
    }

    public void testUnloadParent() {
        final Test1 test1 = new Test1();
        Impala.init(test1);
        Impala.unloadRootModule();
        try {
            Impala.getRootRuntimeModule();
        }
        catch (NoServiceException e) {
        }
        //getFacade can still be called
        Impala.getFacade();
    }
    
    public void testClear() {
        final Test1 test1 = new Test1();
        Impala.init(test1);
        Impala.clear();
        
        try {
            Impala.getRootRuntimeModule();
        }
        catch (NoServiceException e) {
        }
        
        try {
            Impala.getFacade();
        }
        catch (NoServiceException e) {
        }
    }

    private void service(FileMonitor f) {
        f.lastModified((File) null);
    }

    private void noService(FileMonitor f) {
        try {
            service(f);
            fail();
        }
        catch (NoServiceException e) {
        }
    }

    private void noServiceException(String moduleName) {
        try {
            Impala.getRuntimeModule(moduleName);
            fail();
        }
        catch (NoServiceException e) {
            assertEquals("No module named '" + moduleName +
                    "' has been loaded", e.getMessage());
        }
    }

    private ConfigurableApplicationContext getModule(String name) {
        final ConfigurableApplicationContext p11 = SpringModuleUtils.getModuleSpringContext(Impala.getRuntimeModule(name));
        return p11;
    }

    class Test1 implements ModuleDefinitionSource {
        ModuleDefinitionSource source = new SimpleModuleDefinitionSource("impala-core", new String[] { "parentTestContext.xml" }, new String[] { plugin1 });

        public RootModuleDefinition getModuleDefinition() {
            return source.getModuleDefinition();
        }
    }

    class Test2 implements ModuleDefinitionSource {
        ModuleDefinitionSource source = new SimpleModuleDefinitionSource("impala-core", new String[] { "parentTestContext.xml" }, new String[] { plugin1, plugin2 });

        public RootModuleDefinition getModuleDefinition() {
            return source.getModuleDefinition();
        }
    }

    class Test3 implements ModuleDefinitionSource {
        ModuleDefinitionSource source = new SimpleModuleDefinitionSource("impala-core", new String[] { "parentTestContext.xml" }, new String[] { plugin1, plugin2 });

        public Test3() {

            ModuleDefinition p2 = source.getModuleDefinition().getChildModuleDefinition(plugin2);
            new SimpleModuleDefinition(p2, plugin3);
        }

        public RootModuleDefinition getModuleDefinition() {
            return source.getModuleDefinition();
        }
    }
}
