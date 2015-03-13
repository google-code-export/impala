Impala 1.0M4 brings two major new features:
  * the ability to arrange modules in a graph instead of just a hierarchy or tree.
  * preliminary support for OSGi.

Support for graphs of modules and their corresponding class loaders will offer application developers much more flexibility in choosing the
appropriate module structure for applications.

In offering OSGi support, Impala promises to become the first and only project to promise a seamless transition to (and from) OSGi.
There is no intention to make OSGi the default deployment model for Impala. However, OSGi promises to become a good choice
in the future for projects which will benefit from more rigorous management of third-party libraries than is possible using the traditional model.

Support for OSGi in 1.0M4 is in the following form:
  * All Impala jars are now OSGi compliant bundles.
  * It is now possible to run Impala and its application modules in an OSGi container, specifically Eclipse Equinox.

At this stage Impala support for OSGi is still experimental. It is not recommended to use it in a production environment.

Further improvements to Impala's support for OSGi are expected in the forthcoming releases. These include:

  * The ability to run Impala's Interactive Test Runner within an OSGi container. See [issue 121](http://code.google.com/p/impala/issues/detail?id=121).
  * Support for running full test suites within OSGi (currently only running single test at a time is supported). See [issue 120](http://code.google.com/p/impala/issues/detail?id=120).
  * Support for switching easily between OSGi- and non-OSGi-mode when running integration tests. See [issue 123](http://code.google.com/p/impala/issues/detail?id=123).
  * Web application support. See [issue 122](http://code.google.com/p/impala/issues/detail?id=122).
  * Support for OSGi containers other than Eclipse Equinox. See [issue 124](http://code.google.com/p/impala/issues/detail?id=124).

The full list of issues for milestone 1.0M4 is here:  http://code.google.com/p/impala/issues/list?q=label:Milestone-Release1.0M4&can=1.

If you are upgrading from an earlier release, you may wish to check for [non-compatible changes](Compatibility.md).

If you're interested in getting involved in the Impala project, please take a look at this page: http://code.google.com/p/impala/wiki/GetInvolved.