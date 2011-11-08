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

    public ClassLoaderOptions(
            boolean parentLoaderFirst,
            boolean supportsModuleLibraries, 
            boolean exportsModuleLibraries) {
        super();
        this.parentLoaderFirst = parentLoaderFirst;
        this.supportsModuleLibraries = supportsModuleLibraries;
        this.exportsModuleLibraries = exportsModuleLibraries;
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

    @Override
    public String toString() {
        return "ClassLoaderOptions [parentLoaderFirst=" + parentLoaderFirst
                + ", supportsModuleLibraries=" + supportsModuleLibraries
                + ", exportsModuleLibraries=" + exportsModuleLibraries + "]";
    }

}
