## Background ##

In HowToSetUpDevelopmentAutoReloading we describe how to set up auto-reloading when developing in Eclipse. The following describes how to set
up reloading on an application server. The term 'production' here is used loosely. It simply means on a real application server environment rather than
in the embedded Eclipse environment.

The mechanisms used are basically the same. However, Impala adds a couple of extra features to help make reloading more robust, at the cost of
a small amount of extra configuration and/or deployment effort.

## Setup ##

The first thing to do is to set the `auto.reload.modules=true`
flag in _impala.properties_.

In an an application server environment, this will result in Impala monitoring for changes in module jar files, typically found in _WEB-INF/modules_.

Making changes to the module jar files will require at some stage that the files be overwritten, which could be dangerous, for example, if
the application attempts to load a class or resource from a file while this is taking place. In some environments, this type of overwriting may not
be possible as the module jar may already be locked by the application.

Instead, it makes sense to use a staging location or a temporary file, which can be set as follows:

```
auto.reload.monitoring.type=tmpfile
```

for temporary files, and

```
auto.reload.monitoring.type=stagingDirectory
```

for the staging directory approach.

If the temporary file approach is used, Impala will instead monitor for changes in files which have the same name as the module jars, but have the extension
_.tmp_ instead of _.jar_. If a file by this name is added or modified, Impala will block new incoming requests, replace the relevant module jar content from the
corresponding temporary file(s), and then perform module reloading.

For the staging directory approach, a parallel staging directory is used, but the principle is the same. By default, the staging directory location is
_../staging_ (relative to _/WEB-INF/modules_), but this can be modified using the _auto.reload.staging.directory_ property.

Another refinement is to use a touch file. By default, Impala will monitor the module resources (or their temporary file or staging directory cousins).
However, if a touch file is used, Impala will only check for modifications in these files if a change is detected to the touch file.

To use a touch file, use an entry such as:

```
use.touch.file=true
#Optional - uncomment to override the default value
#touch.file=/WEB-INF/modules/touch.txt
```

Using a touch file can make module loading more efficient, and can also help to make sure that all changes have been applied to the application environment
before attempting to initiate module reloading.
