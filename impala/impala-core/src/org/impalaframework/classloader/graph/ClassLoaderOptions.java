package org.impalaframework.classloader.graph;

/**
 * Encapsulates options for {@link GraphClassLoader}
 * @author Phil Zoio
 */
public class ClassLoaderOptions {
    
    /**
     * If true, then attempt to use parent (system or webapp) class loader before using Impala class loader
     */
    private final boolean parentLoaderFirst;
    
    /**
     * If true, then will attempt to load module-internal libraries (for example, in the modules lib directory, or in WEB-INF/modules/lib/module_name)
     */
    private final boolean supportsModuleLibraries;
    
    /**
     * If true, then will allow module-internal library classes to be visible to dependent modules
     */
    private final boolean exportsModuleLibraries;
    
    /**
     * If true, then will also load resources from module-internal libraries if they are present
     */
    private final boolean loadsModuleLibraryResources;

    public ClassLoaderOptions(
            boolean parentLoaderFirst,
            boolean supportsModuleLibraries, 
            boolean exportsModuleLibraries, boolean loadsModuleLibraryResources) {
        super();
        this.parentLoaderFirst = parentLoaderFirst;
        this.supportsModuleLibraries = supportsModuleLibraries;
        this.exportsModuleLibraries = exportsModuleLibraries;
        this.loadsModuleLibraryResources = loadsModuleLibraryResources;
    }

    public boolean isParentLoaderFirst() {
        return parentLoaderFirst;
    }

    public boolean isSupportsModuleLibraries() {
        return supportsModuleLibraries;
    }

    public boolean isExportsModuleLibraries() {
        return exportsModuleLibraries;
    }
    
    public boolean isLoadsModuleLibraryResources() {
        return loadsModuleLibraryResources;
    }

    @Override
    public String toString() {
        return "ClassLoaderOptions [parentLoaderFirst=" + parentLoaderFirst
                + ", supportsModuleLibraries=" + supportsModuleLibraries
                + ", exportsModuleLibraries=" + exportsModuleLibraries
                + ", loadsModuleLibraryResources="
                + loadsModuleLibraryResources + "]";
    }
}
