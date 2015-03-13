# Introduction #

**Note**: this page applies to 1.0M4 and snapshot distributions after [revision 2647](https://code.google.com/p/impala/source/detail?r=2647)

If you've read the [getting started series](GettingStarted.md), set up your own workspace, played a bit with the samples, read a bit of documentation,
you're probably ready to start working on a real application.

The `newproject` command only sets you up with a skeletal set of modules. To take this further, you will soon need to add new modules.

My recommendation is to go for a fairly fine module granularity, as it gives you more flexibility in reducing coupling between
various parts of the system. Any piece of functionality which is not inherently coupled with the
rest of the system, probably belongs in its own module. In practice, a module could consist of anything from one or two classes
(or perhaps none at all, in some cases) to a few dozen. If it gets much larger than that, I would definitely question whether modules were being
used correctly.

Impala has some support for taking automating the process of creating new modules, through the `newmodule` command.

## Adding new modules ##

Simply go to the Impala home directory and type `ant newmodule` as shown below:

```
cd $IMPALA_HOME
ant newmodule
```

This brings up a set of inputs, which require you to enter the name of the new module, its parent, its type. Here's some sample output:

```
ant newmodule
Buildfile: build.xml

newmodule:
     [echo] Creating new module, Impala version: 1.0M5

scaffold:input-workspace-root:
    [input] Please enter name of workspace root directory: [/Users/philzoio/workspaces/newproject]


scaffold:input-parent-project:
    [input] Please enter name of parent module. If not sure, enter the name of the root module:
main

scaffold:input-build-project:
    [input] Please enter build project name, to be used for build module: [build]


scaffold:input-new-project:
    [input] Please enter name of new module:
module2

scaffold:input-module-type-name:
    [input] Please enter type or project: ([empty], application, web, extension)


scaffold:input-repository-project:
    [input] Please enter name of repository project: [repository]


scaffold:input-project-prefix:
    [input] Please enter the project name prefix: []
myapp-

scaffold:input-base-package:
    [input] Please enter the base package to be used for application: [com.application]
   

scaffold:newmodule-confirm:
     [echo] About to create new module of type 'application' with name 'module2' create in /Users/philzoio/workspaces/newproject
     [echo] Repository project name: repository
    [input] Press return key to continue, or CTRL + C to quit ...


scaffold:filterset:

scaffold:copymodule:
     [copy] Copying 8 files to /Users/philzoio/workspaces/newproject/myapp-module2
     [copy] Copied 8 empty directories to 1 empty directory under /Users/philzoio/workspaces/newproject/myapp-module2
     [copy] Copying 1 file to /Users/philzoio/workspaces/newproject/myapp-module2/resources

copypackage:
     [copy] Copying 1 file to /Users/philzoio/workspaces/newproject/myapp-module2/src
     [copy] Copying 2 files to /Users/philzoio/workspaces/newproject/myapp-module2/test
   [delete] Deleting directory /Users/philzoio/workspaces/newproject/myapp-module2/src/classes
   [delete] Deleting directory /Users/philzoio/workspaces/newproject/myapp-module2/test/classes

scaffold:copyweb:

scaffold:newmodule:

BUILD SUCCESSFUL
Total time: 1 minute 38 seconds
```

Note that here I chose the name 'module2', and I went for the default module type of _application_. Here, you also had the choice of
entering _hibernate_ or _servlet_.

Also, I've made sure to choose the same prefix as when creating the initial project:
`myapp-`, and the same base package (`com.application`).

It is worth bearing in mind the purpose of this script, which is to cut down on a couple of the manual steps that you will need to perform for setting up your module correctly. For example, the _hibernate_ module type comes with a Spring configuration file which includes
`sesssionFactory` and `transactionManager` beans.

A key thing to understand is that Impala **does not enforce any programming style or conventions** when it comes to setting up Spring configuration
files and writing your applications. That is entirely up to you. The supplied module type templates are purely a convenience to save
typing. They are no substitute for Grails- or Rails-style code or component generation.

## Extra manual steps ##

The extra steps required depend on the new module type you are adding:

  * [Hibernate](AddingModulesHibernate.md)
  * [Servlet](AddingModulesServlet.md)