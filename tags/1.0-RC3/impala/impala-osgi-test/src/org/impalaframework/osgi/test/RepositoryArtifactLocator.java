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

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.osgi.test.provisioning.ArtifactLocator;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ArtifactLocator} which searches for artifacts in the Impala repository
 * @author Phil Zoio
 */
public class RepositoryArtifactLocator implements ArtifactLocator {
    
    private String repositoryRootDirectory;
    private String[] repositoryFolders;
    
    public RepositoryArtifactLocator(String repositoryRootDirectory, String[] repositoryFolders) {
        super();
        Assert.notNull(repositoryRootDirectory);
        Assert.notNull(repositoryFolders);
        
        this.repositoryRootDirectory = repositoryRootDirectory;
        this.repositoryFolders = repositoryFolders;
    }

    public Resource locateArtifact(String group, String id, String version) {
        return locateArtifact(group, id, version, null);        
    }

    public Resource locateArtifact(String group, String id, String version, String type) {
        
        for (String folder : this.repositoryFolders) {
            String directory = repositoryRootDirectory + "/" + folder + "/";
            Resource resource = findBundleResource(directory, id, version, type);
            
            if (resource.exists()) {
                return resource;
            }
        }

        return null;
    }

    private Resource findBundleResource(String directory, String id, String version, String type) {
        FileSystemResource resource = new FileSystemResource(directory + id + "-" + version 
                + (type != null ? "-" + type : "") + ".jar");
        return resource;
    }
    
}

