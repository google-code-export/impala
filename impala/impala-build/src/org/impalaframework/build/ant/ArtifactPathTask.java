/*
 * Copyright 2007-2008 the original author or authors.
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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Copies created artifacts from dist directory to Maven publish directory. Also
 * generates simple minimal POMs for each artifact.
 * 
 * @author Phil Zoio
 */
public class ArtifactPathTask extends Task {

    private String version;

    private String artifact;

    private String organisation;

    private String property;

    @Override
    public void execute() throws BuildException {

        checkArgs();

        getProject().setProperty(property, getPropertyValue(false, "jar"));
        getProject().setProperty(property + ".pom",
                getPropertyValue(false, "pom"));
        getProject().setProperty(property + ".sources",
                getPropertyValue(true, "jar"));
    }

    private String getPropertyValue(boolean source, String packaging) {

        String organisationPart = organisation.replace('.', '/');
        return organisationPart + "/" + artifact + "/" + version + "/"
                + artifact + "-" + version + (source ? "-sources." : ".")
                + packaging;
    }

    void checkArgs() {

        if (organisation == null) {
            throw new BuildException("'organisation' cannot be null",
                    getLocation());
        }

        if (artifact == null) {
            throw new BuildException("'artifact' cannot be null", getLocation());
        }

        if (version == null) {
            throw new BuildException("'version' cannot be null", getLocation());
        }

        if (property == null) {
            throw new BuildException("'property' cannot be null", getLocation());
        }
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
