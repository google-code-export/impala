## Introduction ##

Impala is built to be extremely extensible. This is largely because the Impala runtime is itself is simply a set of Spring beans, but also because as much as possible it is designed with the goal of extensibility in mind. The code is highly factored, which makes it relatively easy to introduce new pieces of functionality or behaviour in a non-intrusive way.

Remember that much of Impala's built in behaviour can be configured using the Impala properties described in PropertyConfiguration. However, if existing behaviour does not meet your needs, you will probably need to add some extension to the framework.


---


## Impala's extension mechanism ##

Two points to remember:

**1) How setting Impala's Spring context locations differs from Vanilla Spring**

As mentioned, Impala is set up as a Spring application context, but remember that it is not configured in exactly the same way as a vanilla Spring application.

Remember that a vanilla Spring application is typically set up by specifying a list of Spring application context configuration files, in the order in which they should be loaded. This is usually done either programmatically, for example, using

```
new FileSystemApplicationContext(new String[]{"location1.xml", "location2.xml});
```

or using the `contextConfigLocation` init parameter in the _WEB-INF/web.xml_.

With Impala, the actual set of Spring locations used depends partly on the properties set in _impala.properties_, but also values set in the _extra.locations_ property. It is also possible to get full control of the list using the _all.locations_ property.

For more details on this see BootstrapContexts.

**2) How to make your own framework extension classes available**

Here, it is worth remembering that Impala makes quite a clear distinction between application code and third party libraries.

Application code is typically loaded by module class loaders rather than the application class loader (or system class loader). When deploying application modules in a web application, they are placed in _WEB-INF/modules_ rather than _WEB-INF/lib_.

Third party libraries, on the other hand, are loaded from _WEB-INF/lib_ using the application class loader, just like almost all other Java web applications out there.

The Impala framework classes are loaded using the application class loader as a third party library, not as a module.

So in order to extend Impala, what we want is a new project in our workspace which contains the classes that we are using to extend Impala. If we are using Impala's build system, then the convention is for any project containing the substring _extension_ to be built into a jar which is placed in _WEB-INF/lib_ rather than _WEB-INF/modules_. In this way, our framework extension extension classes become available to the application class loader (unlike module classes, which are only visible to class loaders for the relevant modules).


---


### Some extension examples ###

To be added.


