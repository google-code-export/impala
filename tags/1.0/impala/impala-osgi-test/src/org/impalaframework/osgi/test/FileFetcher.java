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

package org.impalaframework.osgi.test;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Returns a list of files references which contain all files in the specified subfolders of the root directory
 * @author Phil Zoio
 */
public class FileFetcher {
    
    private String repositoryRootDirectory;
    private String[] repositoryFolders;

    /**
     * @param repositoryRootDirectory the root directory
     * @param repositoryFolders folder in which to find file references
     */
    public FileFetcher(String repositoryRootDirectory, String[] repositoryFolders) {
        super();
        Assert.notNull(repositoryRootDirectory);
        Assert.notNull(repositoryFolders);
        
        this.repositoryRootDirectory = repositoryRootDirectory;
        this.repositoryFolders = repositoryFolders;
    }

    public List<Resource> getResources(FileFilter filter) {
        
        List<Resource> resources = new ArrayList<Resource>();
        
        for (String folder : this.repositoryFolders) {
            String directory = PathUtils.getPath(repositoryRootDirectory, folder);
            File fileFolder = new File(directory);
            File[] files = fileFolder.listFiles(filter);
            
            for (File file : files) {
                resources.add(new FileSystemResource(file));
            }
        }
        
        return resources;
    }
    
}

