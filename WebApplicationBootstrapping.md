## Simple Startup ##

For vanilla Spring web applications, using a `ContextLoader` instance set up using a `ContextLoaderListener` in the web application's _web.xml_.

For Impala, the mechanisms is similar with a `ContextLoaderListener` subclass being used:

```
<listener>
	<listener-class>org.impalaframework.web.loader.ImpalaContextLoaderListener</listener-class>
</listener>
```

Impala startup works very similarly to regular Spring applications. The big difference is that the `ContextLoader` subclass does not bootstrap the application directly. Instead, it bootstraps the Impala runtime (itself just a set of Spring beans), which itself if responsible for loading the modules which comprise your application.

## Advanced Startup ##

With Impala, it's straightforward to vary the `ContextLoader` subclass which is used to bootstrap Impala in a web environment. Simply
add an extra entry into _web.xml_.

```
<context-param>
	<param-name>contextLoaderClassName</param-name>
	<param-value>my.context.loader.subclass.name</param-value>
</context-param>	
```

If this element is not present, the default is assumed to be `ExternalModuleContextLoader`.

Changing the `ContextLoader` is appropriate if you want to modify the actual mechanism for bootstrapping Impala in a web application.
If you are happy with this mechanism, but you want to vary the details of the Impala runtime configuration, then you have two choices:

  * [Using the built in configuration options](PropertyConfiguration.md).
  * [Setting or adding Spring XML configuration files (context locations) used to bootstrap Impala](BootstrapContexts.md).