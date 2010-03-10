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

package org.impalaframework.file.monitor;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class FileMonitorImplTest extends TestCase {

    public void testLastModifiedFile() {
        File file = new File("../files/ExternalClass.class");
        try {
            new FileMonitorImpl().lastModified(file);
        }
        catch (IllegalArgumentException e) {
        }
    }

    public void testLastModifiedDirectory() throws IOException, InterruptedException {
        File file = null;
        File sub1 = null;
        File f1 = null;
        File sub2 = null;
        File f2 = null;
        File f3 = null;

        try {
            file = new File("files");
            sub1 = newSubdirectory(file, "temp1");

            f1 = new File(sub1, "temp1.txt");
            f1.createNewFile();

            write(f1, "sometext");
            sub2 = newSubdirectory(sub1, "temp2");
            f2 = new File(sub2, "temp2.txt");
            f3 = new File(sub2, "temp3.txt");

            f2.createNewFile();
            write(f2, "sometext");
            write(f3, "sometext");

            Thread.sleep(1500);
            f2 = new File(sub2, "temp2.txt");
            write(f2, "alteredtext");

            long f1time = f1.lastModified();
            long f2time = f2.lastModified();
            long f3time = f3.lastModified();

            FileFilter fileFilter = new FileFilter() {
                public boolean accept(File pathname) {
                    return true;
                }
            };
            FileMonitorImpl fileMonitorImpl = new FileMonitorImpl();
            fileMonitorImpl.setFileFilter(fileFilter);
            long lastModified = fileMonitorImpl.lastModified(file);
            long lastModifiedMany = fileMonitorImpl.lastModified(new File[]{file});
            
            System.out.println("f1time: " + f1time);
            System.out.println("f2time: " + f2time);
            System.out.println("f3time: " + f3time);
            System.out.println("Last modified: " + lastModified);
            assertEquals(lastModified, lastModifiedMany);

            // assumes that it takes less than 10 seconds to figure out
            assertEquals(f2time, lastModified);

        }
        finally {
            f1.delete();
            f2.delete();
            f3.delete();
            sub2.delete();
            sub1.delete();
        }
    }

    private void write(File f2, String toWrite) throws IOException, FileNotFoundException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f2);
            new DataOutputStream(fos).writeBytes(toWrite);
        }
        finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    private File newSubdirectory(File dir, String dirName) {
        File child = new File(dir, dirName);
        child.mkdirs();
        return child;
    }

}
