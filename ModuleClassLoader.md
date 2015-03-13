The main class loader used in Impala is [GraphClassLoader](http://impala.googlecode.com/svn/trunk/impala/impala-core/src/org/impalaframework/classloader/graph/GraphClassLoader.java)
which takes that name because it supports dependencies between modules arranged in a graph, rather than simply a hierarchical or tree-style relationship.

## An example ##

Impala's Graph class loader allows you to express dependencies between modules in the form of a graph. What does this mean.

### Hierarchical relationships ###

Suppose you have a root module A, which has children B and C. In this case
  * The classes in module A will be visible to classes in modules B and C.
  * However, the classes in B will not be visible to the classes in C, and vice versa.
  * Also, the classes in B will not be visible to A, and neither will the classes in C be visible to A.

What I describe above is a hierarchical relationship, shown below:

![http://impala.googlecode.com/svn/wiki/images/modules-hierarchy.png](http://impala.googlecode.com/svn/wiki/images/modules-hierarchy.png)

It allows class loader relationships to be expressed on a parent child basis.

### Graph-based relationships ###

While this offers a fair amount of flexibility, it is not really sufficient for many real world applications.

Suppose we introduce a new module D, which we would like to be a child of B. However, suppose we would like
some of the classes that we define in D to also depend on classes in C. With a simple parent to child or hiearchical relationship,
this is not possible. Instead, we need a graph based relationship, which allows classes in individual modules to have
visibility of classes from multiple dependent modules.

![http://impala.googlecode.com/svn/wiki/images/modules-graph.png](http://impala.googlecode.com/svn/wiki/images/modules-graph.png)

## Class loading ##

### Current Module ###

Whether the class is found will depend on what the **current module** is. So what do we mean by 'current module'. If a module is in the process of being
loaded (for example, if a Spring application context is created), then the current module is the one being loaded. Once the application is running
the current module may take a couple of different meanings. For a web application processing an HTTP request, the current module will initially be the
one to which the request is first directed. However, the request may then involve calls to code defined in other modules.

Impala will change the thread's context class loader so that it reflects the module processing the request. This means that if code in the
module needs to load classes after the module has loaded, it will be able do so using the same class loader which loaded the code in the first place.

### Load order ###

Suppose you attempt to load the class named `D` which is contained within the module D. If the current module is D, then Impala
will first attempt to load the module using the class loader associated with the root module A.
Failing to find it there, it will look in B, then C, and then in D.

What if we are currently in module B, and attempt to load class `D`. In this case, it will only look in modules A and B.
It will fail to find the class, and throw a `ClassNotFoundException`.

### Loading third party libraries ###

Our example above only considers loading of a module class. What about third party classes, for example, contained in _WEB-INF/lib_ in a web
application, or in the Java standard libraries?

These classes will be loaded in exactly the same way as in standard standalone or web applications, following the standard class loader
delegation model. For convenience we talk about these classes being present on the system class path.

All that remains to be determined is how the third party class loading mechanism and module class loading mechanisms fit together.
Which will be searched first?

Actually, this depends on the Impala property `parent.classloader.first`. If this is set to true, then Impala will first delegate to the system/web application
class loader, only using the module class loader if unable to find the class. If set to false, Impala will first use the module class loader, only then delegating
to the system/application class loader if the class is not found.

So why is this option available, and what are its consequences?

The reason why this option is available is that at times it is necessary to run Impala in environments where module classes are also on the system class path. If class loading was always delegated first to the system class loader, then it would not be possible to reload these modules. In this case, the module class path is searched first, then the system class path.

So when can this cause problems? The answer is if modules contain classes which are also present in third party jars. In this case, which of the classes gets loaded will depend on whether `parent.classloader.first` is set true or false. Obviously, this is an undesirable situation - but in general it should not be a problem. In order to protect against this situation, it is important that modules classes are contained in packages which follow a well defined naming convention which prevents these types of clashes from occurring.

## Resource loading ##

Apart from loading classes, the other main purpose of class loaders is to load resources on the class path.

Typically, this is done via two methods defined in `java.lang.ClassLoader`:
  * `public URL getResource(String name)`
  * `public Enumeration<URL> getResources(String name)`

These two methods tend to serve their own set distinct set of use cases within an application, including an Impala application.

In both cases, there is a key difference between resource loading and class loading. With class loading, it is normally to
delegate to parent class loader first, only loading classes locally if parent (or dependent) class loaders cannot find the class.
With resource loading, it works better in reverse. If you are looking for a particular resource, you are better off first attempting to
find it locally, only delegating to parent or dependent class loaders if unable to find it locally.

### URL getResource(String) ###

`getResource(String)` is normally used to load a specific resource, such as a configuration file or a view template.
Impala's graph-based class loader does this by attempting to load the class locally. It will then delegate it to other module class loaders,
which will in turn attempt to find the resource locally. Only if none of the module class loaders are able to find the resource within
any of the local module class paths is the search passed on to the system class loader.

The question then is in what order is the search delegated to modules? The answer is in the exact reverse of the order in which
the module class paths are searched when loading classes.

### Enumeration getResources(String) ###

`getResources(String)` is used in a Spring application for two distinct purposes.
  * first, to find certain configuration files. For example, `getResources(String)` is used to locate pluggable Spring configuration XML namespace and schema handlers.
  * secondly, to enable class path scanning, for example, to auto-detect and read Spring bean definitions from classes which have been marked with Java 5 annotations such as `@Service`, `@Component` and `@Controller`.

The use of `getResources(String)` for these two purposes creates slightly conflicting requirements for Impala's `getResources` implementation:
  * for the first, `getResources` needs to be able to search very widely across the class path. For example, the XML namespace handler configuration files are derived from all the files on the class path named _META-INF/spring.handlers_. In this case, the files will need to be found in Spring and Impala library jars, and possibly others.
  * for the second, `getResources` really only needs to search locally. For example, when scanning for component definitions which should be loaded for the current module, we really want to restrict our search to the current module's local class path. Classes which reside in other modules can in turn be found using class path scanners set up in the Spring configuration files for these modules.

In order to address both these requirements, Impala uses the following strategy in it's `getResources(String)` implementation:
  * Impala first gets an `Enumeration` of local `URLs` from the current class path.
  * It then adds these to the `Enumeration` of `URLs` obtained from calling the system class loader's `getResource(String)` implementation.

In other words, Impala will not delegate a `getResources(String)` call to other modules in the application. However, it will delegate it to
the system class loader.

There are a couple of implications for this approach which should be borne in mind:
  * if you need to new namespace handlers and other similar types of resources to be found using `getResources(String)`, these should be packaged in a third party library, and not in a module. For more information on how this is done, see `ExtendingImpala`.
  * you can safely use class path scanners in your application without having to worry about whether this will result in beans from the current module being instantiated by dependent modules. It is advisable to make sure that all classes in a particular module share a common base package not used by any of your third party libraries. Using this approach can make it simple and efficient to set up class path scanning for your application.



