## Introduction ##

Impala's Interactive Test Runner allows you to write integration tests in your Spring-based applications in an interactive way.

What does this mean? A couple of example scenarios help to demonstrate.

  1. If you make a change to **code in one of your tests**, then you don't need to reload the full runtime application context to see the changes take effect. You simply update the test code and rerun the test. This is very helpful in TDD because it means that you can embellish/harden your tests without having to wait each time your entire Spring/Hibernate environment to refresh.
  1. If you make a change to **code in one of the modules**, you only need to reload that particular module. In most cases, the module reload will be almost instantaneous - that is, it certainly won't translate into any wait time for you, the user.
  1. If you've made changes to the **root module, or to serveral modules**, then you can easily reload all the modules using the `reload` command. In this case, the reload is extremely quick, probably not translating into any user wait time
  1. Only if you need to add or change **third party libraries**, or modify the Impala runtime configuration, should you need to restart the Interactive Test Runner. This is because third party classes are loaded once when required, but there is no mechanism currently to replace third party classes on the fly. For practical purposes, this is not a problem, because adding and modifying third party libraries is something which happens much less frequently than the above three scenarios.

## Setup ##

Setting up the Interactive Test Runner is simple. All you need to do is the following.

**1)** Make your JUnit `TestCase` test class implement `ModuleDefinitionSource`. The easiest way to do this is to use
`TestDefinitionSource`, and list the modules you need included as part of the test.

```
public class MessageIntegrationTest implements ModuleDefinitionSource {

	...
	
	public RootModuleDefinition getModuleDefinition() {	
		return new TestDefinitionSource("root-module", "dao-module" ,"service-module").getModuleDefinition();
	}
}
```

**2)** Add a main method in your class, so that you can run it as a standard Java application.

```
public static void main(String[] args) {
	InteractiveTestRunner.run(MessageIntegrationTest.class);
}
```

**3)** Add a call to `Impala.init()` in setup. This is so that each time the setup is called, the currently loaded module hierarchy
can be inspected and if necessary updated to reflect what is returned from `getModuleDefinition`. This is primarily useful
to support using the same test class in a larger suite with each test class potentially implementing `getModuleDefinition` differently.

```
@Override
protected void setUp() throws Exception {
	super.setUp();
	Impala.init(this);
}
```

## Launching ##

In Eclipse, select the test class, right click, then Run As -> Java Application. This will launch the Interactive Test Runner, with
output displaying in the Console view. This is also where input will be entered.

```
Test class set to test.MessageIntegrationTest
Starting inactivity checker with maximum inactivity of 600 seconds
--------------------

Please enter your command text
```

Note that the application will automatically stop after 10 minutes of inactivity.

## Commands ##

To check out the list of commands available simply type in `usage`:

```
>usage
show (aliases: s): Displays metadata on loaded modules
test (aliases: t): Runs test
rerun-test (aliases: rt, rerun): Runs test
reload (aliases: rel, module): Reloads root module and all child modules
repair (aliases: fix): Attempts to load modules which previously failed to load
set-class (aliases: sc, class): Loads module definition using supplied test class
change-directory (aliases: cd): Changes working directory
exit (aliases: e): Shuts down modules and exits application
usage (aliases: u): Shows usage of commands
```

Note also that each of the commands typically has one or more **aliases**, available to save typing. So if you simply type in `s`, you will get the
same output as if you type in `show`.

### show ###
Used to display the modules currently loaded module hierarchy, the Spring context locations, and the module types loaded.

```
>show
name=example, contextLocations=[parent-context.xml, extra-context.xml], type=ROOT
  name=example-dao, contextLocations=[example-dao-context.xml], type=APPLICATION
  name=example-hibernate, contextLocations=[example-hibernate-context.xml], type=APPLICATION
```

The example above shows the root module loaded is `example`. Also loaded are two application modules, `example-dao` and `example-hibernate`.
The Spring configuration files being used are shown in square brackets.

### test ###
Used to run tests. If only one test is present, then this test will be run without further prompting.

```
Please enter your command text
>test
Running test testDAO
.Entries of count 1996: 1

Time: 0.318

OK (1 test)
```

However, if more than one test is present, then you will prompted to choose from a list of tests. For example, if I add a `testTwo` to the above test class, I get
the following:

```
>test

More than one alternative was found.
Please choose option by entering digit corresponding with selection

1 testDAO
2 testTwo
>2
Running test testTwo
.
Time: 0.024

OK (1 test)
```

Note that I did not need to restart the Interactive Test Runner for the new test class to be picked up.

### rerun-test ###

This command is used to rerun the most recently run test. This time I use the alias `rt`.

```
>rt
Running test testTwo
.
Time: 0.018

OK (1 test)
```

The test method `testTwo` is run again.

### reload ###

If I make changes to application code I may need to reload a particular module, I use the `reload` command.
In order to load the module `example-dao`, I use the following command.

```
>reload dao
Module 'example-dao' loaded in 0.037 seconds
Used memory: 3.8MB
Max available memory: 63.6MB
```

Notice how you don't need to provide an exact match for the name of the module you are reloading.
The first module found containing the specified text in its name will be reloaded.

In order to reload the **root** module, and consequently all other modules, simply type `reload`.

```
>reload
Module 'example' loaded in 0.199 seconds
Used memory: 4.5MB
Max available memory: 63.6MB
```

Note that even though reloading the root module requires all Spring application context to be rebuilt,
Hibernate session factories to be recreated, it is still quick, because it does not require third party library classes to be reloaded,
and doesn't require the JVM to be re-instantiated.

### set-class ###

This command allows you to change the test class that you are using with the Interactive Test Runner, within the current module.
`set-class` saves you having to stop the Interactive Test Runner to change the test class.
Some example output is shown below.

```
Please specify class search text
>test

More than one alternative was found.
Please choose option by entering digit corresponding with selection

1 manual.ManualJarDeployerTest
2 tests.AlternativeEntryServiceTest
3 tests.EntryDAOTest
4 tests.EntryServiceTest
5 tests.InitialIntegrationTest
>4
Test class set to tests.EntryServiceTest

Please enter your command text
>test
Running test testService
.classes.EntryServiceImpl@2c7ad9

Time: 0.273

OK (1 test)
```

First, you are prompted with some text which you can use to search for test classes. If more than one is found, you need to make a
selection by entering the number corresponding with the entry found. Once the test class has been changed, any additionally
required modules will be loaded as needed for the test.

### change-directory ###

The command `set-class` only allows you to change to test classes in the same module as the currently loaded test class.
You can change this by using the `change-directory` command. In the following sequence of commands I change the
current module directory to _example-dao_, use `set-class` to select the test class. The execute a test in this class.

```
Please enter your command text
>cd

Please enter directory to use as current working directory.
>example-dao
Current directory set to example-dao

Please enter your command text
>sc

Please specify class search text
>test
Test class set to InProjectEntryDAOTest

Please enter your command text
>test
Running test testDAO
.org.springframework.orm.hibernate3.HibernateTemplate@d5cfd6
Entries of count 1996: 1

Time: 0.034

OK (1 test)
```

`change-directory` allows you to change the current 'test module' without having to restart the Interactive Test Runner.

### repair ###

Use this command to reload any modules which previously failed to load. You
can see the current load status of modules using the `show` command.

_Note: coming in 1.0M6._
