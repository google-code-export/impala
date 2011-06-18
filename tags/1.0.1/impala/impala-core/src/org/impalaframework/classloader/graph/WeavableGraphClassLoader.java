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

import java.lang.instrument.ClassFileTransformer;

import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.module.ModuleDefinition;
import org.springframework.instrument.classloading.WeavingTransformer;

/**
 * 
 * @author Phil Zoio
 */
public class WeavableGraphClassLoader extends GraphClassLoader {

    private final WeavingTransformer weavingTransformer;
    
    public WeavableGraphClassLoader(
            ClassLoader parentClassLoader,
            DelegateClassLoader delegateClassLoader,
            ClassRetriever classRetriever, 
            ModuleDefinition definition,
            boolean loadParentFirst) {
        super(parentClassLoader, delegateClassLoader, classRetriever, definition, loadParentFirst);
        
        weavingTransformer = new WeavingTransformer(this);
    }
    
    public void addTransformer(ClassFileTransformer transformer) {
        this.weavingTransformer.addTransformer(transformer);
    }

}
