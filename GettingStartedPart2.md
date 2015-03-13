# Implementing Modules #

In a [part one](GettingStartedPart1.md) I described the steps involved in setting up a new Impala project. It's time to spend a little bit of time examining and understanding what we've created. We'll start with the workspace structure.

## Implementing the main or root module ##

Every Impala application needs to contain a root module, which will typically be associated with a single Eclipse project, although it is also possible to use multiple Eclipse projects as the root module constituents.

Impala strongly encourages _interface based programming_. The main purpose of the root module is to define interfaces. In Java terms, the interface is implemented simply using a Java interface. There is also a Spring angle to interfaces, as we'll see. Beans in the root module will typically be _interface_ beans.

### The Java interface definition ###

The quick starter produces a simple _Hello World_ style application. The appropriate interface is called `MessageService`, which, without wishing to jump the gun, has an implementation which returns "Hello World".

```
package interfaces;

public interface MessageService {

    public String getMessage();
	
}
```

### The Spring config file ###

One of the fundamental principles of Impala is the idea of separating interface vs implementation. In the case of Spring beans, we want individual beans to be reusable across modules without different modules having to be aware of the implementations that sit behind the beans that they depend on.

So how do we express Spring interface beans in Impala. You probably guessed it - using proxies.

Here's the bean definition for the `messageService` bean.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:service="http://www.impalaframework.org/schema/service"       
	   xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans 
http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.springframework.org/schema/tx 
http://www.springframework.org/schema/tx/spring-tx.xsd
http://www.impalaframework.org/schema/service 
http://impala.googlecode.com/svn/schema/service-registry.xsd">

 <service:import id="messageService" proxyTypes="com.application.main.MessageService"/>
	
</beans>
```

This is contained in the file _resources/parent-context.xml_ in the _myapp-main_ project.

Note the use of the `service:import` bean definition. Here we are taking advantage of the `import` element from the Impala `service` namespace,
which defines a number of ways for importing services from the Service registry. In our example above, a bean exported to the service registry
using the name `messageService` will be imported, provided that it implements the interface `com.application.main.MessageService`.

For more details on how to import services from the service registry, see ImportingAndExportingServices.

### Module Definition File ###

By default, Impala modules are typically self-contained. The idea is that modules themselves have a mechanism for specifying all the information needed to determine their position in the module hierarchy as well as their internal composition. The default location for this information is in a file called module.properties, which will be located in the root of the module's class path.

The _module.properties_ file for the root module is show below.

```
type=root
```

This file can also be used to identify the Spring context locations for the module, as well as any other dependencies of the root module.

## Implementing a child or sub module ##

While the focus of the root module is primarily on providing a set of interface beans and a home for Java interfaces and shared/domain classes used in the application, sub-modules are typically more implementation focussed.

In our simple generated application, the implementation of our `MessageService` bean comes from the module _myapp-module1_. Before _myapp-module1_ has been loaded, it would be possible for clients to obtain a reference to the `MessageService` bean via the Java interface. However, actually calling methods on this bean would result in a `NoServiceException` - that is, until _myapp-module1_ has been loaded.

Note that modules form a hierarchy. For example, we could extend our sample application by creating new modules _module1A_ and _module1B_ as submodules of _module1_. In situations where deeply nested hierarchies of modules exist, it is possible for certain modules to straddle the interface/implementation boundary. However, in general, I'd recommend a fairly flat hierarchy of modules to keep things simple where possible.

### `MessageService` implementation ###

Our implementation class for `MessageService` is trivially simple, defined in the source folder _module1/src_:

```
package classes;

import interfaces.MessageService;

public class MessageServiceImpl implements MessageService {

	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
```

As you can imagine, the value for the message to be produced via the `getMessage()` call is injected via a Spring configuration file. Here's our Spring configuration, contained in the file _myapp-module1/resources/module1-context.xml_.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:service="http://www.impalaframework.org/schema/service"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans 
http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.impalaframework.org/schema/service 
http://impala.googlecode.com/svn/schema/service-registry.xsd">

    <!-- Export beans by name as a comma-separated list -->
    <service:export-array beanNames="messageService"/>
    
    <!-- Or alternatively add an entry for each bean you want to export 
    <service:export beanName="messageService"/>    
    -->
    
    <!-- Or alternatively automatically export services whose bean name
    matches endpoint bean name in parent application context 
    <service:auto-export/>    
    -->

    <bean id="messageService" class="com.application.module1.MessageServiceImpl">
        <property name = "message" value = "Hello World!"/>
    </bean>

</beans>
```

Note the use of `service:export-array`. This is one of the mechanisms for exporting beans from the current module to Impala service registry, in this
case exporting an array of beans to the service registry.

A number of alternatives are available: you can export beans individually using the `service:export` element.
Alternatively, you can use the `auto-export` element. In this case, Impala looks for beans of the same name in parent application contexts which implement the `org.impalaframework.service.NamedServiceEndpoint`. For each of these, it will export the implementation bean to the service registry, with the expectation that it will be consumed from the service registry by the `ContributionEndpoint` implementation. This provides a simple mechanism for using a child application context to provide implementations for beans defined in a parent application context - a bean interface vs implementation mechanism.

For more details on the bean export mechanisms available see ImportingAndExportingServices.

Finally, we show the _module.properties_ file for the child module.

```
parent=myapp-main
```

It simply identifies the name of the parent module.

Of course, we won't see any of this working until we take a look at integration tests, which we take a look at in [part three](GettingStartedPart3.md).