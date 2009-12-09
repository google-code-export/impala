package org.impalaframework.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which copies modules from a pre-configured modules directory to
 * WEB-INF/modules when building a WAR file. To be used with "war" packaging.
 * 
 * @goal copy-modules
 * 
 * @phase process-resources
 * @requiresDependencyResolution
 * @requiresProject
 */
public class CopyModulesMojo extends AbstractMojo {

    /** @parameter default-value="${project}" */
    private org.apache.maven.project.MavenProject project;

    /**
     * Location of the file.
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Location of the file.
     * @parameter expression="${project.artifacts}"
     * @required
     */
    private Set<Artifact> dependencies;

    /**
     * Location of the file.
     * @parameter expression="${keep.staging.directory}" default-value = "false"
     * @required
     */
    private boolean keepStagingDirectory;

    /**
     * Location of the file.
     * @parameter expression="${module.staging.directory}"
     * @required
     */
    private String moduleStagingDirectory;

    @SuppressWarnings("unchecked")
    public void showArtifacts() {
        Set<Artifact> artifacts = project.getDependencyArtifacts();

        for (Artifact artifact : artifacts) {
            final String path = artifact.getFile().getPath();
            System.out.println(path);
        }
    }

    public void execute() throws MojoExecutionException {

        System.out.println("Maven projects: " + dependencies);
        System.out.println("Current project: " + project);

        File f = outputDirectory;

        if (!f.exists()) {
            f.mkdirs();
        }

        System.out.println("Keep staging directory: " + keepStagingDirectory);

        File targetDirectory = new File(f.getAbsolutePath() + "/"
                + project.getBuild().getFinalName() + "/WEB-INF/modules");
        File stagingDirectory = new File(moduleStagingDirectory);

        try {
            System.out.println(stagingDirectory.getCanonicalPath());
            FileUtils.forceMkdir(targetDirectory);
        }
        catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        final File[] listFiles = stagingDirectory.listFiles();
        for (File moduleFile : listFiles) {

            final String targetFileName = moduleFile.getName();
            
            MojoUtils.copyFile(moduleFile, targetDirectory, targetFileName);
        }
    }
}
