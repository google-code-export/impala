### Moved packages to eliminate package cycles (Ticket #88 - SVN 2698) ###

The following classes and interfaces have been moved to the package: `org.impalaframework.module`
  * `ApplicationContextLoader`
  * `DelegatingContextLoader`
  * `ModificationExtractor`
  * `ModificationExtractorType`
  * `ModuleStateChange`
  * `ModuleElementNames`
  * `TypeReader`
  * `ModuleLoader`
  * `TransitionSet`
  * `ModuleStateChangeNotifier`
  * `ModuleStateChangeListener`
  * `ModuleStateHolder`
  * `Transition`
  * `TransitionProcessor`

The following have been moved to `org.impalaframework.facade`
  * `InternalOperationsFacade`
  * `DefaultModuleManagementFacade`

The following has been moved to `org.impalaframework.module.definition`
  * `ModuleDefinitionAware`

The following to `org.impalaframework.module.loader`
  * `ModuleLoaderRegistryFactoryBean`

The following have been moved to `org.impalaframework.service`
  * `ContributionEndpoint`
  * `ContributionEndpointTargetSource`
  * `ServiceReferenceFilter`
  * `ServiceRegistry`
  * `ServiceRegistryReference`

The following has been moved to `org.impalaframework.service.registry.internal`
  * `ServiceRegistryImpl`

The following has been moved to `org.impalaframework.constants`
  * `LocationConstants`

Also, minor changes have been made to the internal structure of the Impala bean definitions, although this should
not affect average users.

**Action**: For snapshot revisions from SVN 2692, will need to apply these package names to remove compile errors.


---


### More convenient default test `ModuleDefinitionSource` (Ticket #90) ###

Partly because of the removal of package cycles, partly because of [issue 90](http://code.google.com/p/impala/issues/detail?id=90)
test classes that use `InternalModuleDefinitionSource` will need to change:

Code such as the following will no longer work:

```
public RootModuleDefinition getModuleDefinition() {
	return new InternalModuleDefinitionSource(new String[] { "example-dao", "example-hibernate" }).getModuleDefinition();
}
```

**action**: replace this code with the following:

```
public RootModuleDefinition getModuleDefinition() {
	return new TestDefinitionSource("example-dao", "example-hibernate").getModuleDefinition();
}
```

Note that the new code is simpler because the name of the test class is more intuitive,
but also because it allows the module names to be specified using a varargs array.

**Action**: For snapshot revisions from SVN 2692, will need to apply these package names to remove compile errors.


---


### Moved packages to simplify package structure (Ticket #88) ###

The following classes and interfaces have been moved to the package: `org.impalaframework.file`
  * `FileMonitor`
  * `RootPathAwareFileFilter`
  * `FileRecurser
(SVN 2744)

Package `org.impalaframework.service.registry.contribution` renamed to `org.impalaframework.service.contribution`.
Package `org.impalaframework.service.registry.event` renamed to `org.impalaframework.service.event`.
Package `org.impalaframework.service.registry.filter` renamed to `org.impalaframework.service.filter`.
(SVN 2746)


---


### No longer supporting multiple projects for root module (Ticket #99) ###

[Issue 21](http://code.google.com/p/impala/issues/detail?id=21) introduces the capability of defining modules as a graph, which
is a much more powerful and flexible way of achieving the same result.

In SVN 3147, the system property `impala.root.projects` has been removed.

This change also affects _moduledefinitions.xml_. Here, the
`name` element should be used instead of `root-project-names`. For example:

```
<parent>
	<root-project-names>
		<name>project1</name>
		<name>project2</name>
	</root-project-names>
	<context-locations>
		<context-location>parentTestContext.xml</context-location>
		<context-location>extra-context.xml</context-location>
	</context-locations>
</parent>
```

becomes

```
<parent>
	<name>project1</name>
	<context-locations>
		<context-location>parentTestContext.xml</context-location>
		<context-location>extra-context.xml</context-location>
	</context-locations>
</parent>
```

### Removed application\_with\_beansets module type (Ticket #107) ###

This type of module has been deprecated and the supporting code has been moved to the sandbox.

If you are using this module type (for example, using the entry `type=application_with_beansets`) in module.properties,
you will need to remove this entry. If in doubt as to whether you're using this feature, then you're almost certainly not!