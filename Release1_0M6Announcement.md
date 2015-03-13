Impala 1.0M6 includes a full reworking of the Impala service registry implementation, as well as the mechanisms for exporting services to and
importing services from the service registry.

The headline improvements include the following.
  * Configuration of Impala services is now much simpler, as a new Spring `service` namespace has been provided for easily exporting and importing services.
  * Service export and import can now be done not only by name but also by type or by custom attributes, the latter using a model similar to that used in OSGi.
  * Impala's mechanism for proxying services obtained from the service registry has improved, and is now more easily configurable.
  * It is now possible to export and import Impala services without having to specify an interface - proxying of the service implementation class is now also supported.
  * Impala now supports exporting and importing services based on Spring beans which are not singletons or not created using non-singleton Factory beans. It does this in a way that is totally transparent to users of the services, effectively allowing clients to treat all beans as singletons.
  * Impala now provides dynamic `java.util.List` and `java.util.Map` implementations, backed by beans imported from the service registry.

For more details on how to import from and export to the service registry, see ImportingAndExportingServices.

The 1.0M6 release also introduces a number of improvements to make dynamic module reloading more robust, particularly when using the module _auto-reload_ feature.

See the [full list of issues covered for milestone 1.0M6](http://code.google.com/p/impala/issues/list?q=label:Milestone-Release1.0M6&can=1).

If you are upgrading from an earlier release, you will probably wish to check [the backward incompatible changes for 1.0M6](Compatibility_1_0M6.md).
Also, take a look at the [migration to 1.0M6](Migration_1_0M6.md) page.

If you like Impala and would like to support the project, please take a look at this page: http://code.google.com/p/impala/wiki/GetInvolved.