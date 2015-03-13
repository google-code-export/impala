Impala 1.0 RC2 is the second 1.0 release candidate. It is anticipated that there will be one more interim release (1.0 RC3) prior to the 1.0 final release.

Impala 1.0 RC2 contains a mixture of feature enhancements and bug fixes, most notably:
  * support for [builds using Maven](Maven.md).
  * simplifications to the default project structure, making it more intuitive and Maven-friendly.
  * a [dynamic properties](NamespaceDynamicProperties.md) namespace, making it easier to support properties which can be injected and [dynamically updated](DynamicProperties.md) without requiring module or application reload.
  * further refinements to the mechanisms for supporting multi-module web applications.
  * support for Spring 3.0.
  * various other minor feature enhancements, refactorings and bug fixes.

See the [full list of issues covered for milestone 1.0 RC2](http://code.google.com/p/impala/issues/list?q=label:Milestone-Release1.0RC2&can=1).

See also the Maven sample application, including the [Maven sample](SamplesMaven.md), which uses a new Maven plugin for Impala and shows how Maven can be used to build Impala applications as an alternative to Impala's ANT-based build system.

If you are migrating from a previous version, please take a look at the [1.0 RC2 migration guide](Migration_1_0RC2.md).

If you like Impala and would like to support the project, please take a look at this page: http://code.google.com/p/impala/wiki/GetInvolved.