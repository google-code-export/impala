## Changes affecting existing applications ##

The following changes affect existing application and may require you to implement steps in your own applications to upgrade them to the latest version.

### [Issue 250](https://code.google.com/p/impala/issues/detail?id=250) - rename of static Impala methods ###

Some inconsistencies were identified in the naming of the static methods in the `Impala` class, as well as in the `OperationsFacade` interface.
Some methods were renamed to include the word `Module` where this was missing. This change may affect some application code.

### [Issue 277](https://code.google.com/p/impala/issues/detail?id=277) - split of web project into module and host projects ###

One of the main features introduced with this release is support for Maven. In order to facilitate this, the default project structure has
been changed, with the 'web' project split into two:
  * a 'host' project which contains static resources, files for configuring the Impala runtime (e.g. _moduledefinitions.xml_ and _impala.properties_), as well as certain top level configuration files, such as _web.xml_
  * a 'web' module, simply contains module specific files.

Previously, both of these sets of files were contained in the same project, which was not especially intuitive and potentially confusing.

To migrate from the previous project structure:

  * copy the existing 'web' project, and give it a name to make it clear that it will be used as the 'host' project.
  * in the 'host' project:
    * remove the existing contents of _resources_, and move the contents of _webconfig_ into _resources_.
    * delete all classes apart from `StartServer`.
    * change the implementation of `StartServer` to point to the _context_ location in the 'host' project.
  * in the 'web' project:
    * remove the _context_ folder and _webconfig_ folder.
    * remove `StartServer` from the _test_ source folder.

The end result have only module-specific files in the 'web' project.

In addition, the 'build' project should be updated:

The _web.project.list_ value in _build.properties_ should be updated to point to the 'host' project, not the project containing the 'web' module.

```
web.project.list=example-host
```

## Internal framework changes ##

The following changes are generally internal to the framework and should not require any additional user intervention.

### [Issue 129](https://code.google.com/p/impala/issues/detail?id=129) - move of processTransitions method ###

This change was marked as backward incompatible in that it affects implementations of `ModuleStateHolder`. It will not affect application code.

### [Issue 259](https://code.google.com/p/impala/issues/detail?id=259) - separation of interfaces holding application state ###

A few changes were introduced to the internal interfaces to separate more clearly the interfaces which hold application state. This will
make it easier in the future to support seamless production upgrades, multiple concurrent applications, etc. However, it does not affect any
existing applications. Interfaces affected now have either `Application` or an application id `String` passed in as parameters to the
modified methods.

### [Issue 270](https://code.google.com/p/impala/issues/detail?id=270) - getClassLocations method removal ###

The method `getClassLocations` was removed from the `ModuleLoader`. This is an internal simplification only.

### [Issue 275](https://code.google.com/p/impala/issues/detail?id=275) - refactoring of web multi-module wrapper classes and interfaces ###

The `ServletContext`, `HttpSession` and `HttpServletRequest` wrapper classes and interfaces have been renamed, refactored and moved as part of this release.
Application code will not be affected. However, extensions using the wrapper interfaces will be affected.
