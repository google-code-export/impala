# Approach #

Some aspects about the philosophy and approach behind Impala:
  * it aims to be consistent with best practices in Spring application development, such as separation of concerns, dependency management.
  * it encourages test-driven development, in particular through the interactive test client.
  * it is does not impose any programming model requirements. You don't need to use any special APIs to work with Impala. It's simply a matter of setting up the environment for your project correctly. The rest is taken care of.
  * it applies the principle of convention over configuration.
  * it should be fast to build, run and tests
  * it works well with multi-tiered as well as simpler web-based architectures
  * it works in a Java-only environment, which means you don't need to give up on tool support, refactoring and compile time type checking offered by Java.
  * of course, it does not prevent the use of scripting languages.