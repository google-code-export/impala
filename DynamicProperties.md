Impala includes a mechanism for typed dynamic properties. You're probably asking, if you've got dynamic modules, why would you want dynamic properties?

The purpose of dynamic properties are really just to make it easier to wire in, and when necessary update, application configuration. For example, suppose you
have an application which manages customer deliveries of products to customers, and you
want to change the fuel surcharge on deliveries due to an increase in the price of petrol. Here, all you want to do is update a number, either
in the database or in a properties file on the file system, and have it reflected in the application.

Another example is if you have a flag which turns on or off a piece of functionality.

In both cases, doing a module reload would be overkill. Instead, you want to be able to inject the property into your bean, and update this property where necessary.

Here's an example:

```
public class MySpringBean {
	
	private FloatPropertyValue magicNumber;

	public someMethod() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("magicNumber", ""+magicNumber.getValue());
		
		...
	}

	public void setMagicNumber(FloatPropertyValue magicNumber) {
		this.magicNumber = magicNumber;
	}
}
```

Note that the property is wired in not as a float, but as a `FloatPropertyValue`. This makes it possible to:
  * provide a backing implementation of the property source in a way that is transparent to the bean.
  * update the value behind the scenes in a way that is transparent to the bean.

The bean does not need to do any lookups on its own.

The property value is strongly typed, saving you from having to do type conversion in your application. Implementations are
also available for boolean, `String`, `int`, `long`, `float`, `double` and `Date` values.

The Spring configuration for this is shown below:

```
<bean name="/servlet1/test.do1" class="TestController1">
	<property name = "magicNumber">
		<bean class="org.impalaframework.config.FloatPropertyValue">
			<property name="name" value="magic.number"/>
			<property name="propertySource" ref="dynamicProperties"/>
		</bean>
	</property>
</bean>

<bean id="dynamicProperties" class="org.impalaframework.config.ExternalDynamicPropertySource">
	<property name="fileName" value="dynamic.properties"/>
</bean>
```

The configuration is more complex than if you were simply using a `PropertyPlaceholderConfigurer`. However, it is dynamic,
and the overall solution is much simpler than if you were using a lookup based mechanism in the code.

Also, with Impala 1.0 RC2, the configuration is considerably simplified through the use of a [dynamic properties namespace](NamespaceDynamicProperties.md).

```
<bean name="/servlet1/test.do1" class="TestController1">
    <property name = "entryService" ref = "entryService"/>
    <property name = "magicNumber">
        <dynaprop:float name="magic.number" propertySource="dynamicProperties" />
    </property>
</bean>
```

The entry `<dynaprop:float name="magic.number" propertySource="dynamicProperties" />` makes it much easier to reference
dynamic properties where they are used in Spring configurations.

The property value is backed by a `PropertySource` instance. An implementation of this interface could read a properties file from the
file system, or alternatively look up values from a database table.

The `PropertySource` implementation used here is `ExternalDynamicPropertySource`, which also allows you to specify
a folder on the file system in which to look for the _dynamic.properties_ file. If start the application with the
system property `-Dproperty.folder=/tmp`, and place a file called _dynamic.properties_ in the _/tmp_ directory,
the value for `magic.number` in this file will be used. However, if this file is not present, then the fallback is
simply to look for _dynamic.properties_ on the classpath.

The advantage of the external lookup mechanism is that I don't need to dig around in the application to modify the property value,
something which is a real bonus for system administrators.

`ExternalDynamicPropertySource` can optionally be provided a `ScheduledExecutorService`, for example provided using the
Spring's `ScheduledExecutorFactoryBean`, for periodic modification checks on the underlying property file.
If none is provided, one a single threaded `ScheduledExecutorService` will be created, which will do the modification
check once every 100 seconds or so.

The [example samples application](SamplesExample.md)'s _example-servlet1_ module contains an example of the dynamic property at work.



