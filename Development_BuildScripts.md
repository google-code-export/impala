## Building and unzipping to home ##

```
cd impala
ant tohome
```

Not to apply these to petclinic sample

```
cd petclinic
ant fetch
```

## Building snapshot ##

Used to build a snapshot of the repository. Will update the svn.revision property in version.properties with the current snapshot number, and create an impala-1.0-SNAPSHOT.zip file.

```
ant -f release-build.xml snapshot
```

## Building release ##

This creates a new release (not a snapshot), but uses the value in the next.release.version property of release-build.properties. Also tags the repository with the current release.

Only run this as part of a real release.

```
ant release
```

## Bootstrapping build ##

If you run `ant build` from a newly checked out project or after running `ant clean`, the build may fail with the following message:

```
BUILD FAILED
c:\workspaces\impala\impala\shared-build.xml:35: The following error occurred while executing this line:
c:\workspaces\impala\impala-core\build.xml:6: The following error occurred while executing this line:
c:\workspaces\impala\impala\project-build.xml:11: The following error occurred while executing this line:
c:\workspaces\impala\impala\tasks-build.xml:11: java.io.FileNotFoundException: JAR entry org/impalaframew
askdef.properties not found in c:\workspaces\impala\impala-repository\dist\impala-build-1.0-SNAPSHOT.jar
```

To get around this problem, you can bootstrap the build by running:

```
ant build
```

This will create the necessary ANT custom tasks which are used by the rest of the build processes.