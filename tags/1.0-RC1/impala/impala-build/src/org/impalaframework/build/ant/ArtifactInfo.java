package org.impalaframework.build.ant;

class ArtifactInfo extends ArtifactDescription {
    
    private String extraInfo;

    private String targetSubdirectory;

    String getTargetSubdirectory() {
        return targetSubdirectory;
    }

    String getExtraInfo() {
        return extraInfo;
    }

    void setExtraInfo(String type) {
        this.extraInfo = type;
    }

    void setTargetSubdirectory(String targetSubdirectory) {
        this.targetSubdirectory = targetSubdirectory;
    }

}
