package org.impalaframework.maven.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;

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

}
