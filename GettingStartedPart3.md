# Testing with Impala #

Impala has a heavy focus on making it easy to practice Test Driven Development (TDD) when working with Spring applications. An important aspect of the Impala environment is in the area of testing - essentially, making them easier to write, and quicker to run.

## Unit tests ##

For the purposes of the discussion which follows, unit tests are considered to be tests on classes without requiring a Spring application contexts. Impala imposes no restrictions on the definition and running of unit tests - they work just like in any other Java project. The interesting areas is that of integration tests.

## Integration tests ##

### The example test ###

The pain point in this area tends to be integration tests, for a number of reasons. Integration tests are more complicated that unit tests, because they involve more dependencies. Separating out the dependencies required for the test from those which are not can be hard at times. Also, because integration tests are slower to run, slow develop/deploy/test cycle times can be an obstacle to productivity.

Apart from the modules and their dynamic reloading capability, an important part of Impala's solution in this area is the integration testing support, in particular through the interactive test runner.

Below we have an integration test for our `MessageService` implementation.

```
package com.application.main;

... imports omitted

public class MessageIntegrationTest extends BaseIntegrationTest {

    public static void main(String[] args) {
        InteractiveTestRunner.run(MessageIntegrationTest.class);
    }

    public void testIntegration() {
        MessageService service = 
                   Impala.getBean("messageService", MessageService.class);
        System.out.println(service.getMessage());
    }

    public RootModuleDefinition getModuleDefinition() {    
        return new TestDefinitionSource("myapp-main", "myapp-module1").getModuleDefinition();
    }

}
```

This integration test lives in the _myapp-main_ module in the _tests_ source folder. The fact that the test can live in the _myapp-main_ module is important, when we consider that the implementation for `MessageService` is in the module _myapp-module1_. It tests the behaviour of the `MessageService` through it's interface, rather than it's implementation.

Let's consider some aspects of the test implementation.

First, it extends `BaseIntegrationTest`, which simply extends JUnit `TestCase` and has an implementation of the JUnit `setUp()` method, which we will see in a moment.

The next thing to notice is the main method. The main method is used to start the interactive test runner.

The test is pretty straightforward. The pertinent bit is the use of the convenience method `Impala.getBean(beanName, type)` to retrieve a reference to `messageService`. This call effectively obtains the named bean from the Spring `ApplicationContext` associated with the root module.

You're probably wondering at this point how `Impala` gets to find out about the module constituents and hierarchy in the first place. To allow this to happen, we need to provide an implementation of `ModuleDefinitionSource`, which we do so in the final method shown in `MessageIntegrationTest`.

To complete the puzzle, we need to take a look at the superclass, `BaseIntegrationTest`, which has the following form:

```
public abstract class BaseIntegrationTest extends TestCase 
    implements ModuleDefinitionSource {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Impala.init(this);
    }
}
```

The method `Impala.init()` takes a parameter of type `ModuleDefintionSource`, hence the use of `this`. So what happens when `init()` is called? Impala examines the module hierarchy definition supplied via the `ModuleDefinitionSource` implementation. Impala integration tests typically themselves implement the interface `ModuleDefinitionSource`. It then compares it with the currently loaded module set. It will then make adjustments as appropriate. For example, it it encounters a module which has not been loaded, it will load this module. If it encounters one whose definition varies from the loaded module, the loaded module will be unloaded, and a new module loaded from the definition supplied in the `ModuleDefinitionSource` implementation.

This mechanism allows modules to be loaded incrementally over a test run, and only unloaded when required. This helps dramatically speed up the time taken to run a suite of integration tests compared to situations in which the full Spring context is loaded for each test.

## Module level integration tests ##

The first integration test we described lived in the root module. In this case, it is only possible to refer to root Spring module beans via their Java interfaces. At times, it is also useful to refer in integration tests to implementation beans directly. For example, if you are testing beans which are not exported to the service registry, but are instead part of the internal implementation of a module, this can be helpful.

An example of this is provided in the form of the `ProjectMessageIntegrationTest`, defined in _myapp-module1/tests_, whose `testIntegration` method has the following form:

```
public void testIntegration() {
    MessageService service = 
             Impala.getModuleBean("module1", "messageService", MessageService.class);
    System.out.println(service.getMessage());
}
```

Notice that the bean reference is obtained using `Impala.getModuleBean(moduleName, beanName, type)`. This will obtain a reference to the bean implementation, and not a proxy. The restriction, of course, is that these integration tests must be found in the in the relevant module project, or in projects corresponding to dependent modules of the module containing the bean implementation.

### Running the standalone interactive client ###

Let's get a bit more dynamic and see what happens when we run the interactive test runner for the JUnit test class `MessageIntegrationTest`.

As mentioned, this is a main Java application, which can be used to interactively execute tests, reload modules, etc. Here's some example output:

```
Using facade class: org.impalaframework.interactive.facade.InteractiveOperationsFacade
Test class set to com.application.main.MessageIntegrationTest
Starting inactivity checker with maximum inactivity of 600 seconds
--------------------

Please enter your command text
>test
Running test testIntegration
.Hello World!

Time: 0.059

OK (1 test)


Please enter your command text
>reload
Module 'myapp-main' loaded in 0.094 seconds
Used memory: 2.0MB
Max available memory: 63.6MB


Please enter your command text
>reload module1
Module 'myapp-module1' loaded in 0.043 seconds
Used memory: 2.7MB
Max available memory: 63.6MB


Please enter your command text
>rt
Running test testIntegration
.Hello World!

Time: 0.021

OK (1 test)


Please enter your command text
>
```

Any of the JUnit integration tests can be run as a regular unit test in Eclipse, with a green bar showing in the Eclipse JUnit view when tests succeed.

![http://impala.googlecode.com/svn/wiki/images/scaffold_junitidetest.png](http://impala.googlecode.com/svn/wiki/images/scaffold_junitidetest.png)

## Tests project ##

A feature of an Impala workspace is a single _myapp-tests_ project. The main purpose of the _myapp-tests_ project is to provide a single location from which all test suites can be run. For this reason, by convention, the _myapp-tests_ project will include references to all the projects in the workspace which contain tests, as the image below shows:

![http://impala.googlecode.com/svn/wiki/images/getting_started_testprops.png](http://impala.googlecode.com/svn/wiki/images/getting_started_testprops.png)

The _myapp-tests_ project can reference all projects in the workspace, it is the ideal place for defining test suites which can be run across multiple application projects, as the next code listing shows.
From the tests project, find the class `AllTests`. This contains a suite of tests covering all the tests in the project. Run this as a regular unit test, and you will see the following:

![http://impala.googlecode.com/svn/wiki/images/scaffold_alltests.jpg](http://impala.googlecode.com/svn/wiki/images/scaffold_alltests.jpg)

This covers testing for now. In [part four](GettingStartedPart4.md) I talk about how you use Impala in a web application.