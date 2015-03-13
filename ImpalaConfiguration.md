# Overview #

The most important thing to know about Impala configuration is that Impala is itself simply loaded up as a set of Spring beans.

Just like in plain Spring applications, the Impala `ApplicationContext` can be instantiated in a number of ways.

The basic process is as follows:

  1. The Impala itself is bootstrapped. At this point Impala is just a collection of Spring beans. Unlike the modules themselves, the `ApplicationContext` which backs the Impala runtime remains pretty static through the lifetime of the application.
  1. The reading of module definitions takes place. No modules have been loaded, but the runtime will now have an understanding of what modules need to be loaded.
  1. The actual loading of modules takes place. The application is ready to service requests.

As with plain Spring applications, the mechanism for setting up the Impala runtime environment varies according to whether the application
is running as a standalone application (for example, in integration tests), or in a web application (for example in an embedded Jetty server in the IDE
or in a war file in a servlet container such as Tomcat).

  * [Configuring Impala in a standalone environment](ImpalaStandaloneConfiguration.md)
  * [Configuring Impala in a web environment](WebApplicationBootstrapping.md)
