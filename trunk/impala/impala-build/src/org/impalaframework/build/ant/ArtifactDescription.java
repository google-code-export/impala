package org.impalaframework.build.ant;

/**
 * Value object which holds the Maven organisation, artifact and version for an
 * artifact instance
 * 
 * @author Phil Zoio
 */
public class ArtifactDescription {

    private Boolean hasSource;

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

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(500);
        buffer.append(this.getClass().getName()).append(": ");
        buffer.append("hasSource = ");
        if ( this.hasSource!= null )
        buffer.append(this.hasSource.toString());
        else buffer.append("value is null"); 
        buffer.append(", ");
        buffer.append("organisation = ");
        buffer.append(this.organisation);
        buffer.append(", ");
        buffer.append("artifact = ");
        buffer.append(this.artifact);
        buffer.append(", ");
        buffer.append("version = ");
        buffer.append(this.version);
        buffer.append("\n");
        return  buffer.toString();
    }
}
