One of the problems with dynamic reloading of web modules is how to handle session state. The problem is occurs because after a module reload,
some of the objects held in the session may have been loaded using class loaders that have since been discarded. When the new module
attempts to cast the session object, without any remedial action a `ClassCastException` will be thrown.

Impala fixes this problem by wrapping the `HttpSession` with a `getAttribute` method which does the following:
  * gets the object from the underlying session
  * if an object is found, then the class of that object is inspected to determine whether its classloader is visible to that of the current module's class loader. If so, it is returned as is.
  * if the session object has an incompatible class loader, a check is performed to determine whether the object is serializable. If it is not serializable, then the session attribute is discarded and the event is logged as a warning.
  * if the item is serializable, then an attempt is made to clone the object using serialization. In the process, the state of the object is copied to the new instance, but equally importantly, the new object now has a class loader.

Note that in order to enable this mechanism you will need to set the following property in your _impala.properties_ file.

```
session.module.protection=true
```

It should be noted that this mechanism is not guaranteed to work. For example, serialization may fail if the cloned object's class definition has changed in an incompatible way.

In short, if you ensure that all your session objects use classes which implement `java.io.Serializable`, then you have at least a fighting chance of
having session state preserved through module reloads. Extra care should be taken if you intend to use in production environments
dynamic module reloading for modules which hold session state.

A demonstration of the mechanism for preserving session state is available in the Struts module in the [web frameworks sample](SamplesWebframework.md).




