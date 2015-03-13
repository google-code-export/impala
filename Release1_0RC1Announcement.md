Impala 1.0 RC1 is the first final 1.0 release candidate, which means that there are no more major features planned for the 1.0 final
release which still need to be implemented. Only minor enhancements and bug fixes will be added prior to 1.0 final.

Impala 1.0 RC1 contains a number of big feature improvements from the previous release:
  * a much more powerful mechanism for routing requests to and within modules, making it easier to build truly multi-module web applications. See WebModuleBasedHandlerRegistration, as well as the [URL mapping sample](SamplesURLMapping.md).
  * a new Spring web namespace for registering servlets, filters and other web artifacts in Impala web modules. See NamespaceWebReference.
  * enhancements to make the automatic module reloading mechanism more robust and suitable for applying in production environments.
  * various other minor bug fixes and enhancements, particularly in the areas of build, dynamic services and class loaders.

Two new samples have also been created during the work on this release, including the [URL mapping sample](SamplesURLMapping.md), which demonstrates
Impala's improved web multi-module request routing capabilities, and the [Spring Faces sample](SamplesSpringFaces.md), which shows Impala working with a technology stack which includes JSF, Spring Web Flow, Spring Security and JPA.

See the [full list of issues covered for milestone 1.0 RC1](http://code.google.com/p/impala/issues/list?q=label:Milestone-Release1.0RC1&can=1).

If you are migrating from a previous version, please take a look at the [1.0 RC1 migration guide](Migration_1_0RC1.md).

If you like Impala and would like to support the project, please take a look at this page: http://code.google.com/p/impala/wiki/GetInvolved.