package org.impalaframework.build.ant;

/**
 * Value object which holds the Maven organisation, artifact and version for an
 * artifact instance
 * 
 * @author Phil Zoio
 */
public class ArtifactDescription {

    private Boolean hasSource;
    
    private Boolean hasJavaDoc;

    private String organisation;

    private String artifact;

    private String version;

    String getArtifact() {
        return artifact;
    }

    String getOrganisation() {
        return organisation;
    }

    String getVersion() {
        return version;
    }

    void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    void setVersion(String version) {
        this.version = version;
    }

    Boolean isHasSource() {
        return hasSource;
    }

    void setHasSource(Boolean hasSource) {
        this.hasSource = hasSource;
    }
    
    Boolean getHasJavaDoc() {
        return hasJavaDoc;
    }
    
    void setHasJavaDoc(Boolean hasJavaDoc) {
        this.hasJavaDoc = hasJavaDoc;
    }

    @Override
    public String toString() {
        return "ArtifactDescription [organisation=" + organisation
                + ", artifact=" + artifact + ", version=" + version
                + ", hasSource=" + hasSource + ", hasJavaDoc=" + hasJavaDoc
                + "]";
    }
}
