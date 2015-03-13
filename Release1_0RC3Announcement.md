Impala 1.0 RC3 is the final release candidate prior to the official 1.0 release. If no significant issues are raised against this released, it is envisaged that there will
be virtually no changes in the subsequent 1.0 final release.

This release contains a number of enhancements:
  * simplifications making it easier to migrate existing Spring-based projects to use Impala. With [issue 293](http://code.google.com/p/impala/issues/detail?id=293), it is now possible to run Impala as part of an existing virtually unchanged traditional Spring web application.
  * support for JSPs deployed within individual web modules. Prior to 1.0 RC3, JSPs need to be contained within the web application folder. With 1.0 RC3 it is now possible to completely modularise the web elements of a JSP-based web application. [More info](HowToConfigureJSPs.md)
  * support load time weaving of aspects created using AspectJ. [More info](HowToConfigureLoadTimeWeaving.md)
  * various other minor features and bug fixes.

See the full list of [issues covered in this release](http://code.google.com/p/impala/issues/list?can=7&q=label%3AMilestone-Release1.0RC3).

With Impala 1.0 RC3, released artifacts are  available in the Maven central repository for the first time. See http://repo1.maven.org/maven2/org/impalaframework/.

If you like Impala and would like to support the project, please take a look at this page: http://code.google.com/p/impala/wiki/GetInvolved.