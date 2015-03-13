## Background ##

For genuinely modular web applications, you will want to modularise not only your application back end modules but also your
web modules. Impala allows you to do this, effectively 'slicing' your application front end into different web modules, each of which
can serve separate portions of your applications URL space.

In other wors, Impala allows you to allocate different URLs to different modules.

For full details see WebModuleBasedHandlerRegistration.

## Setup ##

**1) Set up _web.xml_**

Simply add the following entry to an empty _web.xml_:

```
<filter>
    <filter-name>web</filter-name>
    <filter-class>org.impalaframework.web.spring.integration.ModuleProxyFilter</filter-class>
</filter>

<filter-mapping>
    <filter-name>web</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

This registers a servlet filter which directs all requests to Impala's module selection mechanism.

**2) Set up Impala's module-aware `HttpServletRequest` and `ServletContext`**

Add the following entry to _impala.properties_

```
module.prefix.mapping.enabled=true
partitioned.servlet.context=true
```

The first of the entries is required, and allows Impala modules to 'subscribe' to incoming requests, as set up in 3) below.
The second ensure that web modules are exposed a 'module-aware' `ServletContext`. With this setting enabled, both
`ServletContext` and `HttpSession` attributes are partitioned, so that by default they are only visible within the module
(unless prefixed with `shared:`). It also allows `ServletContext.getResource()` and related methods to return
resources first from the module class path, and then from the usual web application root directory.

**3) Set up modules to receive requests for particular URLs**

Add the following entry into your web module's Spring configuration file.

```
<web:mapping>
	<web:to-module prefix = "/urlprefix" setServletPath="true"/> 
</web:mapping> 
```

This entry, from the [Impala web namespace](NamespaceWebReference.md), sets any URL with the prefix `urlprefix` to be directed
to the the current module. It also modifies the value that will be returned from `HttpServletRequest.getServletPath()`
to be `/urlprefix` (without the `serServletPath` entry, the servlet path will be determined purely from the `ModuleProxyFilter`
match in 1).

**4) Map requests within modules to servlets and filters**

The [Impala web namespace](NamespaceWebReference.md) allows you to set up servlets and filters within the module which can receive requests within
the module, as shown in the example below.

```
<web:mapping>
	<web:to-module prefix = "/urlprefix" setServletPath="true"/> 
	<web:to-handler extension = "htm" servletName="dispatcher" filterNames = "characterEncodingFilter"/>
	<web:to-handler extension = "css" servletName="resources"/>
</web:mapping> 

<web:servlet id = "dispatcher" 
    servletClass = "org.impalaframework.web.spring.servlet.InternalModuleServlet"/>
    
<web:servlet id = "resources" 
    servletClass = "org.springframework.js.resource.ResourceServlet"
    initParameters = "cacheTimeout=10"/>
    
<web:filter id = "characterEncodingFilter" 
	filterClass = "org.springframework.web.filter.CharacterEncodingFilter" 
	initParameters = "forceEncoding=true,encoding=utf8">
</web:filter>
```

Above, we have a dispatcher servlet (to handler application requests), a resources handling servlet, and a filter. The `web:to-handler` entries
map requests to the module with extension `htm` to the encoding filter and despatcher servlet, and CSS style sheets simply to the
resource servlet.
