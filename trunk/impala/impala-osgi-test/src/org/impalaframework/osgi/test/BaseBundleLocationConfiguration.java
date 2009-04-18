package org.impalaframework.osgi.test;

import org.springframework.core.io.Resource;
import org.springframework.osgi.test.provisioning.ArtifactLocator;

/**
 * Base implementation of {@link BundleLocationConfiguration} with simple set abstract methods
 * subclasses should implement
 * @author Phil Zoio
 */
public abstract class BaseBundleLocationConfiguration implements BundleLocationConfiguration {

    public ArtifactLocator getArtifactLocator() {
        String repositoryRootDirectory = getRepositoryRootDirectory();
        String[] artifactLocatorFolders = getArtifactLocatorFolders();
        return new RepositoryArtifactLocator(repositoryRootDirectory, artifactLocatorFolders);
    }
    
    public Resource[] getTestBundleLocations() {
        String testBundleIncludes = getTestBundleIncludes();
        String testBundleExcludes = getTestBundleExcludes();
        String[] testBundleFolders = getTestBundleFolders();
        
        ConfigurableFileFilter fileFilter = new ConfigurableFileFilter(
                testBundleIncludes,
                testBundleExcludes);
        FileFetcher fileFetcher = new FileFetcher(getRepositoryRootDirectory(), testBundleFolders);
        return fileFetcher.getResources(fileFilter).toArray(new Resource[0]);
    }

    
    public Resource[] getExtenderBundleLocations() {
        String extenderBundleIncludes = getExtenderBundleIncludes();
        String extenderBundleExcludes = getExtenderBundleExcludes();
        String[] extenderBundleFolders = getExtenderBundleFolders();
        
        ConfigurableFileFilter fileFilter = new ConfigurableFileFilter(
                extenderBundleIncludes, 
                extenderBundleExcludes); 
        FileFetcher fileFetcher = new FileFetcher(getRepositoryRootDirectory(), extenderBundleFolders);
        return fileFetcher.getResources(fileFilter).toArray(new Resource[0]);
    }

    protected abstract String[] getArtifactLocatorFolders();
    
    protected abstract String[] getTestBundleFolders();

    protected abstract String getTestBundleExcludes();
    
    protected abstract String getTestBundleIncludes();

    protected abstract String[] getExtenderBundleFolders();

    protected abstract String getExtenderBundleExcludes();

    protected abstract String getExtenderBundleIncludes();

    protected abstract String getRepositoryRootDirectory();
    
}
