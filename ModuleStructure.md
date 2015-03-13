It is important to understand the role of the different files that are present in the modules. We'll use the Petclinic example application to show the internal structure of modules. The projects contained in the Petclinic example are shown below:

![http://impala.googlecode.com/svn/wiki/images/petclinic_all.png](http://impala.googlecode.com/svn/wiki/images/petclinic_all.png)

The Petclinic application has been divided into a main or root module (_petclinic_), two application modules (_petclinic-hibernate_ and _petclinic-service_), and a web module ( _petclinic-web_). A real world application would probably have quite a few more modules. Perhaps a dozen or so application modules, a root web module, and a number of servlet modules for dividing the web portion of the application into logic elements.

Notice also there are an additional two projects which are not modules. These are:

  * _petclinic-repository_: used to contain the third party libraries used for the application.
  * _petclinic-tests_: a convenience project whose primary purpose is to run the suites of tests for all modules as a single JUnit suite run within the IDE.

From Impala 1.0M5 onwards, there is also a separate 'build' project, which is used to contain master build scripts, data scripts, documentation, etc. This takes pressure off the _main_ module's project (described below), which was previously used for this purpose.

In the next section, we'll describe in a bit more detail the internal structure of each of the module types.

## Host project ##

The host module is a regular Java web application which is capable of hosting Impala modules.

The build time structure of the host project is shown below:

![http://impala.googlecode.com/svn/wiki/images/petclinic_host_structure.png](http://impala.googlecode.com/svn/wiki/images/petclinic_host_structure.png)

Impala web applications don't have any special structure that distinguishes them from regular Java WAR files. The only real difference
is that modules are not located on the web application class path.
Instead, these are found in locations known to Impala module class loaders. When deploying Impala as a WAR file to Tomcat, for example,
the modules will be found in jar files in the _WEB-INF/modules_ directory. However, other runtime configurations are possible,
for example, when running Impala embedded within Eclipse.

The host project contains basic web application resources such as _web.xml_, as well as web application files which are not contained within
the modules themselves. For example, as of version 1.0 RC2, Impala does not support hosting JSP within modules.

In addition to the locations which exist for modules, there are two extra folders which contain web application specific content.

### _resources_ ###

_webconfig_ contains resources which are not intended to appear on the web module's class path, but instead are intended to appear on the
web application's class path. In Tomcat, for example, these files will be visible to Tomcat's web application class loader. In practice, these files
will end up in the _WEB-INF/classes_ directory of the web application.

The key Impala-specific files in this folder are:
  * _impala.properties_: the property file used to configure Impala itself. You can use this file to specify the application context files which should be loaded when Impala is initialised (yes, Impala is initialised as a Spring application context). It also contains various Impala-specific configuration properties.
  * _impala-embedded.properties_: an alternative to _impala.properties_ when Impala is run embedded within the IDE. The structure of the project at runtime can vary depending, for example, whether you are running Impala within a WAR file or running embedded it within Eclipse. The different settings in these respective files reflect these differences.
  * _module\_definitions.xml_: this is the module definition descriptor which lists the modules to be loaded by the web application. It also can be used to specify module configuration overrides (departures from the default configuration).

### _context_ ###

_context_ contains the resources for the web application, such as the _web.xml_ file, markup templates, images, styles, etc.

Unlike the application modules themselves, the host part of an Impala web application is static,
and cannot be dynamically reloaded without restarting the whole application.
Where possible, it is a good idea to move files and resources in the host to modules.

## Main or root module ##

The structure of the main module is shown below.

![http://impala.googlecode.com/svn/wiki/images/petclinic_main_structure.png](http://impala.googlecode.com/svn/wiki/images/petclinic_main_structure.png)

The _src_ and _test_ folders contain (mostly Java) source and test files respectively, as with pretty much any other Java project.
_resources_ contains additional resources for the project, such as the module's Spring configuration files (here simply _parent-context.xml_), as well as other resources which might need to be found on the classpath.

An import file in the _resources_ folder, from Impala's point of view, is _module.properties_. This file information about the structure of the module
and its position in the module hierarchy. For example, it is used to specify the module's parent (if it has one), and a list of Spring configuration files,
if the default file is not being used.

Note that the contents of both _src_ and _resources_ will end up in the same classes folder or Jar at runtime. Splitting them is just a convenience to
allow you to separate Java source from other resources.

Files in _test_ are not intended to end up as module contents.

The project also contains _.classpath_ and _.project_ files: these are simply files used by the Eclipse IDE, not specific to Impala.

_build.properties_ and _build.xml_ are only useful if you are using Impala's ANT based build system. If you were using Maven or some other tool to build Impala, these files
would not be necessary. (Note that Maven support is not available provided by Impala, although there is no reason why a Maven enthusiast couldn't use Impala with Maven. They would have a bit of work to do, though.

_dependencies.txt_ is only useful if you are using Impala's [dependency management mechanism](DependencyManagement.md), which allows you to download dependencies with source from local or public Maven repositories (yes, we do use Maven after all!).

## Application module ##

The basic structure of an application module is identical to the root module. See below.

![http://impala.googlecode.com/svn/wiki/images/petclinic_hibernate_structure.png](http://impala.googlecode.com/svn/wiki/images/petclinic_hibernate_structure.png)

The application module has the same project layout, the same locations for configuration files, etc.

Of course, the roles of individual files within the modules are different.
The root module Spring configuration file tends to contain _interface_ beans or beans which are supposed be used by many modules.
The _build.properties_ and _build.xml_ have a larger role, not only in building the root project, but also in co-ordinating the builds of application and web modules,
while the contents of the application module build files reflect the fact that their main if not only job is to build their containing module.

## Web module ##

Unlike the application modules, the web module has a few extra elements which distinguish its structure from that of the root module.
This of course is because it contains files supporting a web application.

The structure of an Impala web module is shown below:

![http://impala.googlecode.com/svn/wiki/images/petclinic_web_structure.png](http://impala.googlecode.com/svn/wiki/images/petclinic_web_structure.png)


