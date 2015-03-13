This page describes in a little detail how path issues are handled in multi-module web applications.
We begin by considering how paths are interpreted in standard servlet based web applications, and then we turn our attention to how
Impala handles some of the trickier points raised by the presence of multi-module web applications.

## Standard Web Applications ##

The key methods we are interested in are defined in the `HttpServletRequest` interface:

  * `getRequestURI()`: denotes the URL path for the request, excluding the query String as well as the host name, port, and scheme. It comes from the first line of the HTTP request.
  * `getContextPath()`: the part of the request URI which is used to map the request to a particular web application within the servlet container.
  * `getServletPath()`: denotes the portion of the URL used to match a servlet or filter, typically derived from the request mapping in _web.xml_ which is used to match the request to a handler.
  * `getPathInfo()`: the part of the URL which follows the servlet path up to but not including the query string.

The important thing to note is that the values returned from these methods (especially `getServletPath()` and `getPathInfo()`) depend not only on the URL, but on the way that the URL is mapped to
servlets or filters within the application, typically in _web.xml_.

Consider for example the URL: `http://localhost:8080/myapp/user/newuser.htm`.

### Wildcard extension match ###

```
<url-pattern>*.htm</url-pattern>
```

In this case, the request is matched to a servlet or filter using the `.htm` extension. Here `getServletPath()` will be `/user/newuser.htm`, and the path info will be null.

### Partial path match ###

```
<url-pattern>/user/*</url-pattern>
```

In this case, the request is matched to a servlet or filter using a portion of the URI. Here `getServletPath()` will be `/user`, and the path info will be `/newuser.htm`.

### Global wildcard match ###

```
<url-pattern>/*</url-pattern>
```

In this case, the servlet path is the full URI after the context path; in other words `/user/newuser.htm`. Again, `path info` is null, in this case.

## Multi-web module Impala applications ##

The difficulty with multi-web module applications is that the servlet container is no longer entirely responsible for mapping the request to the
filter or servlet handler. The recommended mapping in web.xml is `/*`, with requests then mapped to modules using the
`to-module` element, as shown below.

```
<web:mapping>
	<web:to-module prefix = "/user"/>
	...
</web:mapping>
```

The use of this scheme raises a complication: the values for `getServletPath()` and
and `getPathInfo()` may not be appropriate in all cases.

For example, servlets which are responsible solely for serving resources (for example, the Spring JS `ResourceServlet`), may rely on
the path info to determine the resource path, but using the wildcard `/*` mapping, the path info will be null.

Another example is with JSPs. JSPs use both the servlet path and the path info to resolve JSP files. This means that the JSP contained within a module
will always need to exist within a folder tree corresponding with the servlet path. So if we are mapping to the Impala `user` module using
the prefix `/user`, then the JSP for the URI `/user/jsps/user.jsp` will need to be placed within the folder `/user/jsps`. This is a little
unnatural - ideally, we would like to be able to place the JSP simply in the `/jsps` folder within the 'user' module.

For these reasons, Impala makes it possible to influence the `getServletPath()` and `getPathInfo()` values returned to
handlers within the module. In addition, it also allows for the `getContextPath()` value to be modified. The precise mechanics of this are
described next.

## Setting path values within the module ##

The `to-module` element gives you the opportunity to influence the context path, the servlet path or simply use the values passed in via the servlet container.

### Setting the context path ###

To modify the context path, the following configuration can be used:

```
<web:mapping>
	<web:to-module prefix = "/user" setContextPath="true"/>
	...
</web:mapping>
```

which has the same effect as

```
<web:mapping>
	<web:to-module prefix = "/user" contextPath="/user"/>
	...
</web:mapping>
```

In the example of the URL `http://localhost:8080/myapp/user/newuser.htm`.

The context path will be `/myapp/user/', the servlet path will be an empty string, and the path info will be `/newuser.htm`.

### Setting the servlet path ###

Setting the servlet path can be done using the configurations:

```
<web:mapping>
	<web:to-module prefix = "/user" setServletPath="true"/>
	...
</web:mapping>
```

which has the same effect as

```
<web:mapping>
	<web:to-module prefix = "/user" servletPath="/user"/>
	...
</web:mapping>
```

In both cases, the context path will remain `/myapp`. The servlet path will be set to '/user', and the path info will be `/newuser.htm`.

### Usage recommendations ###

If the module is being used to host JSPs, then the `setContextPath` or `contextPath` elements is helpful, because it allows JSPs to be hosted
in folders other `/user` in our example. If the module is being used to host resources such as CSS files, images, etc, then either the
context path or servlet path attributes can be used to allow for a more suitable value for `getPathInfo()` to be used.

For URLs mapping to application controllers with frameworks such as Spring MVC and Struts,
using the context path or servlet path attributes can allow application resources to be matched
using a more concise path mapping. For example, if using Spring MVC with  `servletPath="/user"` or `contextPath="/user"`, an annotated controller method
could use a path such as `/newuser.htm`. Without either of these entries, the controller would need to use the full servlet path value: `/user/newuser.htm`.

Note that Java web frameworks have their own idiosyncrasies when mapping request URLs to handlers. In some cases, a little experimentation, or even minor enhancements, may required to support
new frameworks. For some frameworks, it may not be possible to use the context path or servlet path attributes.
Note however that even when this is the case, it is still possible to map requests to a single module via multiple
prefixes (through multiple `to-module` element entries),
with different mappings using the context path and servlet path setting attributes in different ways (or not at all).

### Forwards and includes ###

When a request is dispatched within the container via a forward or include, the servlet container will manipulate the servlet path and path info
as necessary. It will also set request attributes such as `javax.servlet.forward.request_uri` and `javax.servlet.include.request_uri`.
Forwards and includes are primarily used with JSPs, but can be used for other servlets.

Impala does not provide support for influencing the values of the context path, servlet path and path info during an include. Instead, it detects that
a forward or include is in process, and falls back to the values provided by the servlet container. In order to properly handle forwards and includes,
Impala would need to provide full blown implementations of the `RequestDispatcher` interface, currently outside the scope of the project.
It is still possible to use forwards and includes with Impala, but with some restrictions. For example, when hosting JSPs within modules,
the servlet `org.impalaframework.web.jsp.ModuleJspServlet` needs to be registered within the _web.xml_,
as shown in the [web frameworks sample](SamplesWebframework.md).







