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

package org.impalaframework.classloader.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.util.SerializationUtils;

public class GraphTestUtils {

    public static ModuleDefinition newDefinition(List<ModuleDefinition> list, ModuleDefinition parent, final String name, final String dependencies) {
        final String[] split = dependencies != null ? dependencies.split(",") : new String[0];
        ModuleDefinition definition = new SimpleModuleDefinition(parent, name, ModuleTypes.APPLICATION, null, split, null, null);
        list.add(definition);
        return definition;
    }

    public static SimpleRootModuleDefinition cloneAndMarkStale(SimpleRootModuleDefinition root1,
            final String toReload) {
        final SimpleRootModuleDefinition clone = (SimpleRootModuleDefinition) SerializationUtils.clone(root1);
        final ModuleDefinition child = clone.findChildDefinition(toReload, true);
        System.out.println("Marking " + toReload + " as stale");
        child.setState(ModuleState.STALE);
        return clone;
    }

    public static void printTransitions(
            final Collection<? extends ModuleStateChange> moduleTransitions) {
        for (ModuleStateChange moduleStateChange : moduleTransitions) {
            System.out.println(moduleStateChange.getTransition() + " - " + moduleStateChange.getModuleDefinition().getName());
        }
    }

    public static ModuleDefinition findDefintion(SimpleRootModuleDefinition rootDefinition, String moduleName) {
        ModuleDefinition findDefinition = rootDefinition.findChildDefinition(moduleName, true);
        return findDefinition;
    }

    public static void assertContainsOnly(Collection<ModuleDefinition> moduleDefinitions, String expectedDefinitionNames) {
        List<String> expectedNames = expectedAsList(expectedDefinitionNames);
        
        List<String> actualNames = ModuleDefinitionUtils.getModuleNamesFromCollection(moduleDefinitions);
        for (String actual : actualNames) {
            Assert.assertTrue("Expected module not present in list: " + actual, expectedNames.contains(actual));
        }
        Assert.assertEquals("Expected module names and actual module names are of different size. Expected: " + expectedNames + ", actual: " + actualNames + ".", expectedNames.size(), actualNames.size());
    }

    public static List<String> expectedAsList(String expectedDefinitionNames) {
        List<String> expectedNames = Arrays.asList(expectedDefinitionNames != null ? expectedDefinitionNames.split(","): new String[0]);
        return expectedNames;
    }

    public static void assertModules(String expected, Collection<ModuleDefinition> moduleDefinitions) {
        List<String> actualNames = ModuleDefinitionUtils.getModuleNamesFromCollection(moduleDefinitions);
        List<String> expectedNames = expectedAsList(expected);
        Assert.assertEquals(expectedNames, actualNames);
    }

}
