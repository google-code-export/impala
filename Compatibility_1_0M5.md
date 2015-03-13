Impala 1.0M5 features a number of backward incompatible changes which are described in detail below. Also, you can consult the [example set of steps used to migrate from an earlier version](Migration_1_0M5.md).


### Reduced unnecessarily coupling with Spring framework (Ticket #117 ) ###

`RuntimeModule` interface added. `ModuleStateHolder` methods

```
ConfigurableApplicationContext getRootModuleContext();
ConfigurableApplicationContext getModule(String name);
Map<String, ConfigurableApplicationContext> getModuleContexts();
void putModule(String name, ConfigurableApplicationContext context);
ConfigurableApplicationContext removeModule(String name);
```

have been renamed to

```
RuntimeModule getRootModule();
RuntimeModule getModule(String name);
Map<String, RuntimeModule> getRuntimeModules();
void putModule(String name, RuntimeModule context);
RuntimeModule removeModule(String name);
```


**Area affected:** Service provider implementation classes.


---


`ApplicationContextLoader` moved from `org.impalaframework.module` to `org.impalaframework.module.spring`.

**Area affected:** Service provider implementation classes.


---


`ModuleLoader` interface split into `ModuleLoader` and `SpringModuleLaoder`.
Implementation classes moved to `org.impalaframework.module.spring.loader`.

**Area affected:** Service provider implementation classes.


---


Package `org.impalaframework.bean` has been moved to `org.impalaframework.spring.bean`.


**Area affected:** Clients using classes in old `org.impalaframework.bean` may be affected (e.g `StringFactoryBean`).


---


Moved Spring-related classes from dynamic property framework to `org.impalaframework.spring.config`:

```
DynamicPropertiesFactoryBean.java
DynamicPropertySource.java
ExternalDynamicPropertySource.java
```

**Area affected:** Clients using dynamic property framework may be affected.


---


Split `ModuleLoaderRegistry` into `ModuleLoaderRegistry` and `DelegatingContextLoaderRegistry`. The `DelegatingContextLoaderRegistry`
holds `DelegatingContextLoaders`, which are Spring-specific.

Moved `DelegatingContextLoade` interface to `org.impalaframework.module.spring`.

**Area affected:** Service provider implementation classes.


---


Moved `TypeReaderRegistryFactoryBean` to `org.impalaframework.module.spring.type`.

**Area affected:** Service provider implementation classes.


---


All service exporter classes have been moved to `org.impalaframework.spring.service.exporter`. These include:

```
ModuleArrayContributionExporter.java
AutoRegisteringModuleContributionExporter.java
BaseModuleContributionExporter.java
ModuleContributionPostProcessor.java
ModuleContributionUtils.java
ServiceArrayRegistryExporter.java
ServiceRegistryExporter.java
```

**NOTE: this change is likely to break backward compatibility for most clients.**

**Area affected:** Impala-export definitions in Spring configuration file.


---


`ContributionProxyFactoryBean` moved from `org.impalaframework.spring.module` to
`org.impalaframework.spring.service.proxy`.

**NOTE: this change is likely to break backward compatibility for most clients.**

**Area affected:** Impala-export definitions in Spring configuration file.


---


Package `org.impalaframework.module.spring` renamed to `org.impalaframework.spring.module`.
Applies for subpackages.

**Area affected:** Mostly service provider implementation classes.


---


Impala modules no longer expressed directly in terms of Spring application context. It instead exposes a `ModuleRuntime` object which could be a `SpringModuleRuntime` instance.

Hence the following methods have changed:

```
public static ApplicationContext getRootContext()
public static ApplicationContext getModuleContext(String moduleName)
```

```
public static RuntimeModule getRootRuntimeModule()
public static RuntimeModule getRuntimeModule(String moduleName)
```

**Area affected:** Service provider implementation classes.


---


Package `org.impalaframework.web.config` renamed to `org.impalaframework.web.spring.bean`.

Affects: `SystemPropertyServletContextParamFactoryBean`.

**Area affected:** Clients using this class may be affected.


---


Spring related classes in `org.impalaframework.web.integration` have been moved to `org.impalaframework.web.spring.integration`.

Affects:
```
ExternalFrameworkIntegrationServlet.java
FilterFactoryBean.java
InternalFrameworkIntegrationFilter.java
InternalFrameworkIntegrationFilterFactoryBean.java
InternalFrameworkIntegrationServlet.java
InternalFrameworkIntegrationServletFactoryBean.java
ModuleProxyFilter.java
ServletFactoryBean.java
ModuleProxyServlet.java
```

**NOTE: this change is likely to break backward compatibility for clients using Impala's web framework integration functionality.**

**Area affected:** Impala servlet/filter definitions in Spring configuration files for web modules. Also _WEB-INF/web.xml_.


---


Package `org.impalaframework.web.loader` renamed to `org.impalaframework.web.spring.loader`.

This affects context loader related classes such as ImpalaContextLoaderListener.

**NOTE: this change is likely to break backward compatibility for web clients.**

**Area affected:** _WEB-INF/web.xml_.


---


Module loader related classes in `org.impalaframework.web.module` moved to `org.impalaframework.web.spring.module`.

Include:

```
RootWebModuleLoader.java
WebModuleLoader.java
WebPlaceholderModuleDefinition.java
WebRootModuleLoader.java
```

**Area affected:** Service provider implementation classes.


---


Moved `org.impalaframework.web.servlet` to `org.impalaframework.web.spring.servlet`.

Includes:

```
BaseImpalaServlet.java
ExternalModuleServlet.java
InternalModuleServlet.java
```

**NOTE: this change is likely to break backward compatibility for web clients.**

**Area affected:** Impala servlet/filter definitions in Spring configuration files for web modules. Also _WEB-INF/web.xml_.


---


---


### ModuleDefinition and RootModuleDefinition should be moved to org.impalaframework.module (Ticket #131 ) ###

`ModuleDefinition`, `ModuleDefinitionSource` and `RootModuleDefinition` have been moved from
`org.impalaframework.module.definition` to `org.impalaframework.module`.

**NOTE: this change is likely to break backward compatibility in integration tests for most clients.**

**Area affected:** Integration Tests


---


---


### Rename context-locations to config-locations ###

The property/XML element `context-locations` and `context-location` have been
renamed to `config-locations` and `config-location`, respectively.

Also renamed the method `ModuleDefinition.getContextLocations()` to `ModuleDefinition.getConfigLocations()`.

**NOTE: this change is likely to break backward compatibility for modules which were using
_context-locations_ to explicitly set module Spring context locations.**

**Area affected:** `modules.properties` and `moduledefinitions.xml`.


---


---


=== Created new SPI package `org.impalaframework.module.spi` (Ticket #135)

The following interfaces are now part of new spi subpackage, moved from `org.impalaframework.module`.

```
ModificationExtractor.java
ModificationExtractorType.java
ModuleElementNames.java
ModuleLoader.java
ModuleRuntimeManager.java
ModuleStateChange.java
ModuleStateChangeListener.java
ModuleStateChangeNotifier.java
ModuleStateHolder.java
Transition.java
TransitionProcessor.java
TransitionSet.java
TypeReader.java
```

**Area affected:** this change is likely to affect Impala extension subclasses. Regular clients will not be affected.


---


---


### Package names in Impala project should follow convention (Ticket #136) ###

The following projects are affected:

  * `impala-build`: renamed build ANT classes from `org.impalaframework.ant` to `org.impalaframework.build.ant`.
  * `impala-command`
  * `impala-interactive`
  * `impala-jmx`

**NOTE: this change is likely to break backward compatibility in integration tests for clients using the interactive test runner.**

**Area affected:** the class `InteractiveTestRunner` is now in the package `org.impalaframework.interactive`.


---


---


### Remove inconsistencies in ModuleDefinitionSource class and package names (Ticket #137) ###

The names of a number of `ModuleDefinitionSource` implementations have been changed:

```
BasePropertiesModuleDefinitionSource.java
IncrementalPropertiesModuleDefinitionSource.java
InternalPropertiesModuleDefinitionSource.java
```

renamed from

```
BasePropertiesModuleBuilder.java
IncrementalPropertiesModuleBuilder.java
InternalPropertiesModuleBuilder.java
```

Renamed to
```
WebXmlRootModuleDefinitionSource.java
```

from

```
WebXmlRootDefinitionBuilder.java
```

Also, package renamed from `org.impalaframework.module.builder` to `org.impalaframework.module.source`.
Moved all web `ModuleDefinitionSource` implementations to `org.impalaframework.web.module.source`

**Area affected:** service provider implementation classes.


---


---


### Make Impala config property names more consistent (Ticket #143) ###

[Issue 143](https://code.google.com/p/impala/issues/detail?id=143) establishes a convention for Impala-specific configuration properties.

**Area affected:** The property `impala.adaptor.jmx.port` has been renamed to `jmx.adaptor.port`.

**Note: Clients are encourage to use the named property style of configuration, rather than explicitly setting Impala Spring bootstrap context locations.**

See WebApplicationBootstrapping for more details.