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

package org.impalaframework.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.impalaframework.exception.ExecutionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

/**
 * @author Phil Zoio
 */
public class ResourceUtils {

    public static Resource[] toArray(List<Resource> list) {
        Assert.notNull(list);
        return list.toArray(new Resource[list.size()]);
    }
    
    public static Resource[] getResources(File[] files) {
        Assert.notNull(files, "files cannot be null");
        Resource[] resources = new Resource[files.length];

        for (int i = 0; i < files.length; i++) {
            resources[i] = new FileSystemResource(files[i]);
        }
        return resources;
    }

    public static File[] getFiles(List<Resource> resources) {
        return getFiles(toArray(resources));
    }
    
    public static File[] getFiles(Resource[] resources) {
        Assert.notNull(resources, "resources cannot be null");
        File[] files = new File[resources.length];

        for (int i = 0; i < files.length; i++) {
            try {
                files[i] = resources[i].getFile();
            }
            catch (IOException e) {
                throw new ExecutionException("Unable to convert " + resources[i].getDescription() + " into a File", e);
            }
        }
        return files;
    }

    public static Resource[] getClassPathResources(List<String> locations, ClassLoader classLoader) {
        Resource[] resources = new Resource[locations.size()];
    
        for (int i = 0; i < locations.size(); i++) {
            // note that this is relying on the contextClassLoader to be set up
            // correctly
            resources[i] = new ClassPathResource(locations.get(i), classLoader);
        }
        return resources;
    }
    
    public static Reader getReaderForResource(Resource resource) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(resource.getInputStream(), "UTF8");
        }
        catch (Exception e) {
            throw new ExecutionException("Unable to read resource " + resource.getDescription(), e);
        }
        return reader;
    }

    public static String readText(Resource resource) {
        Reader reader = null;
        try {
            reader = getReaderForResource(resource);
            return FileCopyUtils.copyToString(reader);
        }
        catch (Exception e) {
            throw new ExecutionException("Unable to read resource " + resource.getDescription(), e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                }
            }
        }
    }
    
}
