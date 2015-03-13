# OSGi sample #

This sample is designed as a testbed for Impala's OSGI support.
As of release 1.0M4 this is still preliminary/experimental.

## Project setup ##

The easiest way to access the examples is to check them out directly from trunk.

```
svn co http://impala.googlecode.com/svn/trunk/osgi-sample osgi-sample
```

Then open an Eclipse workspace in the new _osgi-sample_ folder, and import all the projects into the workspace.

## Run the sample ##

From within Eclipse, run `MessageIntegrationTest`, `OsgiContextTest` or `ReloadTest`.
As of release 1.0M4 running these in a suite is still not supported.