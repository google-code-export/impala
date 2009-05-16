package org.impalaframework.build.ant;

import java.io.File;

public class ArtifactOutput extends ArtifactDescription {

    private File srcFile;
    
    private File sourceSrcFile;

    public File getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(File outputFile) {
        this.srcFile = outputFile;
    }

    public File getSourceSrcFile() {
        return sourceSrcFile;
    }

    public void setSourceSrcFile(File sourceOutputFile) {
        this.sourceSrcFile = sourceOutputFile;
    }
    
    public File getOutputLocation(File organisationDirectory, boolean sources) {
        return getOutputLocation(organisationDirectory, ".jar", sources);
    }
    
    public File getOutputLocation(File organisationDirectory, String extension) {
        return getOutputLocation(organisationDirectory, extension, false);
    }
    
    private File getOutputLocation(File organisationDirectory, String extension, boolean sources) {
        final String outputLocation = this.getArtifact() + "/" 
            + this.getVersion() + "/" 
            + this.getArtifact() 
            + "-" 
            + this.getVersion() 
            + (sources ? "-sources" : "")
            + extension;
        return new File(organisationDirectory, outputLocation);
    }
    
}
