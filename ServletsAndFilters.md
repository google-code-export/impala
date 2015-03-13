### Servlets ###

Impala provides a number of servlets which can be used in your application, depending your usage scenario.

#### `org.impalaframework.web.servlet.ExternalModuleServlet` ####

This is an extension of Spring MVC's `DispatcherSerlvet`. Use this servlet when
  * You are using Spring MVC as your web framework.
  * You are happy to declare the servlet in _web.xml_.

An example is shown below, taken from the [examples sample](SamplesExample.md):

```
<servlet>
	<servlet-name>petclinic-web</servlet-name>
    <servlet-class>org.impalaframework.web.servlet.ExternalModuleServlet</servlet-class>
	<load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
	<servlet-name>petclinic-web</servlet-name>
	<url-pattern>*.htm</url-pattern>
</servlet-mapping>
```

When `ExternalModuleServlet` starts up. It will 'attach' itself to the module corresponding with the servlet name, in this case _petclinic-web_.
The Spring application context it references will be updated each time the module reloads.


---


#### `org.impalaframework.web.servlet.InternalModuleServlet` ####

Like `ExternalModuleServlet`, `InternalModuleServlet` is an extension of Spring's `DispatcherServlet`. One key difference
is that it is configured within a module (not _web.xml_), and is hence internal to the module.

Use this Servlet if:
  * You are using Spring MVC as your web framework.
  * you want to avoid coupling your module with _web.xml_.

For example, if you want to design your application in such a way that the _web.xml_ does not need to have any knowledge of
other web modules, you can use this technique. An example configuration is shown below, taken from the module _example-servlet2_ in
the [example samples](SamplesExample.md) application:

```
<bean class="org.impalaframework.web.integration.ServletFactoryBean">
	<property name = "servletName" value = "example-servlet2"/>
	<property name = "servletClass" value = "org.impalaframework.web.servlet.InternalModuleServlet"/>
</bean>	
```

Actually, it's much easier to configure this using the `servlet` element from the
Impala `web` namespace:

```
<web:servlet id = "example-servlet2" 
  servletClass = "org.impalaframework.web.spring.servlet.InternalModuleServlet"/>
```

or, since `InternalModuleServlet` is the default servlet class if none is specified, simply:

```
<web:servlet id = "example-servlet2"/>
```

Notice how `ServletFactoryBean` is used to configure, and also to provide life cycle calls, for the servlet instance.

Another key difference with `ExternalModuleServlet` is that it is instantiated and destroyed each time the module
reloads, rather than just once for the lifetime of the application.

You may be wondering how the an `InternalModuleServlet` gets to receive requests. After all, there is no entry in
_web.xml_, and hence no servlet mapping. That is the role of `ModuleProxyServlet`, which meet in the next section.
However, in order to be 'found' by `ModuleProxyServlet`, the `InternalModuleServlet` instance will on startup be registered
a `ServletContext` attribute with a module-specific name.


---


#### `org.impalaframework.web.integration.ModuleProxyServlet` ####

We saw an example usage of `ModuleProxyServlet` earlier on this page. The role of `ModuleProxyServlet` is to forward
requests to servlets such as `InternalModuleServlet` and `InternalFrameworkIntegrationServlet`, which we meet later.
It implements a simple scheme to fulfil this role:

  * it examples the first segment of the servlet path, that is, the portion of the path which comes after the context path on the URL.
> For example, if the URL is `http://localhost:8080/myapp/myserlvet/somecontroller`, the `ModuleProxyServlet` will by default look in the
> `ServletContext` for a module servlet (say, an instance of `InternalModuleServlet`).
  * if it finds this entry, it will forward the request to this servlet.

An example configuration from the [examples sample](SamplesExample.md) application's _web.xml_ is shown below.

```
<servlet>
    <servlet-name>example-redirector</servlet-name>
    <servlet-class>org.impalaframework.web.spring.integration.ModuleProxyServlet</servlet-class>
    <init-param>
            <param-name>modulePrefix</param-name>
            <param-value>example-</param-value>
    </init-param>
    <load-on-startup>2</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>example-redirector</servlet-name>
    <url-pattern>*.to</url-pattern>
</servlet-mapping>
```


---


#### `org.impalaframework.web.integration.ExternalFrameworkIntegrationServlet` ####

Like `ExternalModuleServlet`, `ExternalFrameworkIntegrationServlet` needs to be registered in _web.xml_. The big difference is that
it's fundamental role is to function as a bridge between the web application and a non-Spring MVC servlet which exists in a module.

The configuration of this type of servlet consists of two elements. First, there is the registration of `ExternalFrameworkIntegrationServlet`
in _web.xml_. This example is from the module _example-servlet4_ in
the [example samples](SamplesExample.md) application:

```
<servlet>
    <servlet-name>example-servlet4</servlet-name>
    <servlet-class>org.impalaframework.web.integration.ExternalFrameworkIntegrationServlet</servlet-class>
    <load-on-startup>4</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>example-servlet4</servlet-name>
    <url-pattern>*.do4</url-pattern>
</servlet-mapping>
```

Secondly, there needs to be an entry for the Servlet to which requests will be delegated, which is in the module's
Spring configuration file _example-servlet4-context.xml_.

```
<bean id = "delegateServlet" class="org.impalaframework.web.integration.ServletFactoryBean">
	<property name = "servletName" value = "delegateServlet"/>
	<property name = "servletClass" value = "servlet.SomeFrameworkServlet"/>
	<property name = "initParameters">
		<map>
			<entry key="controllerClassName" value = "servlet.ServletControllerDelegate"/>
		</map>
	</property>
</bean>
```

Notice how the `ServletFactoryBean` is used to declare the non-Spring MVC Servlet. By default, the `ExternalFrameworkIntegrationServlet`
looks for a servlet named `delegateServlet` to forward the request to.

This configuration is suitable when:

  * you want to or are happy to use the _web.xml_ to declare the path mapping to the servlet.
  * you want to use Impala's dynamic reloading capability for an application not using Spring MVC.


---


#### `org.impalaframework.web.integration.InternalFrameworkIntegrationServlet` ####

In the same way that `ExternalModuleServlet` has a sister servlet in the form of `InternalModuleServlet`,
`ExternalFrameworkIntegrationServlet` has a corresponding `InternalFrameworkIntegrationServlet`.

Like `InternalModuleServlet`, `InternalFrameworkIntegrationServlet` is declared in the module's Spring configuration file,
is instantiated per module reload, and receives requests via a `ModuleProxyServlet`.

And like `ExternalFrameworkIntegrationServlet`, `InternalFrameworkIntegrationServlet` is used as a bridge into a
non-Spring MVC servlet.

The [web frameworks sample](SamplesWebframework.md) uses `InternalFrameworkIntegrationServlet` for all of the examples. The
setup for Struts is shown below:

```
<bean class="org.impalaframework.web.integration.InternalFrameworkIntegrationServletFactoryBean">
	<property name = "servletName" value = "webframeworks-struts"/>
	<property name = "servletClass" value = "org.impalaframework.web.integration.InternalFrameworkIntegrationServlet"/>
	<property name = "delegateServlet" ref = "delegateServlet"/>
</bean>	

<bean id = "delegateServlet" class="org.impalaframework.web.integration.ServletFactoryBean">
	<property name = "servletName" value = "delegateServlet"/>
	<property name = "servletClass" value = "struts.ReloadableActionServlet"/>
	<property name = "initParameters">
		<map>
			<entry key="config" value = "/WEB-INF/struts-config.xml"/>
		</map>
	</property>
</bean>
```

Using the Impala `web` namespace, the above definitions can be condensed to the following:

```
<web:servlet id = "myServlet" 
	delegatorServletName = "webframeworks-struts"
	servletClass = "struts.ReloadableActionServlet" 
	initParameters = "config=/WEB-INF/struts-config.xml"/>
```

Notice how the `InternalFrameworkIntegrationServlet` is set up using an `InternalFrameworkIntegrationServletFactoryBean`.
The delegate servlet is set up using the usual `ServletFactoryBean`. (The use of `struts.ReloadableActionServlet` instead of
the usual Struts `ActionServlet` is to fix a minor bug in Struts 1.2.9 which prevents requests from reloading).

### Impala Filters ###

For integrating with web frameworks which provide Servlet-based entry points, `InternalFrameworkIntegrationServlet` or `ExternalFrameworkIntegrationServlet`
should be adequate. Some frameworks, however, only provide `Filter` based entry points. An example is Tapestry 5. Impala provides a couple of
filters to cater for these requirements.


---


#### `org.impalaframework.web.integration.InternalFrameworkIntegrationFilter` ####

This usage of this class is very similar to `InternalFrameworkIntegrationServlet`, except that it works with filters rather than servlets, and
passes through a `FilterChain` to the delegate filter. An example configuration is shown below, taken from the Tapestry 5 module in the
[web frameworks sample](SamplesWebframework.md).

```
<bean class="org.impalaframework.web.integration.InternalFrameworkIntegrationFilterFactoryBean">
	<property name = "filterName" value = "webframeworks-tapestry5"/>
	<!-- this is a subclass of InternalFrameworkIntegrationFilter -->
	<property name = "filterClass" value = "tapestry5.PathModificationIntegrationFilter"/>
	<property name = "delegateFilter" ref = "delegateFilter"/>	
	<property name = "initParameters">
		<map>
			<entry key="prefix" value = "/tapestry5"/>
		</map>
	</property>
</bean>	

<bean id = "delegateFilter" class="tapestry5.Tapestry5FilterFactoryBean">
	<property name = "filterName" value = "delegateFilter"/>
	<property name = "filterClass" value = "org.apache.tapestry5.spring.TapestrySpringFilter"/>
	<property name = "applicationPackage" value = "tapestry5.application"/>
</bean>
```

Using the Impala `web` namespace, this definition can be condensed to:

```
<web:filter id = "myFilter" 
	delegatorFilterName = "webframeworks-tapestry5"
	factoryClass = "tapestry5.Tapestry5FilterFactoryBean"
	filterClass = "org.apache.tapestry5.spring.TapestrySpringFilter" 
	initParameters = "prefix=/tapestry5">
       <property name="applicationPackage" value="tapestry5.application"/>
</web:filter>
```


---


#### `org.impalaframework.web.integration.ModuleProxyFilter` ####

Just an `InternalFrameworkIntegrationServlet` can be 'found' and passed requests by `ModuleProxyServlet`, `ModuleProxyFilter`
performs a similar role for `InternalFrameworkIntegrationFilter`.

It uses the same mechanism to look up an instance of `InternalFrameworkIntegrationFilter` from the `ServletContext` using a
module-specific key, and passes the request to the filter if found. One subtle point worth noting about the way it uses a filter chain.
Instead of passing the real filter chain to the delegate filter, it instead passes an instance of `InvocationAwareFilterChain`.
`InvocationAwareFilterChain` has a `getWasInvoked` method which will return true if the filter was invoked.

When the `InternalFrameworkIntegrationFilter` has finished processing the request, `ModuleProxyFilter`  will check this method.
If it return true, the real `FilterChain` is invoked. This ensures that the next invocation in the filter chain does not occur in within the module
itself.

An example configuration from the [URL mapping sample](SamplesURLMapping.md) is shown below:

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