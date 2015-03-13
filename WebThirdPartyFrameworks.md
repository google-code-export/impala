Impala has built-in support for Spring MVC as the default web application framework. However, Impala has general support for third party frameworks.
For some frameworks, it is possible to set up handlers entirely using Spring. Others, such as Struts, do so through their own configuration file.
With a little work it is possible to set up your Impala application to support most

Examples for Struts, Wicket and Tapestry are provided in the [web frameworks sample](SamplesWebFramework.md).

The ability to support this is achieved through Impala's management of the thread context class loader.

### Thread context class loader ###

The thread context class loader is used to allow Java libraries to application classes through the application classes' class loader, rather than the library classes' class loader.
Before invoking any module specific servlet or filter, Impala changes the context class loader so that other web frameworks have visibility of the correct module's classes.

For more discussion on this point, see http://impalablog.blogspot.com/2008/10/using-threads-context-class-loader-in.html.

### Setup ###

TBD - add section on use of delegator servlet to pass control from `ExternalFrameworkIntegrationServlet`, `InternalFrameworkIntegrationServlet`
and `InternalFrameworkIntegrationFilter` to third party framework handler.

TBD - describe how Impala's web namespace supports third party web framework configuration.

## Caveats ##

There are some important caveats to note regarding Impala's support for integration with web frameworks other than Spring MVC.

1) At this point, the [web frameworks sample](SamplesWebframework.md) demonstrates that tight integration with web frameworks is possible. The basic mechanisms needed for this integration are required:
  * ability to handle the thread context class loader to correctly.
  * the ability to give each module access to it's own Spring root web application context in a way that is transparent to the framework. In the web frameworks sample, for example, the web application context visible to the Struts module is different from the one visible to the Wicket module, even though they are accessed using the same `WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE`.
  * the ability to instantiate an aribitrary servlet within a module, and manage its life cycle correctly.
2) This does not mean that there will not be issues. Each web framework is different, and makes it's own set of assumptions about the environment in which it operates. In a dynamic, multi-module environment, some of these assumptions may be stretched. Without further testing on a per framework basis, it is not possible to know where the problem points lie.
However, I believe that the problems are likely to be relatively minor and not too difficult to overcome on a case by case basis.
3) Eventually, we can expect that for web frameworks that we decide to properly support, there will be integration projects which resolve these issues and provide a seamless integration with the web framework which will work out of the box.


