# Overview #

When working with vanilla Spring applications, servlets and filters are set up in _WEB-INF/web.xml_. Impala offers two choices:
to set up these handlers in _web.xml_, and to set these up in the modules themselves.

The [module-based mechanism](WebModuleBasedHandlerRegistration.md) for servlet and filter setup offers more flexibility when developing
applications for which it is suitable to split the web tier into multiple modules. If only a single web tier module is
required, an alternative is _web.xml_ setup mechanism is available, which is perhaps simpler to configure to begin with
but less flexible in the options that it supports for multi-module applications.

## An example ##

The [Petclinic sample](SamplesPetclinic.md) has a single Spring-MVC based web tier, and is convenient to use as an example.

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/j2ee"
	      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	      xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"
	      version="2.4">
  	...

    <listener>
        <listener-class>org.impalaframework.web.spring.loader.ImpalaContextLoaderListener</listener-class>
    </listener>
    
    <servlet>
        <servlet-name>petclinic-web</servlet-name>
        <servlet-class>org.impalaframework.web.spring.servlet.ExternalModuleServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>petclinic-web</servlet-name>
        <url-pattern>*.htm</url-pattern>
    </servlet-mapping>

	...      
</web-app>
```

Note the use of the `ImpalaContextLoaderListener`, which is a base class of the usual Spring `ContextLoaderListener`.

The servlet used here is `org.impalaframework.web.spring.servlet.ExternalModuleServlet`. This class is a subclass of
the Spring `DispatcherServlet`. Note that the name of the servlet, `petclinic-web`, corresponds with the name of
module which contains the Spring MVC controllers to which requests will be dispatched.

## Limitations ##

There are a number of limitations to the _web.xml_-based approach to
registering servlets for an application.

**Firstly**, all the servlets and filters required for the application must be known at application startup time. It
is not possible to decide after the application has initially loaded that another servlet or filter should be added or removed.

**Secondly**, if you attempt to register an `ExternalModuleServlet` instance without a module present to which to connect requests,
an exception will be thrown, and the application will fail to load. Together with the previous point, this means that
the identity of all the modules must be known at application startup.

A **third** limitation concerns the way requests are mapped in a Java web application. The servlet API allows you to map servlets
either using a prefix based mapping (e.g `/myapp/myservlet/*), or using an extension based mapping (e.g. `**.do`). However, it
does not allow you to map to servlets based on a combination of the above.**

In an Impala application, if you use a prefix based mapping to map to `ExternalModuleServlet`, then you will need to use
a different prefix to map to resources in your application (e.g. images, styles, JavaScript files, etc.). However,
if you use a suffix based mapping, you will need to use a different suffix after each module. In both cases,
hosting application resources in the module itself is not possible - instead they need to be held in a common directory on the file
system below the root context folder in your web application.
This means that your web modules are not actually that modular. If you wish to add a module to your an Impala web application, not only will
you need to add your module into the `/WEB-INF/modules` directory, you will also need to add resources for the module into another directory
or directories below the module context path.

For this reason, for [multi-module web applications](WebModuleBasedHandlerRegistration.md), we recommend using the technique of
registering servlets and filters in the module itself, and reducing reliance on _web.xml_, as described [here](WebModuleBasedHandlerRegistration.md).




