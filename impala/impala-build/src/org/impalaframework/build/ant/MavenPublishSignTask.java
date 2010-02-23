package org.impalaframework.build.ant;

import java.io.File;

import org.apache.commons.openpgp.ant.OpenPgpSignerTask;
import org.apache.tools.ant.BuildException;

/**
 * Extens {@link MavenPublishTask} by also signing artefacts
 * 
 * @author Phil Zoio
 */
public class MavenPublishSignTask extends MavenPublishTask {

    private String keyId;

    private File pubring;

    private File secring;

    private String password;
    
    @Override
    public void execute() throws BuildException {
        
        //FIXME check for values
        super.execute();
    }

    @Override
    protected void postProcessArtifacts(File organisationDirectory,
            ArtifactOutput artifactOutput) {

        OpenPgpSignerTask task = new OpenPgpSignerTask();
        task.setProject(getProject());
        task.setLocation(getLocation());
        task.setKeyId(keyId);
        task.setAsciiarmor(true);
        task.setPubring(pubring);
        task.setSecring(secring);
        task.setPassword(password);
        
        sign(task, getTargetFile(organisationDirectory, artifactOutput));
        sign(task, getTargetSourceFile(organisationDirectory, artifactOutput));
        sign(task, getPomFile(organisationDirectory, artifactOutput));
    }

    private void sign(OpenPgpSignerTask task, final File fileToSign) {
        task.setArtefact(fileToSign);
        task.init();
        task.execute();
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public void setPubring(File pubring) {
        this.pubring = pubring;
    }

    public void setSecring(File secring) {
        this.secring = secring;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
