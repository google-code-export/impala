# Overview #

A core idea behind the multi-module web application is to reduce dependency on the _web.xml_ (which is static, cannot be reloaded or added
to). So while it is possible to map to multiple modules through servlets registered in _web.xml_, it tends to be cumbersome and
a lot less flexible than the approach described below.

Instead, Impala allows you to delegate requests to modules, and allow these modules to host filters, servlets, resources, etc.
Indeed, it is possible to reduce your dependency on web.xml to bootstrapping Impala and to provide a catch-all filter mapping:

```
<filter>
    <filter-name>web</filter-name>
    <filter-class>org.impalaframework.web.spring.integration.ModuleProxyFilter</filter-class>
    <load-on-startup>2</load-on-startup>
</filter>

<filter-mapping>
    <filter-name>web</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

This style of development is illustrated in the [URL mapping sample](SamplesURLMapping.md) - here's the
[the full web.xml for the application](http://impala.googlecode.com/svn/trunk/urlmapping-sample/urlmapping-web/context/WEB-INF/web.xml).

Much of the configuration is done through Impala's new [web namespace handler](WebNamespaceHandler.md).

**Note:** if you only wish to use a single web tier module exposed through a single _web.xml_-registered servlet, then you can find out
more about this approach in WebXmlHandlerRegistration.


---


---


## Setting up a multi-module application ##

The [URL mapping sample](SamplesURLMapping.md) is fairly well documented. This page describes in a bit more detail the bits which go into
a multi-module application.


---


### Setting up the web.xml ###

In a typical Java enterprise application, the _web.xml_ is used to define servlets and filters, which are among the main
entry points into your application from the outside world. Since _web.xml_ cannot be reloaded, added to or modified without reloading
the entire application, it is not a very convenient place to host application configuration and definitions in a dynamic module applications.
However, you cannot do away with the _web.xml_ altogether. In the example above, the `ModuleProxyFilter` is used to direct requests to
servlets. Alternatively, you can use `ModuleProxyServlet` for this purpose.


---


### Request to module mapping ###

The question is, how does Impala know how to map requests to different modules. This capability, used by both `ModuleProxyFilter` and
`ModuleProxyServlet`, is encapsulated in the `RequestModuleMapper` interface.

As a user of Impala, you won't need to interact with this interface directly, but it is worth knowing about it.
Impala provides implementations of this interface.

#### Simple path prefix mapping ####

The **first** does a mapping based on the first part of the URL after the context path.
For example, in the URL:

```
http://localhost:8080/application/module1/someresource.htm
```

the first part of the URL after the context path is _module1_. By default, `ModuleProxyFilter` will attempt to direct this request to a module
named _module1_.

Quite a common practice with Impala is to have modules belonging to a common application sharing a prefix. Suppose for example you are using the
prefix _myapp_, and that rather than having a module named _module1_, you have a module named _myapp-module1_. Impala caters for this situation
by allowing you to specify a module prefix as an init parameter of `ModuleProxyFilter` or `ModuleProxyServlet`. For example:

```
<filter>
    <filter-name>web</filter-name>
    <filter-class>org.impalaframework.web.spring.integration.ModuleProxyFilter</filter-class>
    <init-param>
            <param-name>modulePrefix</param-name>
            <param-value>myapp-</param-value>
    </init-param>
    <load-on-startup>2</load-on-startup>
</filter>
```

In this case, the URL `http://localhost:8080/application/module1/someresource.htm` will be mapped to the module `myapp-module1`.

#### Explicit path prefix mapping ####

The forthcoming version of Impala (1.0M7) allows you to explicitly configure which prefixes are mapped to which modules.
This gives you a lot of flexibility in defining which URLs get mapped to which module.

In order to take advantage of this feature, you will need to enable the `module.prefix.mapping.enabled` property in _impala.properties_.

```
module.prefix.mapping.enabled=true
```

With this done, you can then specify within the modules themselves, which paths are mapped to the current module. In the [URL mapping sample](SamplesURLMapping.md),
requests are mapped to the module _urlmapping-web_ using the `prefix` element from the new Impala web namespace. In the file _urlmapping-web-context.xml_,
the following declarations are found:

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:web="http://www.impalaframework.org/schema/web"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans 
http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.impalaframework.org/schema/web 
http://impala.googlecode.com/svn/schema/web.xsd">

	<import resource="urlmapping-web-common.xml"/>

	<web:mapping>
		<web:to-module prefix = "/main" setServletPath="true"/> 
		...
	</web:mapping>    
    
    <!-- servlet and/or filter definitions, controllers, etc -->
    ...

</beans>
```

The key line here is the `<web:to-module prefix = "/main" setServletPath="true"/>`, which basically says: map to the current module
requests with URLs starting with `http://localhost:8080/myapp/main`.

You can define as many `web:mapping` elements as you like in your Spring context and file, and in each of these,
you can define as many `web:to-module` elements as you like. You can even define multiple paths to the same resource, although can't think of
why you would ever want to do this!


---


### Registering Filters and Servlets ###

Impala allows servlets and filters to be registered within the modules themselves. Unlike servlets and filters registered
in _web.xml_, whose life cycle is tied to that of the application, the life cycle of servlets or filters registered
within the module is tied to that of module itself. This means that when the module loads, the servlet
or filter's `init` method is called, and when the module unloads, the servlet or filter's `destroy` method is called.

Init parameters can be passed to the servlets or filters, either using the `initParameters` attribute or a
nested `init-parameters` element.

The example, below, taken from the _urlmapping-webview_ module from the [URL mapping sample](SamplesURLMapping.md)
has two servlet and two filter definitions. In the next section we explain how these are mapped to requests.

```
<web:servlet id = "urlmapping-webview" 
    servletClass = "org.impalaframework.web.spring.servlet.InternalModuleServlet"/>
    
<web:servlet id = "urlmapping-resources" 
    servletClass = "org.springframework.js.resource.ResourceServlet"
    initParameters = "cacheTimeout=10"/>
    
<web:filter id = "characterEncodingFilter" 
	filterClass = "org.springframework.web.filter.CharacterEncodingFilter" 
	initParameters = "forceEncoding=true,encoding=utf8">
</web:filter>

<web:filter id = "sysoutLoggingFilter" 
	filterClass = "org.impalaframework.urlmapping.webview.SysoutLoggingFilter">
</web:filter>
```

The servlets and filters are set up using the new Impala `web` namespace. Internally, the creation of servlets and filters is
done through `ServletFactoryBean` and `FilterFactoryBean`.

Notice that the first servlet does not have a servlet class specified. The default used is
`org.impalaframework.web.spring.servlet.InternalModuleServlet`, which is an extension of
the Spring `Dispatcher` servlet, and as such, provides the entry point to any Spring MVC-based controllers defined within the module.
It is theoretically possible to define multiple such servlets, although it would be pointless. The convention is to define
one which has the same name as the module itself.

See this page for more [details on defining servlets and filters](WebNamespaceHandler.md) using the new web namespace handler.


---


### Mapping requests within a module ###

For Impala 1.0M6 and below, it is only possible to map requests which are directed to a module to a single servlet or filter defined within the module.
Subsequent versions remove this restriction. Indeed, any request can be mapped to a combination of zero or more filters registered within the module,
and/or optionally to a servlet registered within the module. The documentation covers versions after 1.0M6.

While mapping of requests from outside Impala to modules is typically done by prefix, mapping within the module is done by suffix.
The following example is taken from the _urlmapping-webview_ module in the [URL mapping sample](SamplesURLMapping.md).

```
<web:mapping>
	...
	<web:to-handler extension = "htm" servletName="urlmapping-webview" filterNames = "characterEncodingFilter,sysoutLoggingFilter"/>
	<web:to-handler extension = "css" servletName="urlmapping-resources"/>
</web:mapping>    

... servlet and filter definitions   
   
```

Using the two `web:to-handler` entries, the requests ending with the extension `htm` are mapped to the servlet `urlmapping-webview`,
and requests ending with `.css` are mapped to the servlet `urlmapping-resources`.

In addition, `.htm` requests are mapped to the `characterEncodingFilter` and the `sysoutLoggingFilter`. The semantics of this behaviour
is the same as for within the web container itself. The request will be processed first by `characterEncodingFilter`, then by `sysoutLoggingFilter`,
and finally, by the servlet. As with standard web applications, the filter can short circuit execution by not calling `doFilter(...)` on the
received `FilterChain` instance, in which case neither subsequent filters nor the mapped servlet will get the chance to handle the request.

Note that any number of `web:to-handler` entries can be
placed within a `web:mapping` element. In this way it is possible to map multiple extensions to a single servlet, or to
specify combinations of servlets and filters to URLs with different extensions should be mapped.



