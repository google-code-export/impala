# Maven sample #

This example is really designed to illustrate the usage of Impala with Maven, allowing Maven project structure
conventions to be used, and allowing Maven to be used as a build tool.

As of 16 November 2009, this is work in progress. The application can be checked out and run, but support for building valid Impala war files is
not yet complete.

## Project setup ##

The easiest way to access the examples is to check them out directly from trunk.

```
svn co http://impala.googlecode.com/svn/trunk/maven-sample maven-sample
```

Then open an Eclipse workspace in the new _maven-sample_ folder, and import all the projects into the workspace.

## Run the sample ##

From within Eclipse, run StartServer as a web application. Then connect using the URL:

http://localhost:8080/maven-host/