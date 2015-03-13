This page describes module reloading in Impala, but also gives a bit more background on how modules are represented.

## Module Representation ##

I'll start by explaining what form modules take in Impala, both during development and at runtime.

At **development time**, you can think of a module as an Eclipse project, which will import into it's build path the
other modules on which it depends.

Typically, it will contain classes as well resources. Significant among these will typically be a _module.properties_
file, which contains metadata about the module, and one or more Spring application context definition files,
containing bean definitions.

An example is shown below:

![http://impala.googlecode.com/svn/wiki/images/url_mapping.png](http://impala.googlecode.com/svn/wiki/images/url_mapping.png)

For more details on the structuring of modules, see ModuleStructure.

A module can be deployed from a folder on the file system (for example, the _bin_ directory of your Eclipse project)
or from a jar in your web application (typically, contained in the _/WEB-INF/modules_ directory).
In both cases, the folder or jar will contain the resources present in your Eclipse class path, with Java files compiled, of course.

At **runtime** a module, has two representations: as metadata, in the form of a `org.impalaframework.module.ModuleDefinition` instance,
and in a form usable by the application, in the form of a `org.impalaframework.module.RuntimeModule`. Each module will be
backed by a class loader, and in the case of Spring modules, by an `ApplicationContext` instance.

## Module Reloading ##

Here, module reloading is a term I am using rather loosely to cover all operations you might want to initiate, including
adding a new module, removing a module, changing the definition or configuration of an existing module, or simply reloading an existing module.

So what happens when you initiate one of these operations:

**Firstly**, the metadata for your new module structure you wish to apply is determined. In the case of a simple reload operation, the
metadata itself will be unchanged.

**Secondly**, the differences between the module structure metadata and the new module structure metadata are used to calculate a sequence
of individual module operations.

For example, suppose your application consists of loaded modules A, B, C and D, with the following structure:

  * A is the parent of B and C
  * D is the child of B but depends on C

Suppose also that you have module E which is also a child A, but is not loaded.

Now suppose your new module metadata structure involves the addition of E, but the removal of C. The following sequence of operations will be determined:

  * D is to be unloaded because it is a child of C
  * C is to be unloaded since this was explicitly requested
  * E is to be loaded

Suppose, for the same initial starting point, module A was explicitly reloaded, without any metadata change. The sequence of operations that would result would be
  * unload D
  * unload C
  * unload B
  * unload A
  * load A
  * load B
  * load C
  * load D

Impala will automatically unload and then reload dependency of any modules you explicitly choose to load, and do so in the correct order.

**Finally**, module load operations are applied. In the case of unloading modules, the life cycle methods will result in the closing of
Spring application contexts, and the discarding of class loaders associated with the unloaded modules.
Beans previously exported to the service registry will be removed.

In the case of the load operation, a new class loader is created for the module concerned, and a new Spring application context is
created to back the module. Services will be exported to the service registry as configured in the application context definition.

