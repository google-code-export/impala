Impala includes a number of Spring namespaces which make it easier to configure the Impala-specific aspects of
your Spring application when working with Impala.

  * [service namespace](NamespaceReferenceService.md)
> Used for exporting and importing services from the service registry and other convenience elements for managing Spring bean references.
  * [web namespace](NamespaceWebReference.md)
> Used for defining web module mappings as well as servlets and filters used within modules.
  * [dynaprop (dynamic properties) namespace](NamespaceDynamicProperties.md)
> Used for setting up dynamic properties, allowing for on the fly configuration updates to injected properties without requiring module reloads.