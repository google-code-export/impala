I am pleased to announce the 1.0.3 release of Impala.
Partly a maintenance release to bring Impala fully up to date to latest versions of Spring.
While the core of Impala is very stable, the new release
contains a few useful new features to help users of the project in development and in the field.

## Selectively reloadable modules ##

With Impala 1.0.3 it is now possible to turn on module reloadability selectively.
In other words, some but not all of the modules in an application can be set to reloadable.

The advantage of this feature is that it opens up the possibility of supporting safer
reloadability of certain modules in a production environment. Modules that may require reloading
while an application is running can be set to reloadable, while core modules that should remain static can be set to non-reloadable.
This allows for partial update of selective elements of a running application without disruption to users.

## File system module resources ##

Impala web application resources are typically packaged for production in module-specific jars.
On occasions, a requirement exists to change resources (such as web templates) packaged as part
of the application without doing a full software build and release.
Prior to 1.0.3, replacing any module-specific resource would require the to be rebuilt, which is a little awkward.
With Impala 1.0.3, the resource loading mechanism has been modified to first look
in a module-specific file system location, if such a location is present. This supports a simpler mechanism for updating or 'hot fixing'
resources in an application, without the overhead of a new software release.

## Spring 3.2.2 support ##

Impala has been updated to work out of the box with the latest version of Spring, currently 3.2.2.
Impala is now also compiled using Spring 3.2.

## Java 5 Generic compliance ##

Changes have been made in the Impala source code to support Java 5 generic compliance where this support wasn't present.

## Comment syntax ##

With 1.0.3, the Impala module definition file supports a comment syntax, making it easier to comment against entries in this file.





