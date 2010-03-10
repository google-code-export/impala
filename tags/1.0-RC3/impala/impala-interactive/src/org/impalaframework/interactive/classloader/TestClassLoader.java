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

package org.impalaframework.interactive.classloader;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.classloader.BaseURLClassLoader;

public class TestClassLoader extends BaseURLClassLoader {

    private static final Log logger = LogFactory.getLog(TestClassLoader.class);

    private String testClassName;

    public TestClassLoader(File[] locations, String testClassName) {
        super(locations);
        this.testClassName = testClassName;
    }

    public TestClassLoader(ClassLoader parent, File[] locations, String testClassName) {
        super(parent, locations);
        this.testClassName = testClassName;
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {

        Class<?> toReturn = null;

        if (!className.contains(testClassName)) {
            toReturn = loadParentClass(className);
            if (toReturn == null) {
                toReturn = getAlreadyLoadedClass(className);
            }
        }

        if (toReturn == null) {
            toReturn = loadCustomClass(className);
        }

        if (toReturn == null) {
            if (logger.isDebugEnabled())
                logger.debug("Class not found: " + className);
            throw new ClassNotFoundException(className);
        }

        // System.out.println("TestClassLoader: " + className + " loaded by " +
        // toReturn.getClassLoader());
        return toReturn;
        
    }

}
