## Background ##

Impala has a powerful feature which allows you to automatically pick up changes in a module, and reload the module as well as its dependents.
This allows for an extremely rapid build/deploy/test cycle, as application changes can be transparently applied without any user intervention.

## Setup ##

When running a web application in Eclipse using the embedded Jetty server, setting up auto-reloading is simply a case of setting the `auto.reload.modules=true`
flag in _impala-embedded.properties_. This is by far the most productive way to develop, because it involves development without any explicit build step.

(Note the use of _impala-embedded.properties - this is the default Impala configuration file for running the application in 'embedded' mode)._

If the application gets large, it may be necessary to limit the frequency of module reloads by being more selective on which resources
it monitors for changes. By default, the application will monitor all files
on the modules class path. You can change this using the `auto.reload.extension.includes` and `auto.reload.extension.excludes` properties.

In the example below, a module is only reloaded if a change is detected to a class (with a _.class extension) or an XML file ending in_context.xml_._

```
auto.reload.extension.includes=context.xml,class
```

In the example below, a module is reloaded if a change is detected to any file in the module class path which is not a _.txt_ file.

```
auto.reload.extension.excludes=.txt
```

The use of excludes and includes and reduce the number of 'spurious' reloads, which if too frequent, can drain system resources unnecessarily.