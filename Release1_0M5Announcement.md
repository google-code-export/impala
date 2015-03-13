Impala 1.0M5 introduces a number of API and configuration improvements, making the framework easier to configure and extend,
and usable in a wider range of environments. Following 1.0M5, only minor changes in internal APIs are now expected prior to the 1.0 final release.

The 1.0M5 release makes it much easier to configure Impala-based applications by
supporting a [property-based configuration](PropertyConfiguration.md).
While Impala is still very heavily based on the Spring framework, 1.0M5 now
also makes it possible to plug in other runtime frameworks into Impala's dynamic module loading mechanism.

The full list of issues for milestone 1.0M5 is here:  http://code.google.com/p/impala/issues/list?q=label:Milestone-Release1.0M5&can=1.

Note that there are a number of package name and configuration changes in this release.
If you are upgrading from an earlier release, you will probably wish to check [the backward incompatible changes for 1.0M5](Compatibility_1_0M5.md) and [an example migration sequence for this release](Migration_1_0M5.md).

If you're interested in getting involved in the Impala project, please take a look at this page: http://code.google.com/p/impala/wiki/GetInvolved.