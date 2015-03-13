## Adding the namespace headers ##

Before using one of the `web` elements in your bean definition file, you will need to add the Impala `web` namespace declaration
to the root element of the relevant Spring configuration files.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:web="http://www.impalaframework.org/schema/web"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans 
http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
http://www.impalaframework.org/schema/web 
http://impala.googlecode.com/svn/schema/web.xsd">
```

Note the entry `xmlns:web="http://www.impalaframework.org/schema/web"` declares the Impala web namespace, and the entry
`http://www.impalaframework.org/schema/web http://impala.googlecode.com/svn/schema/web.xsd` associates
this with a schema location.


---


## Mapping Elements ##

### context-listener element ###

Creates a module level servlet context listener whose life cycle is tied to that of the module.
`contextInitialized()` is called when the module loads, and `contextDestroyed()` when the module is unloaded.

### mapping element ###

Container element for to-module and to-handler mappings.
The `to-module` element is used to direct requests for particular URLs, based on the URI to-module, to the current module.
The `to-handler` element is to map requests directed to the current module to particular filters or servlets contained within the module.

An example of the `mapping` element is shown below, taken from the [URL mapping sample](SamplesURLMapping.md):

```
<web:mapping>
	<web:to-module prefix = "/webview" setServletPath="true"/> 
	<web:to-handler extension = "htm" servletName="urlmapping-webview" filterNames = "characterEncodingFilter,sysoutLoggingFilter"/>
	<web:to-handler extension = "css" servletName="urlmapping-resources"/>
</web:mapping>    
  
<web:servlet id = "urlmapping-webview" 
    servletClass = "..."/>
    
<web:servlet id = "urlmapping-resources" 
    servletClass = "..."
    initParameters = "cacheTimeout=10"/>
    
<web:filter id = "characterEncodingFilter" 
	filterClass = "..." 
	initParameters = "forceEncoding=true,encoding=utf8">
</web:filter>

<web:filter id = "sysoutLoggingFilter" 
	filterClass = "...">
</web:filter>
```

In this example, requests beginning with `/webview/*` are mapped to the current module. Once in the module,
requests ending with `*.htm` are mapped to the filters `characterEncodingFilter` and `sysoutLoggingFilter`, and
to the servlet `urlmapping-webview`. Requests ending with `*.css` are mapped to the `url-mapping` resources.

### to-module element ###

Used to direct request to the current module, based on URI to-module.

The prefix of the URL which the request should match. For example, if a request has the URI,
_/app/mypath1/mypathA_, then the path _/mypath1_ will match, as will _/mypath1/my_, as will _/mypath1/mypathA_.

Note that if another module provides a more precise match to a particular path, then the request will be directed
to that module instead. For example, if module A contains a to-module element with path _/prefix1_,
but module B contains one with path _/prefix1/path1_, then a request with URI _/app/prefix1/path1resource_
will be directed to module B, not module A.

#### setServletPath attribute ####

Whether to set the servlet path in the request passed to servlets or filters in this module.
Setting this value has the effect of altering the values returned by calls to
`HttpServletRequest.getSerlvetPath()` and the corresponding `HttpServletRequest.getPathInfo()`.

If the value for this attribute is set to `true`, and `servletPath` attribute
is not specified, then `getServletPath()` returns the value specified using the `path`
attribute. However, is a value is specified using the `servletPath` attribute is specified,
then this value is used.

Calls to `getServletPath()` made outside the module are not affected. Here, value returned by the
servlet container itself is returned.

#### servletPath attribute ####

If a value is set for this optional attribute,
then as long as `setServletPath` is not set with a value of false, then sets
the servlet path in the request passed to servlets or filters in this module.
In other words, the value specified using the `servletPath` attribute is returned
by calls from within the module to `getServletPath()`.

Calls to `getServletPath()` made outside the module are not affected. Here, value returned by the
servlet container itself is returned.

#### setContextPath attribute ####

Whether to set the context path in the request passed to servlets or filters in this module.
Setting this value has the effect of altering the values returned by calls to
`HttpServletRequest.getContextPath()`.

If the value for this attribute is set to `true`, and `contextPath` attribute
is not specified, then `getContextPath()` returns the value specified using the `path`
attribute. However, is a value is specified using the `contextPath` attribute is specified,
then this value is used.

Calls to `getContextPath()` made outside the module are not affected. Here, value returned by the
servlet container itself is returned.

#### contextPath attribute ####

If a value is set for this optional attribute,
then as long as `setContextPath` is not set with a value of false, then sets
the context path in the request passed to servlets or filters in this module.
In other words, the value specified using the `contextPath` attribute is returned
by calls from within the module to `getContextPath()`.

Calls to `getContextPath()` made outside the module are not affected. Here, value returned by the
servlet container itself is returned.

See this [more detailed explanation on the reasons for and usage of the attributes](WebMultiModulePaths.md) `servletPath`, `setServletPath`, `contextPath` and `serContextPath`.

### to-handler element ###

Used to map requests with a particular extension to a servlet and/or list of filters. For each request mapped to the module
using the `to-module` element, the `to-handler` element performs a second level mapping function within the module itself.

Note that a to-handler mapping is not required. If not present, then an implicit mapping will be created, using the following algorithm:
- Impala first looks for a servlet with the same name as the module name. If found, all requests are mapped to this servlet.
- If not found, Impala looks for a filter with the same name as the module. If found, all requests are mapped to this filter.
- If not found, Impala checks to see if there is exactly one servlet registered within the module. If so, then all requests
are mapped to this servlet.
- If no servlets are found, Impala checks to see if there is exactly one filter registered within the module. If so, then all requests
are mapped to this filter.
- If in either of the above cases more than one filter or servlet is found, an exception is thrown, since in this case it is not
possible to determine unambiguously what the filter or servlet mapping should be.

#### extension attribute ####

The file extension in the URI for a request mapped to the module using the `to-module` element.

For example, if the request URI is _/app/prefix/mypath.htm_
then the extension attribute required to match this URI this is _htm_.

Special values include:
  * `'*'`: denotes all extensions. Will match any URI mapped to the module using the to-module element.
  * `'[none]'`: used for URI's without file extensions. Will match a URI such as _/app/prefix/resourceWithoutExtension_.

#### servletName attribute ####

The name of a servlet registered within the module to which the request should be mapped. Can be omitted.

#### filterNames attribute ####

Comma-separated list of names of servlets registered within the module to which the request should be mapped. Can be omitted.


---


## Handler Elements ##

### filter element ###

Creates a `javax.servlet.Filter` who's life cycle is bound to that of the module.
An example is shown below:

```
<web:filter id = "characterEncodingFilter" 
	filterClass = "org.springframework.web.filter.CharacterEncodingFilter" 
	initParameters = "forceEncoding=true,encoding=utf8">
</web:filter>
```

#### filterClass attribute ####

The class of the filter declared using the `filter` element. There is no default for this attribute.

#### filterName attribute ####

The name of the filter declared using the `filter` element. Used to identify the filter when performing
URL to filter mappings. If not specified, the `id` attribute is used for this purpose.

#### delegatorFilterName attribute ####

This optional attribute is used if the registered filter is fronting a non-Spring MVC-based web framework. In this case,
a delegator filter can automatically be registered on this filter's behalf. This delegator performs some
additional functions required for integration with non-Spring MVC filters such as setting the thread
context class loader.

The name of the delegator filter is given using this attribute.

### servlet element ###

Creates a `javax.servlet.HttpServlet` who's life cycle is bound to that of the module. An example is shown below:

```
<web:servlet id = "urlmapping-resources" 
    servletClass = "org.springframework.js.resource.ResourceServlet"
    initParameters = "cacheTimeout=10"/>
```

#### servletClass attribute ####

The class of the servlet declared using the `servlet` element. Note that if not provided, will default to
`org.impalaframework.web.spring.servlet.InternalModuleServlet`.

#### servletName attribute ####

The name of the servlet declared using the `servlet` element. Used to identify the servlet when performing
URL to servlet mappings. If not specified, the `id` attribute is used for this purpose.

#### delegatorServletName attribute ####

This optional attribute is used if the registered servlet is not a Spring MVC servlet. In this case,
a delegator servlet can automatically be registered on this servlet's behalf. This delegator performs some
additional functions required for integration with non-Spring MVC servlets such as setting the thread
context class loader.

### Attributes used by both the `servlet` and `filter` elements ###

The `filter` and `servlet` elements define a common set of nested elements and attributes as described below.

#### id attribute ####

The id for this bean. If the servletName (for servlets) or filterName (for filters) attribute is not specified
then the id is used to specify the name of the servlet or filter, which is in turn used to identify the filter
in servlet or filter mappings.

#### factoryClass attribute ####

The `FactoryBean` class used to create and destroy the servlet or filter instance. If not specified, then
defaults to `org.impalaframework.web.spring.integration.ServletFactoryBean` (for servlets) or
`org.impalaframework.web.spring.integration.FilterFactoryBean` (for filters).

You won't need to specify a value for this property unless you need to override the default mechanism for
instantiating or destroying the servlet or filter instance.

#### initParameters attribute ####

Optionally, used to specify init parameters which are passed to the servlet or filter instance.
Has the same effect as specifying `<init-param>` entries in _web.xml_.

The initParameters attribute allows these init parameters to be specified in a concise way.
Individual entries are comma separated. Name and value pairs are separated by the equals sign.
For example:
```
initParameters = "name1=value1,name2=value2" 
```

sets two init parameters, with values `value1` and `value2`,
respectively.

Note that init parameters can alternatively specified using the contained `init-parameters` element.
If both the `init-parameters` element and the `initParameters` attribute are specified, and both contain the same attribute name,
then the definition in the `init-parameters` element takes precedence.

#### init-parameters element ####

Optionally, used to specify init parameters which are passed to the servlet or filter instance.
Has the same effect as specifying `<init-param>` entries in _web.xml_.

The `init-parameters` attribute allows init parameters using nested `param` elements.
For example:

```
<init-parameters>
	<param name = "name1" value = "value1"/>
	<param name = "name2" value = "value2"/>
</init-parameters>
```

Note that init parameters can alternatively specified using the contained `initParameters` attribute.
If both the `init-parameters` element and the `initParameters` attribute are specified, and both contain the same attribute name,
then the definition in the `init-parameters` element takes precedence.

The nested `param` element is used to specify an individual parameter with an `init-parameters` element declaration.
Its `name` attribute specifies the name of the init parameter, while the `value` attribute specifies the value for the init parameter.
