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

package org.impalaframework.build.ant;

import java.io.File;

import org.apache.commons.openpgp.ant.OpenPgpSignerTask;
import org.apache.tools.ant.BuildException;

/**
 * Extends {@link MavenPublishTask} by also signing artefacts. Uses the
 * commons-openpgp.jar's {@link OpenPgpSignerTask}. The compiled copy was
 * obtained from
 * <a href ="http://makumba.svn.sf.net/viewvc/makumba/trunk/makumba/lib/building/commons-openpgp-1.0-SNAPSHOT.jar?view=log">here</a> Note that the BouncyCastle
 * Provider and PGP API jars need to be installed on the JVM for this to work.
 * See from <a href = "http://www.randombugs.com/java/javalangsecurityexception-jce-authenticate-provider-bc.html">Randombugs blog entry</a><br/>
 * <ul>
 * <li>Find java.security in ${env.JAVA_HOME}/lib/security -
 * <li>Add
 * security.provider.X=org.bouncycastle.jce.provider.BouncyCastleProvider -
 * <li>Add the bcprov-jdk16-145.jar and bcpg-jdk16-145.jar to
 * ${env.JAVA_HOME}/lib/ext
 * </ul>
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

        checkSignTaskArgs();

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

    void sign(OpenPgpSignerTask task, File fileToSign) {
        task.setArtefact(fileToSign);
        task.init();
        task.execute();
    }

    void checkSignTaskArgs() {
        checkNotNull("keyId", keyId);

        checkNotNull("pubring", pubring);
        checkExists("pubring", pubring);

        checkNotNull("secring", secring);
        checkExists("secring", secring);

        checkNotNull("password", password);
    }

    private void checkNotNull(String name, Object property) {
        if (property == null) {
            throw new BuildException("'" + name + "' cannot be null",
                    getLocation());
        }
    }

    private void checkExists(String name, File file) {
        if (!file.exists()) {
            throw new BuildException("'" + name + "' file "
                    + file.getAbsolutePath() + " does not exist", getLocation());
        }
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
