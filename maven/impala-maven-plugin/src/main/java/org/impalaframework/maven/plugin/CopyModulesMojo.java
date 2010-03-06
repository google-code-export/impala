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

package org.impalaframework.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Goal which copies modules from a pre-configured modules directory to
 * WEB-INF/modules when building a WAR file. To be used with "war" packaging.
 * 
 * @goal copy-modules
 * 
 * @phase process-resources
 * @requiresDependencyResolution
 * @requiresProject
 */
public class CopyModulesMojo extends AbstractMojo {

    /** @parameter default-value="${project}" */
    private org.apache.maven.project.MavenProject project;

    /**
     * Location of the file.
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Location of the file.
     * @parameter expression="${project.artifacts}"
     * @required
     */
    private Set<Artifact> dependencies;

    /**
     * Location of the file.
     * @parameter expression="${module.staging.directory}"
     */
    private String moduleStagingDirectory;

    public void execute() throws MojoExecutionException {

        final Log logger = getLog();
        
        if (project.getPackaging().equals("war")) {
            
            moduleStagingDirectory = MojoUtils.getModuleStagingDirectory(project, moduleStagingDirectory);
    
            if (logger.isDebugEnabled()) {
                logger.debug("Maven projects: " + dependencies);
                logger.debug("Current project: " + project);
            }
            
            File targetDirectory = getTargetDirectory();
            File stagingDirectory = new File(moduleStagingDirectory);
    
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Staging directory " + stagingDirectory.getCanonicalPath());
                }
                FileUtils.forceMkdir(targetDirectory);
            }
            catch (IOException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
            
            if (logger.isInfoEnabled()) {
                logger.info("Copying files from staging directory: " + stagingDirectory);
            }
    
            final File[] listFiles = stagingDirectory.listFiles();            
            
            if (listFiles != null) {
                for (File moduleFile : listFiles) {
        
                    final String targetFileName = moduleFile.getName();
                    
                    MojoUtils.copyFile(moduleFile, targetDirectory, targetFileName);
                    if (logger.isInfoEnabled()) {
                        logger.info("Copying from from staging directory: " + moduleFile);
                    }
                }
            }
        }
    }

    File getTargetDirectory() {
        return new File(outputDirectory.getAbsolutePath() + "/"  + project.getBuild().getFinalName() + "/WEB-INF/modules");
    }
}
