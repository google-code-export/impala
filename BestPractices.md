# Introduction #

Below we've listed some recommendations which will hopefully help you to get the most out of
Impala and avoid some of the pitfalls arising from working an a multi-module environment.

## Modules ##

### Use prefix for all module names ###

It's a good idea to have a common prefix for all module names within the same
application. This is for the simple reason that if modules from more than one application
are included in the same workspace, there will a) be no name clashes between modules,
and b) it will be less confusing.

### Go for relative fine module granularity ###

One of the big design decisions you will need to make when working with a modular system
is what should be your module granularity. How big should the modules be? How many classes
or bean definitions should a module contain?

The answer to the question should really be along the following lines: does the module
contain any separate pieces of functionality which are not inherently related, that don't
necessarily belong together. If so, then you should strongly consider splitting this functionality
into separate modules.

Don't be afraid to start with modules that seem very small. They will tend to grow over time.
Also, it is probably easier to join modules (bringing their content together) than to split them
apart.

### Use package naming convention based on module name ###

Impala doesn't enforce any package naming convention for classes within modules, but it's a good
idea to use one. Our recommendation is as follows.

  * pick a base package which will be used for all modules in the application
  * make the base package for each module the prefix package plus a name which depends on the module. Exclude the common module prefix, if you are using one.

So if your base package is `com.application`, and you have three modules named `myapp-main`, `myapp-module1` and `myapp-web`,
then your module base packages should be `com.application.main`, `com.application.module1` and `com.application.web`.

Using a package naming convention will help you to avoid split packages, and adds a layer of self-documentation to your code.

### Pull down from root module where possible ###

Don't make the mistake of putting all your functionality in the root module. If you do this, you will end up with effectively just a single module
with all its tangled inter-dependencies.

Impala gives you the power to provide a true separation between interface and implementaion, so that only interfaces (and shared domain classes)
need to be in higher level modules. Implementations can be kept in lower level modules and made available to other parts of the application
through the service registry.

### Actively refactor module locations ###

Just as you regularly need to move code between classes and packages in a single module application, you will also need to
move classes between modules in a multi-module application. This may seem a pain, but it is an important part of keeping
your code base in a healthy state.

## Web ##

### Keep _web.xml_ small ###

Impala encourages you to modularise the web tier of your application by allowing you to
divide your web tier into multiple modules. The idea is that new web modules can come and go without
requiring any changes in your _web.xml_.

Unless you are planning to use only a single web module, our recommendation is that you
register your dispatcher servlets in the modules themselves, using `InternalModuleServlet` or
`InternalFrameworkIntegrationServlet` in the module, and `ModuleProxyServlet` and/or `ModuleProxyFilter`
in _web.xml_. `ModuleProxyServlet` and `ModuleProxyFilter` will delegate calls to the correct module
servlet based on a scheme which by default will use the 'servlet path' (first part of the URL after the
servlet context name) as the module name. You can customise this mechanism: see ImpalaWebConfiguraiton.

If you rely on `ExternalModuleServlet` and `ExternalFrameworkIntegrationServlet`, both of which
must be registered in _web.xml_, then you will need restart your application to add a new module, and
it will be a little harder to dynamically configure which web modules are included in your application.

### Make all session objects serializable ###

Keeping session objects serializable is generally recommended to allow for cross VM session replication.
Impala also uses a custom serialization mechanism to preserve session state between module reloads
if the original class loader for the object is no longer valid.
This mechanism relies on session objects being serializable.

### Prefer templating which does not tie you to servlet context directory ###

Templating languages such as velocity and freemarker can easily be set up so that they
read resources off the class path. This allows them to be kept within the module, rather
than in a shared location under _WEB-INF_. The end result is web modules which are more
self-contained.

## Resource Loading ##

### Be explicit in use of class loader ###

It is worth being explicit in the use of class loader when loading resources and dynamically
loading classes, particularly if these classes or resources are lazily loaded after the initial
module reload. Spring makes it easy to access the class loader used at module load time using the `BeanClassLoaderAware`
interface. Use this, rather than relying on the thread context class loader.

If you are tied to using libraries which dynamically load classes after the initial module load, then
these may rely on the thread context class loader being correctly set. You can ensure that this
happens for each invocation by setting the Impala property `proxy.set.context.classloader`
and the servlet init parameter `setThreadContextClassLoader` (for any servlet extending `BaseImpalaServlet`) to _true_.
For `ExternalFrameworkIntegrationServlet`, this is set to true by default.

## Deployment ##

### Consider using Jetty standalone deployment ###

The `StartServer` class which is used to run Impala in an embedded Jetty container has an analogue which
can be used in production environment. As well as building a WAR file, Impala's build environment supports
building a standalone zip file which contains the embedded Jetty container as well as scripts necessary
to start the application.

Using this allows you to deploy your application without having to pre-install an application server. This
can be a bonus.

## Coding ##

### Eliminate package cycles ###

If you have package cycles in your code, it is a strong indication that your packages structure
will benefit from some refactoring. If you have package cycles, it will not be possible, for example,
to move either of the affected packages to a different module.