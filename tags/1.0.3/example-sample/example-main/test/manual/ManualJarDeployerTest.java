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

package manual;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.facade.BootstrappingOperationFacade;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.source.SimpleModuleDefinitionSource;
import org.impalaframework.util.ReflectionUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * This is a manual test which is not part of the suite. First run an ant build (using the command 'ant') before running this test
 * from the IDE
 * @author Phil Zoio
 */
public class ManualJarDeployerTest extends TestCase implements ModuleDefinitionSource {

    public void testRun() throws Exception {
        String workspaceRoot = "../deploy";
        File file = new File(workspaceRoot);
        assertTrue(file.exists());
        
        System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, workspaceRoot);
        System.setProperty(LocationConstants.APPLICATION_VERSION, "SNAPSHOT");
        BootstrappingOperationFacade facade = new BootstrappingOperationFacade();
        facade.init(this);
        
        RuntimeModule runtimeModule = facade.getRootRuntimeModule();
        ClassLoader classLoader = runtimeModule.getClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        
        Object bean = runtimeModule.getBean("entryDAO");
        System.out.println(ReflectionUtils.invokeMethod(bean, "toString", new Object[0]));
        
        Class<?> entryClass = Class.forName("classes.Entry", false, classLoader);
        Object entry = entryClass.newInstance();
        BeanWrapper entryWrapper = new BeanWrapperImpl(entry);
        entryWrapper.setPropertyValue("title", "mytitle");
        
        System.out.println(ReflectionUtils.invokeMethod(bean, "save", new Object[]{ entry }));
    }
    
    public RootModuleDefinition getModuleDefinition() {
        return new SimpleModuleDefinitionSource("example", new String[] { "parent-context.xml" }, new String[] { "example-dao", "example-hibernate" }).getModuleDefinition();
    }
    
}
