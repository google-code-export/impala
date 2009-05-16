package org.impalaframework.build.ant;

import java.io.File;
import java.io.FileFilter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Echo;

/**
 * Copies created artifacts from dist directory to Maven publish directory. Also
 * generates simple minimal POMs for each artifact.
 * 
 * @author Phil Zoio
 */
public class MavenPublishTask extends Task {
    
    private File sourceDir;
    
    private File destDir;
    
    private String artifacts;
    
    private String organisation;
    
    @Override
    public void execute() throws BuildException {

        checkArgs();
                
        //determine list of artifacts by parsing artifacts String
        //read source directory to get all candidate resources
        //for each, determine whether in artifact list
        final File[] files = getFiles();
        ArtifactOutput[] ads = getArtifactOutput(files);

        //parse the version information, and copy to the organisation specific folder
        //generate pom for each of test
        copyArtifacts(ads);
    }

    private void copyArtifacts(ArtifactOutput[] ads) {
        
        Copy copy = new Copy();
        copy.setProject(getProject());
        copy.setPreserveLastModified(true);
        
        Echo echo = new Echo();
        echo.setProject(getProject());
        
        final File organisationDirectory = getOrganisationDirectory();
        for (ArtifactOutput artifactOutput : ads) {

            File targetFile = artifactOutput.getOutputLocation(organisationDirectory, false);
            copy.setFile(artifactOutput.getSrcFile());
            copy.setTofile(targetFile);
            copy.execute();
            copy.init();
            
            if (artifactOutput.getSourceSrcFile() != null) {
                File targetSourceFile = artifactOutput.getOutputLocation(organisationDirectory, true);
                copy.setFile(artifactOutput.getSourceSrcFile());
                copy.setTofile(targetSourceFile);
                copy.execute();
                copy.init();
            }
            
            String pomText = "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" + 
              "<modelVersion>4.0.0</modelVersion>\n" + 
              "<groupId>" + artifactOutput.getOrganisation() + 
              "</groupId>\n" + 
              "<artifactId>" + artifactOutput.getArtifact() +
              "</artifactId>\n" + 
              "<version>" + artifactOutput.getVersion() +
              "</version>\n" + 
              "</project>";

            File pomFile = artifactOutput.getOutputLocation(organisationDirectory, ".pom");
            
            echo.setFile(pomFile);
            echo.addText(pomText);
            
            echo.execute();
        }
        
    }

    File getOrganisationDirectory() {
        String organisationSplit = organisation.replace(".", "/");
        File organisationDirectory = new File(destDir, organisationSplit);
        return organisationDirectory;
    }

    ArtifactOutput[] getArtifactOutput(File[] files) {

        ArtifactOutput[] ads = new ArtifactOutput[files.length];
        for (int i = 0; i < files.length; i++) {
            
            final ArtifactOutput artifactDescription = new ArtifactOutput();
            artifactDescription.setSrcFile(files[i]);
            String fileName = files[i].getName();
            String fileNameWithoutJar = fileName.substring(0, fileName.indexOf(".jar"));
            int lastDashIndex = fileNameWithoutJar.lastIndexOf("-");

            String version = fileNameWithoutJar.substring(lastDashIndex+1);
            
            //special case for SNAPSHOT
            if ("SNAPSHOT".equals(version)) {
                lastDashIndex = fileNameWithoutJar.substring(0, lastDashIndex).lastIndexOf("-");
                version = fileNameWithoutJar.substring(lastDashIndex+1);
            }
            
            String artifact = fileNameWithoutJar.substring(0, lastDashIndex);
            artifactDescription.setArtifact(artifact);
            artifactDescription.setOrganisation(organisation);
            artifactDescription.setVersion(version);
            
            File parent = files[i].getParentFile();
            String sourceFileName = fileName.replace(".jar", "-sources.jar");
            File sourceFile = new File(parent, sourceFileName);
            if (sourceFile.exists()) {
                artifactDescription.setHasSource(true);
                artifactDescription.setSourceSrcFile(sourceFile);
            }
            
            ads[i] = artifactDescription;
        }
        return ads;
    }

    File[] getFiles() {
        
        final String[] artifactList = artifacts.split(",");
        
        final File[] files = sourceDir.listFiles(new FileFilter() {

            public boolean accept(File file) {
                final String fileName = file.getName();
                if (fileName.contains("sources")) {
                    return false;
                } 
                if (file.isDirectory()) {
                    return false;
                }
                
                if (!fileName.endsWith(".jar")) {
                    return false;
                }
                
                for (String artifact : artifactList) {
                    if (fileName.startsWith(artifact.trim())) {
                        return true;
                    }
                }
                return false;
            }
            
        });
        
        return files;
    }

    void checkArgs() {
        
        if (sourceDir == null) {
            throw new BuildException("'sourceDir' cannot be null", getLocation());
        }
        
        if (artifacts == null) {
            throw new BuildException("'artifacts' cannot be null", getLocation());
        }       
        
        if (organisation == null) {
            throw new BuildException("'organisation' cannot be null", getLocation());
        }
        
        if (destDir == null) {
            throw new BuildException("'destDir' cannot be null", getLocation());
        }
        
        if (!sourceDir.exists()) {
            throw new BuildException("The source directory '" + sourceDir + "' does not exist", getLocation());
        }
        
        if (!sourceDir.exists()) {
            throw new BuildException("The source directory '" + sourceDir + "' does not exist", getLocation());
        }
        
        if (!sourceDir.isDirectory()) {
            throw new BuildException("'sourceDir' is not a directory", getLocation());
        }
    }

    public void setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
    }

    public void setArtifacts(String artifacts) {
        this.artifacts = artifacts;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }


}
