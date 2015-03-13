Impala 1.0M6 introduces changes to the recommended way of exporting and importing services from the Impala service registry.
Compatibility issues are mostly concerned with this. However, support for the existing mechanisms has not been removed - instead it has been deprecated,
but will be removed before 1.0 final.

### New Impala service namespace - [issue 195](https://code.google.com/p/impala/issues/detail?id=195) ###

`ContributionProxyFactoryBean`: prior to 1.0M6 this class is used to access beans from the service registry.
In 1.0M6 it is still possible to use this class in the same way, but usage is logged with quite severe health warnings.

Instead, it is recommended to use the `import` element from the Impala `service` namespace. See ImportingAndExportingServices.


---


`ModuleContributionPostProcessor`: prior to 1.0M6 this class is used to automatically export named beans to the service registry.
In 1.0M6 it is still possible to use this class in the same way, but usage is logged with quite severe health warnings.

Instead, it is recommended to used on of the export elements from the Impala `service` namespace. See ImportingAndExportingServices.

### Removal of previously deprecated methods - [issue 197](https://code.google.com/p/impala/issues/detail?id=197) ###

As of 1.0M6 it is no longer possible to configure the Impala Spring config locations using the
Impala property `bootstrapLocations`. Use the properties `all.locations` or `extra.locations` instead.
