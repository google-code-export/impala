Impala allows you to arrange your web tier modules in a hierarchy. Just as you have a root module for the application as a whole,
you also have a root module for the web tier, know as the _web root module_.

The _web root module_ contains the servlet context resources,
that is, the resources which will be found below the context path in a deployed application, such as styles, images, jsp files, etc.
It is also possible to host resources in the modules themselves.

Impala provides web application modularity by allowing each module to be represented by at least one filter or servlet.
It is also possible to have multiple filters or servlets servicing requests, per module.

There are two styles of setting up handlers (filters and servlets) for your aplication:
  * [Standard \_web.xml\_ based handler registration](WebXmlHandlerRegistration.md). This is simpler getting started but is much more limited in what you can achieve.
  * [Module based handler registration](WebModuleBasedHandlerRegistration.md). This requires a little more understanding but offers much greater flexibility when developing multi-module applicaitons.






