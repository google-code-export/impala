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
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Ant;

/**
 * @author Phil Zoio
 */
public class AntForeachTask extends Ant {

    private File dir;

    private String projects;

    public void setDir(File dir) {
        this.dir = dir;
    }

    @Override
    public void execute() throws BuildException {

        checkArgs();

        List<File> subdirectories = getSubdirectories();
        
        //now execute task
        for (File file : subdirectories) {
            init();
            super.setDir(file);
            super.execute();
        }
    }

    List<File> getSubdirectories() {
        String[] valueList = projects.split(",");
        
        List<File> subdirectories = new ArrayList<File>();

        for (String directory : valueList) {
            directory = directory.trim();
            if (directory.length() > 0) {
                File subdir = new File(dir, directory);
                if (!subdir.exists()) {
                    throw new BuildException("Subdirectory for ANT task does not exist: " + subdir, getLocation());
                }
                
                if (!subdir.exists()) {
                    throw new BuildException("Subdirectory for ANT task does not exist: " + subdir, getLocation());
                }
                
                subdirectories.add(subdir);
            }
        }
        return subdirectories;
    }

    void checkArgs() {
        if (projects == null) {
            throw new BuildException("'values' property not specified", getLocation());
        }

        if (dir == null) {
            throw new BuildException("'dir' property not specified", getLocation());
        }
        
        if (!dir.exists()) {
            throw new BuildException("'dir' does not exist", getLocation());
        }
        
        if (!dir.isDirectory()) {
            throw new BuildException("'dir' must be a directory", getLocation());
        }
    }

    public void setProjects(String values) {
        this.projects = values;
    }

}
