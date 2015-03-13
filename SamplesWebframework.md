# Web frameworks sample #

The web frameworks sample demonstrates tight integration with a number of web frameworks, showing how Impala can offer full module reloading and Spring integration capability with multiple web frameworks, all running in separate modules and using separate filters. The example demonstrates some advanced aspects of Impala's web framework support, such as per-module root Spring application contexts, thread context class loader management, preservation of session state across module reload.

It also features a JMX console for module reloading.

Examples are created for Struts 1, Struts2, Tapestry 5, Wicket and JSF.

See also WebMultiModuleApplications for more details on how multi-web module support works in Impala.

## Project setup ##

The easiest way to access the examples is to check them out directly from trunk.

```
svn co http://impala.googlecode.com/svn/trunk/webframeworks-sample webframeworks-sample
```

Then open an Eclipse workspace in the new _webframeworks-sample_ folder, and import all the projects into the workspace.

## Run the sample ##

From within Eclipse, run StartServer as a web application. Then connect using the URL:

http://localhost:8080/webframeworks-host/