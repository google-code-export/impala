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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Get;

/**
 * Used to perform downloads of individually identified files
 * @author Phil Zoio
 */
public class GetTask extends Task {

    private File dependencies;

    private String baseSourceUrls;

    private File toDir;

    private boolean downloadSources;

    private boolean failOnError;

    private DownloadGetTask get;
    
    private Copy copy;

    private List<Result> results = new ArrayList<Result>();

    @Override
    public void execute() throws BuildException {

        if (dependencies == null) {
            throw new BuildException(
                    "'dependencies' cannot be null. It should refer to a File containing a list of dependencies",
                    getLocation());
        }

        // check dependencies exists and is a file
        if (!dependencies.exists()) {
            if (failOnError)
                throw new BuildException("The location refered to by 'dependencies' does not exist", getLocation());
            else {
                log("Dependencies file " + dependencies + " does not exist", Project.MSG_INFO);
                return;
            }
        }

        if (!dependencies.isFile()) {
            if (failOnError)
                throw new BuildException("The location refered to by 'dependencies' is not a file", getLocation());
        }

        if (toDir == null) {
            throw new BuildException(
                    "'toDir' cannot be null. It should refer to a File pointing to download target location",
                    getLocation());
        }

        if (baseSourceUrls == null) {
            throw new BuildException(
                    "'baseSourceUrls' cannot be null. It should point to the base location from where artifacts are downloaded",
                    getLocation());
        }

        // check toDir exists and is a directory
        if (!toDir.exists()) {
            throw new BuildException("The location refered to by 'toDir' does not exist", getLocation());
        }

        if (!toDir.isDirectory()) {
            throw new BuildException("The location refered to by 'toDir' is not a directory", getLocation());
        }

        get = new DownloadGetTask();
        get.setProject(getProject());
        get.setIgnoreErrors(false);

        copy = new Copy();
        copy.setProject(getProject());

        List<String> fileList = getFileList();

        String[] sourceUrls = baseSourceUrls.split(",");

        log("Using following locations to retrieve resources: ");
        log("-------------------------------------------------");
        for (int i = 0; i < sourceUrls.length; i++) {
            sourceUrls[i] = sourceUrls[i].trim();
            if (!sourceUrls[i].endsWith("/")) {
                sourceUrls[i] = sourceUrls[i] + "/";
            }
            log(sourceUrls[i]);
        }
        log("-------------------------------------------------");
        
        for (String file : fileList) {

            List<DownloadInfo> di = getDownloadInfos(file);

            for (DownloadInfo info : di) {
                doDownload(sourceUrls, info.getUrlString(), info.getFile());
            }
        }

        printResults();

    }

    protected List<DownloadInfo> getDownloadInfos(String urlString) {
        List<DownloadInfo> di = new LinkedList<DownloadInfo>();

        final String url = urlString;

        String fileName = url;

        int lastSlash = url.lastIndexOf("/");

        if (lastSlash > 0) {
            fileName = url.substring(lastSlash + 1);
        }

        DownloadInfo downloadInfo = new DownloadInfo(url, new File(toDir, fileName));

        di.add(downloadInfo);

        if (downloadSources) {
            int lastDot = url.lastIndexOf(".");

            if (lastDot > 0 && lastDot > lastSlash) {
                String sourceUrl = url.substring(0, lastDot) + "-sources" + url.substring(lastDot);

                int lastFileDot = fileName.lastIndexOf(".");
                String sourceFileName = fileName.substring(0, lastFileDot) + "-sources"
                        + fileName.substring(lastFileDot);

                di.add(new DownloadInfo(sourceUrl, new File(toDir, sourceFileName)));
            }
        }
        return di;
    }

    private void doDownload(String[] sourceUrls, String url, File toFile) {

        log("Retrieving new resource if available for " + url);
        toFile.getParentFile().mkdirs();

        Boolean downloaded = null;
        
        for (int i = 0; i < sourceUrls.length; i++) {
            try {
                URL srcUrl = new URL(sourceUrls[i] + url);

                if ("file".equals(srcUrl.getProtocol())) {
                    copy.init();
                    copy.setFile(new File(srcUrl.getFile()));
                    copy.setTofile(toFile);
                    copy.setPreserveLastModified(true);
                    copy.setTaskName("copy");
                    copy.execute();

                    results.add(new Result(url, Result.SUCCEEDED, srcUrl));
                }
                else {
                    get.init();
                    get.setSrc(srcUrl);
                    get.setUseTimestamp(true);
                    get.setDest(toFile);
                    get.setDescription(getDescription());
                    get.setLocation(getLocation());
                    get.setOwningTarget(getOwningTarget());
                    get.setTaskName("get");
                    get.execute();
                    
                    downloaded = get.getDownloaded();
                }
                //result is interpreted as succeeded if null. Otherwise SUCCEEDED if downloaded = true otherwise NOT modified
                
                if (get.getSucceeded()) {

                    final int result = downloaded == null ? Result.SUCCEEDED : (downloaded ? Result.SUCCEEDED : Result.NOT_MODIFIED);
                    results.add(new Result(url, result, srcUrl));
                    
                    return;
                
                }
            }
            catch (MalformedURLException e) {
                log("Unable to form valid url using " + url, Project.MSG_ERR);
            }
            catch (Exception e) {
                log("Unable to download " + url, Project.MSG_DEBUG);
            }
        }               
        

        results.add(new Result(url, Result.FAILED, null));
    }

    private List<String> getFileList() {
        List<String> fileList = new ArrayList<String>();

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(dependencies);
        }
        catch (FileNotFoundException e) {
            throw new BuildException("Could not find file " + dependencies.getAbsolutePath(), getLocation());
        }
        BufferedReader reader = new BufferedReader(fileReader);

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0 && !line.startsWith("#"))
                    fileList.add(line);
            }
        }
        catch (IOException e) {
            throw new BuildException("Could not read file " + dependencies.getAbsolutePath(), getLocation());
        }
        return fileList;
    }

    private void printResults() {
        StringBuffer buffer = new StringBuffer();

        String lineSeparator = System.getProperty("line.separator");
        buffer.append("******************************************************");
        buffer.append(lineSeparator);
        buffer.append(lineSeparator);
        buffer.append("               RESULTS OF DOWNLOAD OPERATION                   ");
        buffer.append(lineSeparator);
        buffer.append(lineSeparator);

        if (results.size() > 0) {
            for (Result result : results) {
                buffer.append(result.toString()).append(lineSeparator);
            }
        }
        else {
            buffer.append("No files to download");
        }

        buffer.append(lineSeparator);
        buffer.append("******************************************************");
        log(buffer.toString(), Project.MSG_INFO);
    }

    public void setBaseSourceUrls(String baseSourceUrl) {
        this.baseSourceUrls = baseSourceUrl;
    }

    public void setDependencies(File dependencies) {
        this.dependencies = dependencies;
    }

    public void setToDir(File toDir) {
        this.toDir = toDir;
    }

    public void setDownloadSources(boolean downloadSources) {
        this.downloadSources = downloadSources;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    /* ************************ protected getters *********************** */

    protected File getDependencies() {
        return dependencies;
    }

    protected boolean isDownloadSources() {
        return downloadSources;
    }

    protected boolean isFailOnError() {
        return failOnError;
    }

    protected File getToDir() {
        return toDir;
    }

}

class DownloadGetTask extends Get {
    private Boolean downloaded;
    private Boolean succeeded;

    @Override
    public void init() throws BuildException {
        super.init();
        this.downloaded = null;
        this.succeeded = null;
    }

    @Override
    public void log(Throwable t, int msgLevel) {
    }

    @Override
    public void log(String msg, int msgLevel) {
    }

    @Override
    public void log(String msg, Throwable t, int msgLevel) {
    }

    @Override
    public void log(String msg) {
    }

    @Override
    public boolean doGet(int logLevel, DownloadProgress progress)
            throws IOException {
        boolean download = false;
        try {
            download = super.doGet(logLevel, progress);
            this.succeeded = true;
        } catch (Exception e) {
            return false;
        }
        this.downloaded = download;
        return download;
    }

    public Boolean getDownloaded() {
        return this.downloaded;
    }

    public Boolean getSucceeded() {
        return succeeded;
    }

}

class Result {

    static final int NOT_MODIFIED = 0;

    static final int FAILED = 1;

    static final int SUCCEEDED = 2;

    public Result(String archive, int result, URL srcUrl) {
        super();
        if (archive == null)
            throw new IllegalArgumentException("archive cannot be null");
        if (NOT_MODIFIED > result || SUCCEEDED < result)
            throw new IllegalArgumentException("result must be between 0 and 2 (inclusive)");
        if (SUCCEEDED == result && srcUrl == null)
            throw new IllegalArgumentException("success location required for successful result");

        this.archive = archive;
        this.result = result;
        this.successLocation = srcUrl;
    }

    private String archive;

    private URL successLocation;

    private int result;

    public String toString() {
        switch (result) {
        case NOT_MODIFIED:
            return archive + " not modified";

        case FAILED:
            return archive + " could not be downloaded from any location";

        case SUCCEEDED:
            return archive + "\nresolved from\n" + successLocation;
        default:
            throw new IllegalStateException("Should not get here");
        }
    }
}
