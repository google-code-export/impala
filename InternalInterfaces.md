## Introduction ##

As a casual user of the framework you will rarely need to use the interfaces described below. However, when extending Impala, or developing Impala,
and occasionally when debugging, knowledge of these interfaces can be very useful indeed.

As background for this discussion, it is helpful to bear in mind the processes and operations involved when loading and managing modules.

Note that these interfaces are provisional and may still be changed prior to the final 1.0 release.

## Service Registry Interfaces ##

### [ServiceRegistry](http://impala.googlecode.com/svn/trunk/impala/impala-core/src/org/impalaframework/service/ServiceRegistry.java) ###

Represents the mechanism for sharing services between modules. Beans can be exported to the registry, which results in a bean
being registered by name (not necessarily the name of the source bean) or by one or more export types. Beans can also be exported with
custom attributes.

When a bean is registerd, a `ServiceRegistryEntry` instance is created.

Once present in the registry, beans can be retrieved from the registry, again either by name, by one or more export types, or
via a filter based on custom attributes set during registration. Methods are available either to retrieve just a single
service or to retrieve a collection of services. In these cases, `ServiceRegistryEntry` instances or collections will be returned.

Note that `ServiceRegistry` is actually a sub-interface of two other interfaces -
`ServiceEntryRegistry`, which contains methods used to registry and retrieve services, and
`ServiceEventListenerRegistry`, which is used to register and deregister listeners to
service events, generated for example when services are added or removed.

### [ServiceRegistryEntry](http://impala.googlecode.com/svn/trunk/impala/impala-core/src/org/impalaframework/service/ServiceRegistryEntry.java) ###

Represents a service present in the the `ServiceRegistry`. Contains information on the export name, types and custom attributes,
as well as a handle which can be used to retrieve the service bean instance. The latter is available through the `ServiceBeanReference` interface.

### [ServiceBeanReference](http://impala.googlecode.com/svn/trunk/impala/impala-core/src/org/impalaframework/service/ServiceBeanReference.java) ###

Represents a service present in the registry. For singleton services, a the implementation of `ServiceBeanReference` will
simply statically wrap the singleton instance. For non-singletons (e.g. prototypes, non-singleton `FactoryBeans`)
registered from Spring-based modules,
the `ServiceBeanReference's` `getService` method will involve a dynamic lookup from the contributing module's `ApplicationContext`.

`ServiceBeanReference` has a `isStatic` method which can be used to determine whether the contained reference is static or not.

### [ServiceRegistryEventListener](http://impala.googlecode.com/svn/trunk/impala/impala-core/src/org/impalaframework/service/ServiceRegistryEventListener.java) ###

A `ServiceRegistryEventListener` receives notifications of changes made to the service registry. The key changes are
when services are added to the registry and when services are removed from the registry.

`ServiceRegistryEventListeners` are used, for example, to keep service-registry backed lists up to date.
They are used, for example for services imported via the `import` element with registered attributes.
See ImportAndExportingServices for more details.

### [ServiceEndpoint](http://impala.googlecode.com/svn/trunk/impala/impala-core/src/org/impalaframework/service/ServiceEndpoint.java) ###

A `ServiceEndpoint` is the target of an import from a service registry. Application code does not use this interface directly.
See ImportingAndExportingServices for more details.

### [NamedServiceEndpoint](http://impala.googlecode.com/svn/trunk/impala/impala-core/src/org/impalaframework/service/ServiceEndpoint.java) ###

A subclass of `ServiceEndpoint` where the target imports the service by name from the service registry.

## Module Interfaces ##

### [ModuleDefinition](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/ModuleDefinition.java) ###

In Impala, a module has two parallel representations. One is the runtime module hierarchy, which consists of a hierarchy of Spring `ApplicationContext`s and
their corresponding class loaders. The second is a metadata representation. The key interface here is `ModuleDefinition`.
`ModuleDefinition` exposes a number of methods which return information on the module, such as `name`, `type` and the identity of parent and child modules.

The hierarchy of `ModuleDefinition` can be considered an abstract representation of the module hierarchy, which can be manipulated separately from the
module hierarchy itself.

At the root of the a hierarchy of module definitions is an instance of `RootModuleDefinition`, which exposes another couple of methods specific to root modules.

### [ModuleDefinitionSource](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/ModuleDefinitionSource.java) ###

This interface defines a strategy for loading module definitions.
The module definition hierarchy can be loaded by any implementation of `ModuleDefinitionSource`.
There are a number of implementations of `ModuleDefintionSource`. Which implementation is best to use depends on the circumstances.
`XmlModuleDefinitionSource` uses by default an _moduledefinitions.xml_ placed on the web application class loader's class path (for example, in _WEB-INF\classes_).
For integration tests, it's easier to implement `ModuleDefinitionSource` in code the test directly. Here's an example:

```
public class EntryDAOTest implements ModuleDefinitionSource {
  ...
 
  public RootModuleDefinition getModuleDefinition() {
	return new TestDefinitionSource("example-dao", "example-hibernate").getModuleDefinition();
  }
}
```

The example above uses `TestDefinitionSource`, which involves a passed in a vararg array of names of modules.

### [RuntimeModule](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/RuntimeModule.java) ###

While `ModuleDefinition` encapsulates an Impala definition at the metadata level, `RuntimeModule` encapsulates
the runtime representation of a module which has been loaded. In the case of Spring modules, the implementation is an instance of `SpringRuntimeModule`
which will be backed by a Spring `ApplicationContext` instance.

## Generic SPI Interfaces ##

### [ModuleRuntime](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/spi/ModuleRuntime.java) ###

`ModuleRuntime` is a coarse grained interface which contains method for loading and unloading modules on behalf of a particular module runtime
implementation. The default implementation is `SpringModuleRuntime` which is used to load and unload Spring `ApplicationContext`-based modules.

### [ModuleLocationResolver](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/resolver/ModuleLocationResolver.java) ###

`ModuleLocationResolver` is responsible for determining where to find modules. Impala supports a number of deployment configurations, such as standalone deployment of jar files,
deployment of modules as jar files in a web application's _WEB-INF/modules_ directory, and deployment directly from the _bin_ directory of an Eclipse project on the
file system. Each of these scenarios requires a different strategy for module loading, and hence a different implementation of `ModuleLocationResolver`.

### [TypeReader](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/spi/TypeReader.java) ###

Impala features the concept of a module **type**. Examples include root modules, application modules, servlet modules, etc.
The idea behind `TypeReader` is that each of module types will require its own strategy for reading metadata about the modules.

Type readers support a mechanism for reading module type information from XML (in the form of an `org.w3c.dom.Element` instance) or properties (for example read
from a properties file.

### [ModuleLoader](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/spi/ModuleLoader.java) ###

Another interface which hangs of module types is `ModuleLoader`. Just as different types of modules need to have their metadata read (by `TypeReader`s),
different types of modules also require different strategies for loading. This covers a range of aspects:
  * provision of a `ClassLoader` for the module.
  * what kind of Spring application context is constructed. For example, web module types need to be backed by instances of `WebApplicationContext`.
  * what `BeanDefinitionReader` is used to extract the bean information.

`ModuleLoader` encapsulates differences that exist between module types in these respects. Module types backed by a `ModuleLoader`
will observe some restrictions on how they are constructed and initialized, with the benefit of reducing the responsibilities of module loader implementations.
However, if these restrictions are inappropriate or cannot be observed, it is possible to allow a module to be backed by a `DelegatingContextLoader`.

### [ModuleLoaderRegistry](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/loader/ModuleLoaderRegistry.java) ###

A class which holds a mapping between module types and either a `ModuleLoader`.

### [ModificationExtractor](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/spi/ModificationExtractor.java) ###

Recall that we have a duality in the module implementation between a module definition, and the runtime instantiation of a module hierarchy.
The job of a `ModificationExtractor` is to determine what changes are required to the current module hierarchy (which at pre-load time will be empty),
and the desired module hierarchy as determined by a hierarchy of `ModuleDefinitions`.

For example, suppose module A is the root module and it's child, module B, is loaded. Suppose also that change the desired module hierarchy so that
module A will still be loaded, but it will have just one child, module C. The actions that need to take place are:
  * B must be unloaded
  * C must be loaded

The job of the `ModificationExtractor` is to determine what set of changes are required from any existing module hierarchy to an arbitrary new hierarchy.
The advantage of the use of the `ModificationExtractor` is that it is possible to edit a module configuration hierarchy offline, then apply it in one go,
without having to work out individually what are the module changes required to effect this change.

### [ModificationExtractorRegistry](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/modification/ModificationExtractorRegistry.java) ###

It is possible to have multiple active `ModificationExtractors`. For example, when running a suite of integration tests, the `ModificationExtractor` used
will not unload modules which are not in use for a particular test. However, in a web application, modules which are loaded but not part of the desired
or target hierarchy _will_ be unloaded. These different strategies are represented by different _ModificationExtractor_ implementations, which can be set up in the
same `ModificationExtractorRegistry`.

### [TransitionProcessor](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/spi/TransitionProcessor.java) ###

A `TransitionProcessor`'s job is to finish the job started by the `ModificationExtractor`. For example, if the `ModificationExtractor` determines that
module B should be unloaded and module C should be loaded, it is job of an 'unload' transition processor to perform the unload operation,
and a 'load' `TransitionProcessor` instance to perform the module load operation.

The 'load' operation is by default implemented by the`LoadTransitionProcessor` which, in turn,
delegates most of the module loading job to the `ApplicationContextLoader`. It also has some additional housekeeping tasks to perform relating to
recording module state.

### [TransitionProcessorRegistry](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/transition/TransitionProcessorRegistry.java) ###

A registry of `TransitionProcessor`s, keyed on the operation they are supposed to perform.

### [TransitionManager](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/spi/TransitionManager.java) ###

`TransitionManager` handles the mechanism of calling the `TransitionProcessor` instances and collection the results of each operation in a `TransitionResultSet` instance.

### [ModuleStateHolder](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/spi/ModuleStateHolder.java) ###

The main job of the `ModuleStateHolder` is to maintain information on the state of modules loaded. This includes references to
the `ApplicationContext`s which back the modules, as well as to the currently loaded `ModuleDefinition` hierarchy.

## Spring Module Runtime Implementation ##

### [ApplicationContextLoader](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/spring/module/ApplicationContextLoader.java) ###

`ApplicationContextLoader` is responsible for the overall task of loading a module-specific `ApplicationContext` given a `ModuleDefinition` and parent.
`ApplicationContextLoader` is not module type-specific. It has a broader responsibility. The default implementation, `DefaultApplicationContextLoader`
uses the appropriate type specific instance of `ModuleLoader` or `DelegatingContextLoader` to perform the actual module load.

### [DelegatingContextLoader](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/spring/module/DelegatingContextLoader.java) ###

`DelegatingContextLoader` is used as an alternative strategy for module loading to `ModuleLoader`.

Implementations of `DelegatingContextLoader` can be used to back a Spring module for types of modules for which the restrictions which
apply for `ModuleLoader` implementations is not appropriate. For example, there is a _web\_placeholder_ module type, which is backed by an empty instance of
`GenericWebApplicationContext`, to cover servlet modules which are present in _web.xml_ but not actually backed by any real module functionality.

`DelegatingContextLoader` could also be used for integrating with Impala specific frameworks which use a Spring application context in a very specialized way,
for example, through their own subclass implementations of `WebApplicationContext`. An example might be a framework such as Grails.

## Facade Interfaces ##

### [ModuleOperation](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/operation/ModuleOperation.java) ###

`ModuleOperation` is an abstraction which models user initiated operations on modules. Examples include:
  * Reload a particular named module
  * Reload a module with a name like X
  * Remove a named module
  * Add bean definitions to the root module
  * Shut down the entire module hierarchy
  * etc.

`ModuleOperation` is a high level interface which can used by clients to get a particular job done. `ModuleOperations`
implementations use many of the interfaces described above to perform their tasks.

### [ModuleOperationRegistry](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/operation/ModuleOperationRegistry.java) ###

`ModuleOperationRegistry` is the access point for `ModuleOperation`s, allowing a `ModuleOperation` to retrieved by operation name.

### [ModuleManagementFacade](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/facade/ModuleManagementFacade.java) ###

An internal interface which is used to access instances of other Impala-specific interfaces, many of them documented above. Also extends `BeanFactory` so that it can access any Spring-configured Impala bean directly.

## Module Update Interfaces ##

### [ModuleChangeMonitor](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/monitor/ModuleChangeMonitor.java) ###

`ModuleChangeMonitor` is used optionally to implement modification detection and automatically reload modules.
For the _web-listener-bootstrap_ [Impala configuration](WebApplicationBootstrapping.md), a file system polling mechanism is used.
The alternative module reload trigger is currently a JMX console. An administration console is also planned.

### [ModuleStateChangeNotifier](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/spi/ModuleStateChangeNotifier.java) ###

Defines a contract for `ModuleStateChangeListener`s to subscribe to changes in a module's state
(for example, a module becoming stale and requiring a reload). Implements the Observer Pattern.

### [ModuleStateChangeListener](http://code.google.com/p/impala/source/browse/trunk/impala/impala-core/src/org/impalaframework/module/spi/ModuleStateChangeListener.java) ###

Classes implementing `ModuleStateChangeListener` and subscribing to `ModuleStateChangeNotifier` can be notified of module state changes events.

## Web Interfaces ##

### [RequestModuleMapper](http://code.google.com/p/impala/source/browse/trunk/impala/impala-web/src/org/impalaframework/web/integration/RequestModuleMapper.java) ###

This interface defines a mechanism for directing requests to modules.