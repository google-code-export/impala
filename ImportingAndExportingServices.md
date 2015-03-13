Impala uses a service registry to share services (ordinarily, Spring beans) between modules. The service registry is a central registry
shared by the whole application to which services can be dynamically added and removed. Clients of the service registry can look up services
using a number of mechanisms, and can also register as listeners for service events.

It is very necessary for application code to interact directly with the service registry. Instead, you can take advantage of a number
of Impala Spring beans which are most easily configured using the Impala `service` namespace, new with Impala 1.0M6. The `service` namespace
provides a number of XML elements which you can use in your Spring application context XML file to export beans to and import beans from the
service registry. It also takes care of the details of setting up proxies to imported services. These proxies manage the dynamic interactions with the
service registry, meaning that the complexity of these interactions is not imported into your application.

For details on the elements available, see the [Impala service namespace reference](NamespaceReferenceService.md)