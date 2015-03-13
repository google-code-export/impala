## Simple Startup ##

With Spring, a standalone
Spring `ApplicationContext` will normally be set up using a call such as:

```
new ClassPathXmlApplicationContext(contextLocatons);
```

There is a little more indirection in the mechanism for starting up a standalone application context. The
easiest way to do this is to use the static method:

```
Impala.init();
```

## Advanced Startup ##

So how do we control how Impala is configured at startup time?

The class `Impala` is really itself just a very thin wrapper with static methods around `OperationsFacade`, which in turn is a facade to the
various operations Impala exposes. Most of the implementations you are likely to use extends `BaseOperationsFacade`. This
class defines an abstract method

```
protected abstract List<String> getBootstrapContextLocations();
```

which the concrete class will need to implement. The default implementation used is `BootstrappingOperationFacade`.

As with web-based Impala applications, this implementation uses a two pronged approached to configuring Impala at runtime.
  * For built-in Impala capabilities, configuration can be applied by setting properties, typically
> using entries in _impala.properties_. For more details, see [PropertyConfiguration](PropertyConfiguration.md).
  * For introducing extensions into the Impala runtime, new Spring application context XML files can be added with additional
> beans to be loaded. This is described in more detail in [BootstrapContexts](BootstrapContexts.md).


It is straightforward to change the actual implementation class used through the system property `FacadeConstants.FACADE_CLASS_NAME`, which translates
to `FacadeConstants.facade.class.name`. For example, when using the [interactive test runner](InteractiveTestRunner.md), a different `OperationsFacade` subclass
is used.