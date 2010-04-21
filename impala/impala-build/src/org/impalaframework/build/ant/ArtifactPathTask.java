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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Generates paths for jar, sources-jar and pom files based on Maven artifact info.
 * Sets up following properties:
 * <ul>
 * <li>for jar, uses property
 * <li>for sources jar, uses property.sources
 * <li>for pom, uses property.pom
 * </ul>
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

        getProject().setProperty(property, getPropertyValue(null, "jar"));
        getProject().setProperty(property + ".pom", getPropertyValue(null, "pom"));
        getProject().setProperty(property + ".sources", getPropertyValue("sources", "jar"));
        getProject().setProperty(property + ".javadoc", getPropertyValue("javadoc", "jar"));
    }

    private String getPropertyValue(String classifier, String packaging) {

        String organisationPart = organisation.replace('.', '/');
        return organisationPart + "/" + artifact + "/" + version + "/"
                + artifact + "-" + version + (classifier != null ? "-" + classifier : "")
                + "." + packaging;
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
