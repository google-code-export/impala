# Development Modes #

Impala aims to be consistent with best practices in Spring application development. It encourages test-first development. It is does not impose any programming model requirements, and works well with multi-tiered as well as simpler web-based architectures. It works in a Java-only environment, which means you don't need to give up on tool support, refactoring and compile time type checking offered by Java. Of course, it does not prevent the use of scripting languages.

Impala runs in two modes, a **test mode** and a **web mode**.

**Test mode** provides an interactive environment for running JUnit tests:

  * there is no build step. You Spring context is automatically deployed, and changes are reflected automatically
  * you can rerun tests without reloading application contexts.
  * only the part of the application that you modify reloads. That means that reloading is dramatically quicker. For example, if you are working on Hibernate DAOs, only the DAO beans need to reload, not the Hibernate session factory. For large applications, this can lead to a dramatic reduction in the build/deploy/test cycle time
  * reloading can either be in the background or user initiated
  * the full application context only reloads if interfaces of shared/domain classes change
  * it is very easy to identify the parts of the application which need to be loaded for individual tests

**Web mode** involves starting the application with an embedded [Jetty](http://www.mortbay.org/) server. It has the same benefits as test mode. Again, there is no build step. Changes are automatically picked up. Here the feel is more "Rails-like", although without any real restrictions in the programming model.

Web applications can also be deployed as wars (with individual modules contained in separate jar files) and run in servers such as [Tomcat](http://tomcat.apache.org/). There is also a deployment format which takes advantage of the Jetty's "embeddability", allowing for web applications to be deployed without having to pre-install a web servlet container.