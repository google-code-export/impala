Impala is a dynamic module system, which uses class loaders to
enforce module boundaries as well as to allow dynamic reloading of parts of an application.
For this reason, class loaders are an important feature of Impala
and an understanding of their use is important.

This page describes the use of class loaders within Impala.

## Class Loader Taxonomy ##

The following are the types of class loaders which may be used in a dynamic module system.

  * **Shared class loader**: here, all modules share the same class loader. In this case, all public classes are visible from all modules within the application. I'm stating this case for the sake of completeness.
  * **Hierarchical class loader**: type visibility is inherited from module parents. In other words, classes have visibility of public classes within the module as well as those visible to the parent module.
  * **Graph class loader**: visibility is inherited from parents as well as dependencies. In other words, if module B has parent module A and also depends on module C, then classes in A will have visibility of public classes in both module B and C.
  * **OSGi class loader**: class loader model which applies when OSGi runtime is used. The OSGi class loader model adds to the capabilities of the graph class loader model - it is the most sophisticated class loader model supported in Impala.

Each of these class loader types is supported in Impala (although OSGi support is only experimental at this stage), enabling you to choose the class loader arrangement which suits the requirements of your application or environment.

The default class loader type is the [Graph class loader](ModuleClassLoader.md), which as it supports a much more flexible set of module configurations than a hierarchical class loader.

## Graph Class Loader ##
