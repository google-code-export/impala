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

package org.impalaframework.file;

import java.io.File;
import java.io.FileFilter;

import org.springframework.util.Assert;

/**
 * {@link FileFilter} implementation which matches files with the given extension
 * @author Phil Zoio
 */
public class ExtensionFileFilter implements FileFilter {

    private final String extension;

    public ExtensionFileFilter(String extension) {
        super();
        Assert.notNull(extension, "extension cannot be null");
        this.extension = extension;
    }

    public boolean accept(File file) {
        if (file.getName().endsWith(extension)) {
            return true;
        }
        return false;
    }
}
