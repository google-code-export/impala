# Download and Setup #

This introduction is an abridged version of the GettingStarted series. For a more detailed introduction, see [this link](GettingStarted.md).

## Setup Steps ##

First, start by downloading the latest snapshot distribution of Impala from the subversion repository.

```
http://impala.googlecode.com/svn/trunk/impala/impala/dist/impala-1.0.zip
```

or the relevant distribution from [the Impala downloads page](http://code.google.com/p/impala/downloads/list).

You can then unzip the Impala distribution:

```
unzip impala-1.0.zip
```

Ensure that you have the _IMPALA\_HOME_ environment property set correctly to the unzipped file location.
Run the following command from the _IMPALA\_HOME_ directory:

On Windows:
```
cd %IMPALA_HOME%
```
and on Unix
```
cd $IMPALA_HOME
```
and then run
```
ant newproject
```

Follow the interactive steps to determine the Impala new workspace location, root module name, child module name, web module name, test project name, and repository folder name.

First, go the main newly created workspace's root project, and run the following two commands:

```
cd [workspace location]/scaffold/main
ant fetch get
```

Start Eclipse in the newly created workspace. Use the menus File -> Import ... -> General -> Existing Projects Into Workspace. When prompted, set the import base directory to the workspace root directory.
Select all of the projects and import them.

## Running Impala in Eclipse ##

  * Full suite (as JUnit test): run `AllTests`
  * Individual integration test (as JUnit test): run `MessageIntegrationTest` and/or `ProjectMessageIntegrationTest`
  * Individual integration test (as Java application): run `MessageIntegrationTest` and/or `ProjectMessageIntegrationTest` as Java application
  * Web application: run `StartServer` as Java application. Assuming you used `web` as your web module name, then verify using http://localhost:8080/web/message.htm

## Build and Deploy to Tomcat ##

Build and test:

```
ant clean dist test
```

Create the War file:
```
ant war 
```

Deploy to Tomcat (note that your `tomcat.home` property in _web.properties_ will need to be correctly set for this to copy the files to the correct place):
```
ant tomcat 
```

Run Tomcat
```
cd [tomcat home]/bin
catalina run
```

Check that this is working using the following link: http://localhost:8080/web/message.htm