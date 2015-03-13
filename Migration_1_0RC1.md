A recommended migration task for users moving from Impala 1.0 RC1 from earlier versions is to use the
new Spring [web namespace handler](WebNamespaceHandler.md), instead of using the factory bean equivalents.

Backward incompatible changes made from 1.0M6 to 1.0 RC1 include:

### [Issue 209](https://code.google.com/p/impala/issues/detail?id=209) ###

In _impala.properties_, the property that was named `spring.path.mapping.enabled` has been changed to `top.level.module.path.enabled`
as this is a more correct descriptive name. Do a search and replace to correct this.

### [Issue 228](https://code.google.com/p/impala/issues/detail?id=228) ###

Some of the functionality now present in the WebModuleBaseHandlerRegistration was previously present in a `RequestModuleMapper` implementation
called `TopLevelPathContainsModuleMapper`. This functionality has been removed.

### [Issue 230](https://code.google.com/p/impala/issues/detail?id=230) ###

One of the module types in 1.0M6 was `servlet`. This module type has been renamed to `web` in Impala 1.0 RC1.