### What does Impala do? ###

Impala allows you to break out your Spring application into individual modules. Each module has it's own Spring application context, which can be started and stopped independently. Each time this happens, a new class loader will typically be used, allowing class changes to be reflected dynamically. Modules are composed in a hierarchy, with interfaces typically defined at higher level modules, and implementation classes tending to be found in lower level modules.

### What kind of problems does Impala solve? ###

Impala allows you to develop much more **productively** because you can reload parts of your application rather than having to restart your application every time you make a change.

Also, because you can write your applications in a genuinely modular way, it is much easier to keep your applications simple even as the size and complexity of your applications grow.

### How can Spring users benefit from Impala? ###

While Spring framework is an excellent piece of software, the fact that it doesn't have any first class support for modules is a real limitation. A large Spring application will consist of thousands of classes and hundreds of bean definitions. These bean definitions will typically be spread across different XML definition files.

Managing these XML definition files becomes a real challenge (and at times a nightmare), especially as the project grows. For example, certain integration tests only require some of the functionality. On the one hand, you don't want to load your entire application for every integration test, but on the other, mixing and matching between different bean definition files across tests is time consuming, error prone, and headache ridden.

Another problem with Spring is the mechanism for varying wirings to support different combinations of configurations within the system. Spring effectively allows this by adding new bean definition files. The definitions which need to be overridden are replaced using new bean definitions with the same names. This mechanism works, but is quite cumbersome, and can result in bean definition bloat.

With applications divided into Impala modules, these problems become much more manageable. Typically, you won't even need to specify application context definition file names when working with Impala. You can compose your application at a higher level using modules. That being said, you can easily vary configurations within modules, either by specifying the Spring configuration files to be used in the module, or by using a flexible bean definition import mechanism provided by Impala.

In addition, Impala provides a number of features aimed at making Spring applications easier to manage and configure. Your operation teams will like Impala!

### How does Impala compare with Spring Dynamic Modules and the dm Server? ###

Spring Dynamic Modules and the SpringSource dm Server also aim to tackle the same problem as Impala - providing dynamic modularity to Spring applications. However, the approach to the problem is quite different.

Both Spring DM and dm Server take OSGi as their starting point, and as a result is quite "OSGi-centric". Much of the work that has gone into Spring DM is about bringing the Spring programming model to OSGi. The dm Server adds extra feature which make Spring DM much more usable for the typical user, but at the cost of a more restricted environment - it only works with the Equinox OSGi runtime. Also, it has a less commercially friendly GPL licence.

Impala approach is much more about answering the question: "What can we do, given existing mainstream technologies and Spring Framework capabilities, to make modularity a reality for Spring applications?" It's evolution has been driven by the need to provide practical solutions that address the pain points associated with lack of dynamic modularity in Spring itself.

Impala's class loader and modularity model has evolved into something quite sophisticated, capable of of supporting a great deal of flexibility in the structuring of application modules. It does not, however, support multiple versions of third party libraries. In terms of third party library management at runtime, Impala works in the same way as traditional Java applications.

### What is the relationship between Impala and TDD? ###

Impala is extremely well suited to test driven development. The interactive test runner provides a powerful mechanism for driving development through [integration tests](http://impalablog.blogspot.com/2008/04/thoughts-on-integration-testing.html). See [this page](GettingStartedPart3.md) for more details on Impala's test environment.

Equally, integration tests can be run within the IDE or ANT, both individually and as part of a suite, without any additional setup.

### How easy is it go get started with Impala? ###

Very easy. By following the instructions in the [getting started tutorial](GettingStarted.md) you can build and run a modular web application without having to edit a single line. All you need to do is run a few simple commands.

If you're in a bit more of a rush, see this [shortened tutorial](FirstSteps.md).

The only software prerequisites for getting started with Impala are Java (1.5 and above) and ANT (1.6.5 or later is recommended).

Because Impala is based on Spring with unchanged programming model, it will feel very familiar to Spring users. The additional additional knowledge required is a basic understanding of how Impala structures application contexts and class loaders to create a dynamic, modular application environment. All of this means a pretty shallow learning curve for experienced Java developers.

### What does it mean to say 'Impala supports modular application development'? ###

Modularity is very important in Impala. The ideas behind modularity are essentially
the abilities to separate implementation from interface (at a coarser level of
granularity than classes and interfaces), and the avoidance of unnecessary coupling
between components.

This topic is covered in quite a bit of detail in [this blog entry](http://impalablog.blogspot.com/2008/01/modularity-what-is-it-and-why-is-it.html).

### How stable is Impala? ###

As of 1.0M6, no substantial further API changes are planned. The code base has been through several intense round of refactorings over the last year.

The Impala code base is in fact very well factored - only a couple of classes are more than 500 lines long, and most classes are less than 100 lines long.

### How do  class loaders work in Impala? ###

The mechanisms for specifying the behaviour of classloaders is configurable. However, in practice, you can think of modules as having a class loader hierarchy which parallels the module hierarchy. Specifically, class loaders for individual modules have visibility of their parent module class loaders, but not that of sibling class loaders. All modules have visibility of the application class loader.

Impala allows you to arrange modules in a graph, instead of just a hierarchy, which gives you a great deal more flexibility in specifying a module structure for your applications. Each module will delegate to class loading operations to dependent modules in the correct order.

Impala does not address loading of third party libraries. Here, and Impala application works identically to regular Java applications - all third party libraries are loaded by the application class loader, which will typically either be the Java class path, or, for web applications, jars in the _WEB-INF/lib_ directory.

### Will I run into class loader problems? ###

You'd have to be pretty brave to say never here. However, without making an effort to bend Impala out of shape, you shouldn't run into class loader problems very often, if at all. Impala has been carefully designed to minimise the risk of class loader issues occurring, and make it easy to diagnose these when they do occur. In practice, class loader issues appear to come up only very rarely, and are addressed as soon as they are detected.

### Does Impala work with Hibernate? ###

Yes, as with any other mainstream library we've tried. See the [Petclinic sample](http://impalablog.blogspot.com/2008/01/springs-petclinic-sample-impala-style.html).

### Does Impala work with other technologies, such as JMX, Quartz, etc. ###

Absolutely. Impala should work with just about any technology that works within Spring itself.

### How does Impala integrate with other web frameworks? ###

Impala work with any web framework using the standard Spring integration mechanisms available to that framework. Simply expose your Impala-based service layer.

With Spring MVC, Impala achieves and more dynamic integration out of the box, allowing Spring MVC as well as other elements of the application to be reloaded dynamically without having to restart your web container or redeploy your web application.

With a small amount of extra work, it is also possible to achieve this level of integration with arbitrary web Java web frameworks. See the [web frameworks sample](SamplesWebframework.md).

In the future some frameworks, such as Grails, would probably require and benefit from a more specialised integration mechanism which takes advantage of the capabilities of that framework.

### What about OSGi? ###

OSGi is a power technology that allows for modularity and class version in Java application development. In the problems it tackles, Impala is in many ways quite similar to [Spring Dynamic Modules](http://www.springframework.org/osgi/), which is based on OSGi. While clearly a great technology, it is far from clear to me that OSGi represents the simplest, most practical way to solve the most burning issue which is affecting Spring developers on a day to day basis - the lack of a first class concept of modularity within Spring core, and the resultant difficulties in configuring large, complex applications. OSGi is a great solution for third party class versioning issues, but for me this is much less of a issue than the need for a simple, productive way of achieving modularity. Nevertheless, OSGi is on the Impala roadmap, and an [OSGi sample](SamplesOSGi.md) has been created. However, it will not be given high priority prior to the final 1.0 release. See this [Impala Blog entry](http://impalablog.blogspot.com/2007/11/impala-and-osgi.html).

See also the [OSGi convergence wiki page](OSGiConvergence.md).

### What about Maven? ###

Unlike many open source projects, Maven is not required to work with Impala.

Impala is not tied directly to any build system. However, it does feature an optional ANT-based [build system](BuildSystem.md), which saves you having to write your own scripts when kickstarting your application. See also the build section in the [getting started tutorial](GettingStartedPart5.md).

Impala has a simple approach to dependency management, aiming not for full transitive dependency management, but for simple but effective tools for managing dependencies, described in this [Impala Blog entry](http://impalablog.blogspot.com/2007/09/impalas-non-maven-approach-to-simpler.html).

All this being said, better support for Maven is on the road map. Maven users should be able to work with Impala, using Maven project structure conventions. There is no reason why Maven users shouldn't be able to build Impala applications using Maven. However, use of Maven is not required.

### Why the name Impala? ###

The [Impala](http://en.wikipedia.org/wiki/Impala) is an antelope common in the Southern African bushveld and savannah. Impalas are graceful, elegant and agile - qualities worth aspiring to in software development.