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

package org.impalaframework.classloader;

import java.io.File;
import java.net.URL;

/**
 * Concrete implementation of <code>URLClassLoader</code> which will attempt
 * to load from the named class locations BEFORE attempting to load using the
 * parent class location.
 * 
 * @see BaseURLClassLoader
 * @author Phil Zoio
 */
public class ModuleClassLoader extends CustomClassLoader {

    public ModuleClassLoader(File[] locations) {
        super(locations);
    }

    public ModuleClassLoader(ClassLoader parent, File[] locations) {
        super(parent, locations);
    }
    
    public ModuleClassLoader(URL[] locations) {
        super(locations);
    }

    public ModuleClassLoader(ClassLoader parent, URL[] locations) {
        super(parent, locations);
    }

    @Override
    protected boolean loadCustomClassFirst() {
        return true;
    }
    
}
