This page describes how to rename a module. In addition to changing the project name (in Eclipse, for example), there a few other places where changes are required.

  * Rename the project in Eclipse
  * Rename the property `project.name` in _build.properties_
  * Rename the `name` property in _build.xml_
  * Rename the module Spring application context bean definition XML file. This step is necessary if you are relying on the convention of the application context XML having the name _%module\_name%-context.xml_.
  * Rename the module in the web root project's _webconfig/moduledefinitions.xml_ file.
  * Rename the module in the `project.list` property in the build project's _build.properties_.