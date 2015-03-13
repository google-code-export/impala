Impala has a lot of features designed to make using and working with web applications as powerful and seamless as possible.
Impala provides flexible deployment options, and supports a large range of scenarios in enabling modular application development.

## Web deployment ##

Impala web applications can be launched in the Eclipse in an embedded Jetty Server. This is powerful, because it means that
you can do all your web application development without having to run any build scripts. However, war based deployment is also supported.
For more information, see the [tutorial](GettingStarted.md).

## Features ##

Below are some of Impala's web application development features:

  * **modular web applications**: we have the advantages of modularity available for the web tier of our applications just as with any other tier.
  * **dynamic reloading support for other frameworks**: Impala provides the support not only for Spring MVC-based web applications, but for web applications written using arbitrary Java web frameworks (see the bottom of the page for a caveat on this)
  * **objects held in the `HttpSession` should survive module reloads**, even if the class loader used to load the object originally saved in the session is discarded. See below in this page for more details.

## Impala Web Development Documentation ##

[Bootstrapping web applications](WebApplicationBootstrapping.md) - describes initial setup configuration.

[Anatomy of an Impala web application](WebApplicationStructure.md) - describes briefly how different Impala configuration files are used during Impala and application load process.

[Multi-module web application development](WebMultiModuleApplications.md)
  * [Web XML based servlet and filter registration](WebXmlHandlerRegistration.md)
  * [Module based servlet and filter registration](WebModuleBasedHandlerRegistration.md)

[Impala servlets and filters](ServletsAndFilters.md) - reference on servlets and filters provided by Impala.

[Session management with module reloads](WebSessionManagement.md) - describes how session state is preserved for reloaded module classes.

[Third party frameworks support](WebThirdPartyFrameworks.md) - background and info on getting Impala working with web frameworks other than Spring MVC.

[Web namespace handler](NamespaceWebReference.md) - reference for the Impala `web` namespace, which simplifies servlet and filter registration, especially for [module-based servlets and filters](WebModuleBasedHandlerRegistration.md).





