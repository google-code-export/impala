package org.impalaframework.maven.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
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

    static String getModuleStagingDirectory(MavenProject project, String moduleStagingDirectory) throws MojoExecutionException {
        
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
        
        System.out.println("Using module staging directory: " + moduleStagingDirectory);
        
        return moduleStagingDirectory;
    }

}
