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

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Goal which makes module available to a pre-configured directory
 * 
 * @goal stage-module
 * 
 * @phase package
 * @requiresDependencyResolution
 * @requiresProject
 */
public class StageModuleMojo extends AbstractMojo {
    
    /** @parameter default-value="${project}" */
    private org.apache.maven.project.MavenProject project;
    
    /**
     * Location of the file.
     * @parameter expression="${module.staging.directory}"
     */
    private String moduleStagingDirectory;
    
    public void execute() throws MojoExecutionException {

        final Log log = getLog();
        
        boolean isImpalaModule = isImpalaModule();
        
        if (isImpalaModule) {

            moduleStagingDirectory = MojoUtils.getModuleStagingDirectory(getLog(), project, moduleStagingDirectory);

            //copying module to staging directory
            
            final File file = project.getArtifact().getFile();
            
            if (log.isInfoEnabled()) {
                log.info("Copying file " + file.getAbsolutePath() + " to module staging directory: " + moduleStagingDirectory);
            }
            
            final File targetDirectory = new File(moduleStagingDirectory);
            try {
                FileUtils.forceMkdir(targetDirectory);
            }
            catch (IOException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
            MojoUtils.copyFile(file, targetDirectory, file.getName());

        }
    }

    boolean isImpalaModule() {
        final boolean isJar = "jar".equals(project.getPackaging());
        
        if (!isJar) {
            getLog().debug("Not supporting " + project.getArtifactId() + " as it does not use 'jar' packaging");
            return false;
        }
        
        final Properties properties = project.getProperties();
        String moduleJarProperty = properties.getProperty("impala.module");
        if (moduleJarProperty != null && moduleJarProperty.length() > 0) {
             final boolean parseBoolean = Boolean.parseBoolean(moduleJarProperty);
             if (!parseBoolean) {
                 getLog().debug("Not supporting " + project.getArtifactId() + " as it has set the 'impala.module' property to false");
                 return false;
             }
             
            return parseBoolean;
        }
        getLog().info("Staging " + project.getArtifactId() + " as an Impala module.");
        return true;
    }
    
    void setProject(org.apache.maven.project.MavenProject project) {
        this.project = project;
    }
}
