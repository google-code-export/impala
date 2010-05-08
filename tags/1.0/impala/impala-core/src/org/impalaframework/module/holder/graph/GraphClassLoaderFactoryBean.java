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

package org.impalaframework.module.holder.graph;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * {@link FactoryBean} implementation returns instanceof {@link WeavingGraphClassLoaderFactory} or {@link GraphClassLoaderFactory},
 * depending on whether {@link #aspectjAwareClassLoader} is set to true or not
 */
public class GraphClassLoaderFactoryBean implements FactoryBean, InitializingBean {

    private boolean aspectjAwareClassLoader;
    
    private ModuleLocationResolver moduleLocationResolver;
    
    private boolean parentClassLoaderFirst;
    
    private GraphClassLoaderFactory classLoaderFactory;
    
    public void afterPropertiesSet() throws Exception {
        if (aspectjAwareClassLoader) {
            this.classLoaderFactory = new WeavingGraphClassLoaderFactory();
        } else {
            this.classLoaderFactory = new GraphClassLoaderFactory();
        }
    
        this.classLoaderFactory.setModuleLocationResolver(moduleLocationResolver);
        this.classLoaderFactory.setParentClassLoaderFirst(parentClassLoaderFirst);
        this.classLoaderFactory.init();
    }

    public Object getObject() throws Exception {
        return classLoaderFactory;
    }

    public Class<?> getObjectType() {
        return GraphClassLoaderFactory.class;
    }

    public boolean isSingleton() {
        return true;
    }
    
    public void setAspectjAwareClassLoader(boolean aspectjAwareClassLoader) {
        this.aspectjAwareClassLoader = aspectjAwareClassLoader;
    }    
    
    public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
        this.moduleLocationResolver = moduleLocationResolver;
    }
    
    public void setParentClassLoaderFirst(boolean parentClassLoaderFirst) {
        this.parentClassLoaderFirst = parentClassLoaderFirst;
    }

}
