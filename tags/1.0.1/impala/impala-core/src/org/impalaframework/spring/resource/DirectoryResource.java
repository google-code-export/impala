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

package org.impalaframework.spring.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

/**
 * Extension of <code>FileSystemResource</code> which expects the resource
 * concerned to represent a directory on a file system rather than the file
 * itself. Checks that file supplied in constructor is a directory if it exists.
 * @see FileSystemResource
 * @author Phil Zoio
 */
public class DirectoryResource extends FileSystemResource {

    public DirectoryResource(String path) {
        super(path);
        checkIsDirectory();
    }

    public DirectoryResource(File file) {
        super(file);
        checkIsDirectory();
    }

    /**
     * This implementation returns a URL for the underlying file, with the
     * assumption that the underlying file is a directory.
     * @see java.io.File#getAbsolutePath()
     */
    @Override
    public URL getURL() throws IOException {
        File directory = getFile();
        return new URL(ResourceUtils.FILE_URL_PREFIX + directory.getAbsolutePath() + "/");
    }

    private void checkIsDirectory() {
        File dir = getFile();
        if (dir.exists()) {
            Assert.isTrue(dir.isDirectory(), "Supplied file '" + dir.getAbsolutePath() + "' is not a directory");
        }
    }

}
