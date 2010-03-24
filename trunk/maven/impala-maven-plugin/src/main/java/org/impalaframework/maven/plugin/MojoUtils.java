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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public class MojoUtils {

    public static void copyFile(File sourceFile, File targetDirectory,
            final String targetFileName) throws MojoExecutionException {
        
        InputStream input = null;
        OutputStream output = null; 
        
        try {
            input = new FileInputStream(sourceFile);
            output = new FileOutputStream(new File(targetDirectory, targetFileName));
            IOUtils.copy(input, output);
        }
        catch (IOException e) {
            throw new MojoExecutionException("Error creating file ", e);
        }
        finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

    static String getModuleStagingDirectory(Log log, MavenProject project, String moduleStagingDirectory) throws MojoExecutionException {
        
        //FIXME test
        
        String parentName = null;
        
        if (moduleStagingDirectory == null) {
            MavenProject parent = project.getParent();
            if (parent != null) {
                parentName = parent.getName();
                final String parentOutputDirectory = parent.getBuild().getDirectory();
                if (parentOutputDirectory != null) {
                    moduleStagingDirectory = parentOutputDirectory + "/staging";
                }
            }
        }
        
        if (moduleStagingDirectory == null) {
            throw new MojoExecutionException("Unable to determine module staging directory for project '" + project.getName() + "'" + 
            		(parentName != null ? " from project parent '" + parentName +	"'" : " with no project parent") +
            		". Please use 'moduleStagingDirectory' configuration parameter to specify this.");
        }
        
        log.info("Using module staging directory: " + moduleStagingDirectory);
        
        return moduleStagingDirectory;
    }

    public static boolean checkConditionFromPropertyAndPackaging(
            MavenProject project, 
            String propertyName, 
            String packagingName, 
            Log log) {
        
        final Properties properties = project.getProperties();
        String moduleJarProperty = properties.getProperty("impala.module");
        
        if (moduleJarProperty != null && moduleJarProperty.length() > 0) {
             final boolean parseBoolean = Boolean.parseBoolean(moduleJarProperty);
             if (!parseBoolean) {
                 log.debug("Not supporting " + project.getArtifactId() + " as it has set the '" + propertyName + "' property to false");
                 return false;
             }
             
            return parseBoolean;
        }
        
        final boolean isJar = packagingName.equals(project.getPackaging());
        
        if (!isJar) {
            log.debug("Not supporting " + project.getArtifactId() + " as it does not use '" + packagingName + "' packaging");
            return false;
        }
        
        return true;
    }

}
