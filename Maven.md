# Introduction #

As of Impala 1.0 RC2, Impala can be built using Maven instead of using the built-in ANT build system.
If you are using Maven, you don't need to download Impala, but instead you add Impala-specific entries into you projects' _pom.xml_ files.

## Repositories ##

The following repositories are being used at present:

  * **snapshots:** http://oss.sonatype.org/content/repositories/impalaframework-snapshots
  * **milestone releases:** http://oss.sonatype.org/content/repositories/impalaframework-releases

Impala is not yet available on the central Maven repository.

## Project structure ##

The project structure is best illustrated through the [Maven sample project](SamplesMaven.md). Basically, it consists of the following:

  * a parent build project, used to manage the overall build. At present it also contains all the dependency definitions.
  * a host web project, containing the static web content, the main _web.xml_ file for the application, but not the application modules.
  * a project for each application module.

If you are interested in using Impala with Maven, you are recommended to check out the [Maven sample](SamplesMaven.md).
More detailed documentation as well as a Maven archetype should be available as part of the next signifant release of Impala.

## Comparison with standard Maven multi-module build ##

The build first builds the application modules, then builds the web project, as with other Maven multi-module builds.
The difference is that the web project does not depend on the module projects directly. Instead, an Impala Maven plugin facilitates the build by:
  * copying the built artifact jars to a temporary staging directory after each project is built (as part of the _package_ step).
  * copying the modules from the temporary staging directory into _WEB-INF/modules_, to make these available as Impala modules.