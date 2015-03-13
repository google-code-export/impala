## Prelude ##

In our examples, we will consider a bean contained in the exporting module:

```
<bean id="messageService" class="classes.MessageServiceImpl">
    <property name="message" value="Hi Phil - a typed message"/>
</bean>  
```

which will be exported and imported in various ways.

## Adding the namespace headers ##

Before using one of the `service` elements in your bean definition file, you will need to add the Impala service namespace declaration
to the root element of the relevant Spring configuration files.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:service="http://www.impalaframework.org/schema/service"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.impalaframework.org/schema/service http://impala.googlecode.com/svn/schema/service-registry.xsd">
```

Note the entry `xmlns:service="http://www.impalaframework.org/schema/service"` declares the Impala service namespace, and the entry
`http://www.impalaframework.org/schema/service http://impala.googlecode.com/svn/schema/service-registry.xsd` associates
this with a schema location.


---


## Export elements ##

The following elements from the Impala service registry namespace are used to export beans from individual modules to the
Impala service registry.

### 'export' element ###
Used to export an individual Spring bean as an Impala service. The `export` element allows you to
add beans to the service registry which can be looked up by name, Java type, or published attributes.
  * `beanName` is the name of the bean being exported, resolved used `ApplicationContext.getBean(beanName)`
  * `exportName` is the name against which the bean is registered in the Impala service registry (enabling name based lookups using the import element)
  * `exportTypes` is a comma separated list of types against which the bean will be exported (enabling typed based lookups using the import element)
  * `attributes` is a set of attributes pairs, separated by default by line breaks or commas, with name and value separated by equals.

Beans of any Spring scope can be exported to the service registry. Additionally, beans backed by non-singleton
`FactoryBeans` can be exported. In the case of non-singleton beans or `FactoryBeans`, the service registry will hold a reference to the underlying Spring context and bean name, allowing for on-demand lookup of the underlying service object.

**Examples:**

The following example exports the `messageService` bean using the service registry name `myNamedMessageService`. In other words,
clients importing the service from the service registry will be able to do so using the attribute declaration `exportName="myNamedMessageService"`.

```
<service:export 
	beanName="messageService" 
	exportName="myNamedMessageService" />    
```

The following will export the service using the same name as the bean name, i.e. `messageService`.

```
<service:export 
	beanName="messageService"/>    
```

This example exports the service using the types `MessageService` and `MessageServiceImpl`. This will allow the service to looked up using
these types (providing of course that these types are visible to the importing modules), using the `import` element.

```
<service:export 
	beanName="messageService" 
	exportTypes="interfaces.MessageService,classes.MessageServiceImpl" />
```

The following example exports the bean with the attribute `sender=phil`. Attributes are useful in allowing you to look up beans by `filterExpression`,
and for defining filters for the 'list' and 'map' import elements.

```
<service:export 
	beanName="messageService" 
	attributes="sender=phil" /> 
```

The following publishes the `messageService` with a `service.ranking` attribute of 10, which is greater than the default
service ranking of 0. If two modules publish services by the same interface but different service rankings, the one with the higher
service ranking will be picked up first.

```
<service:export beanName = "messageService" 
	exportTypes="interfaces.MessageService" 
	attributes="service.ranking=10"/>
```


---


### 'auto-export' element ###

Used to automatically export beans to the service registry by allowing a child module to provide a concrete implementation for
a service being 'imported' in a parent module. It checks for the presence in beans in the parent (and ancestor) application
contexts for beans which are service endpoints, created for example using the `import` element.
It also checks each bean in the current application context, and if its bean name corresponds with the endpoint's
export name, it exports the bean using the current bean name as the export name.

In this way, the `auto-export` element provides a very easy way to contribute an implementation for a service endpoint defined in a parent
application context.

The `auto-export` element does not have any parameters, simply looking as follows:

```
<service:auto-export/>
```


---


### 'export-array' element ###

Used to export an array of beans to the service registry. Beans will be exported by name.
  * `beanNames`: the names of the beans being exported
  * `exportNames`: the corresponding names against which the exported beans will be registered in the service registry.

Note that beans are not exported by any explicit type (as in the export `exportTypes` attributes), and not exported
using any explicit filter. This means that when using this form of export, bean lookup will typically be using the `import` element with the `exportName` specified.

An example is shown below, in the case below using separate export names for each of the exported beans.

```
<service:export-array beanNames="messageService1,messageService2" exportNames="myMessageService1,myMessageService2"/>
```

The example above is really just a convenient alternative to two individual `export` entries:

```
<service:export 
	beanName="messageService1" 
	exportName="myMessageService1" />  
	
<service:export 
	beanName="messageService2" 
	exportName="myMessageService2" />  
```

If the export names used in the service registry are the same as the bean names, then the following will do:

```
<service:export-array beanNames="messageService1,messageService2"/>
```


---


---


## Import elements ##

The following elements from the Impala service registry namespace are used to export beans from individual modules to the
Impala service registry. The underlying service object is wrapped by an Impala proxy. This insulates the client from
complexity arising from the dynamic nature of the service registration and removal, the possibly dynamic scope of the beans
themselves.

### 'import' element ###

Used to import an individual Spring bean from the Impala service registry.
Basically, there are three ways of importing beans. First, by name, corresponding with a name used to register a bean into the service
registry. Second, by type, corresponding with types used to register a bean in the service registry.
Third, by filter, in which case the service must have been exported using attributes which match the filter expression.

The following attributes apply:
  * `id`: used for the name of the bean. This is required so that the imported bean can be referenced by other beans.
  * `filterExpression`: if not null, then used to look up the service as an entry which matches the filter
  * `exportName`: if not null, and filterExpression is null, then used to look up service as a named entry
  * `exportTypes`: if both filterExpression and exportName are null, then used to look up service as a typed entry. Even if `filterExpression` or `exportName` is specified, then if `exportTypes` is present it is used to limit the candidate services to those which were exported using all of the specified set of export types.
  * `proxyTypes`: must be present if exportTypes is not. If present, then the `proxyTypes` are used to create the proxy to the service which is passed to client users of the service. If not present, then the export types (from the `exportTypes` attribute) are also used as the proxy types.
  * `proxyOptions` (1.0 RC3 onwards): allows proxy creation defaults typically set in _impala.properties_ to be overridden. This allows, for example, for individual
> proxies to support retries, or provide no-op dummy implementations when the defaults set in _impala.properties_ imply different behaviour.

**Examples**

The example below imports a bean exported using the `exportName` attribute with the value `namedMessageService`. The imported bean is stored in the
current (client) application context with the bean name `messageService`. The underlying service is proxied using the `MessageService` interface.

```
<service:import id="namedMessageService" 
	exportName="messageService"
	proxyTypes="interfaces.MessageService"/>
```

The following example has the identical effect. Since neither the `exportTypes` or `filterExpression` attributes have been set
the `exportName` is implicitly used, set to the same as the bean `id`.

```
<service:import id="namedMessageService" proxyTypes="interfaces.MessageService"/>
```

The following example imports a bean explicitly exported using the type `interfaces.MessageService`. If more than one bean is found which
satifies this condition, the bean with the highest `service.ranking` custom attribute is used. If the `service.ranking` attribute is not used
for any candidate service, then the first service added to the service registry is returned.

```
<service:import id="typedMessageService" exportTypes="interfaces.MessageService" proxyTypes = "interfaces.MessageService"/>
```

The above bean is also proxied using the interface `interfaces.MessageService`. However, if the `exportTypes` element is used,
the `proxyTypes` element can be omitted. The following entry has the identical effect.

```
<service:import id="typedMessageService" exportTypes="interfaces.MessageService"/>
```

The following example uses a filter expression to retrieve a bean from the service registry. The filter uses a
[LDAP RFC 1960](http://www.ietf.org/rfc/rfc1960.txt) expression to match against attributes specified using the `export` element's
`attributes` attribute. In the example below, the filter will match entries to the service registry which are
exported with the custom attribute `sender` with a value of `phil`.

```
<service:import id="filteredMessageService" 
	proxyTypes="interfaces.MessageService"
	filterExpression="(sender=phil)"/>
```

In the example below, we are using the `proxyTypes` element to specify the proxy interfaces or class. It is also possible to use
specify `exportTypes` in combination with `filterExpression`. In the following example, the filter expression is applied,
but the matched bean also has to have been explicitly registered using the export type `interfaces.MessageService`.

```
<service:import id="filteredMessageService" 
	exportTypes="interfaces.MessageService"
	filterExpression="(sender=phil)"/>
```

Note that it is not possible to use `exportTypes` or `filterExpression` in combination with `exportName`. The idea is that the
`exportName` and `proxyTypes` attributes should provide a sufficiently narrow match to eliminate the necessity of also matching by
`exportTypes`.

In the example below, there is no entry in the service registry corresponding to the `missingService` entry. However,
because the options `allow.no.service` and `log.warning.no.service` have been set to true, method invocations on this
proxy will not throw a `NoServiceException`. Instead, a no-op implementation of the proxy interface will be invoked,
returning `null` for method calls returning `Object`, and returning default values for methods returning primitives.
The available options correspond with [Impala properties](PropertyConfiguration.md) prefixed with `proxy.`

```
<service:import id="missingService" 
    proxyTypes="interfaces.MessageService"
    proxyOptions="allow.no.service=true,log.warning.no.service=true"/>
```


Note also that as of Impala 1.0M6 it is not possible to use the `filterExpression` attribute to import beans backed by non-singleton Spring beans
or `FactoryBeans`.


---


### 'list' element ###

Used to create a `java.util.List` which is backed by Impala service registry entries.

The following attributes apply:
  * `filterExpression`: used to filter services which are eligible for adding to the list
  * `exportTypes`: if exportTypes is present it is used to limit the candidate services to those which were exported using all of the specified set of export types.
  * `proxyTypes`: if present, then the proxyTypes are used to create the proxy to the service for each entry in the map. If not present, then the export types (from the `exportTypes` attribute) are also used as the proxy types. If neither is present, then Impala will attempt to create a CGLIB proxy of the class.

Note that Spring beans of non-singleton scope (e.g. `prototype`), or beans backed by
non-singleton `FactoryBeans` are not supported. Impala will simply filter these beans out
so that they will not be included in the list, even if they match using one or more of the
other attributes.

The example below creates a list backed by all beans which implement `MessageService` and are exported with the `sender` attribute specified.

```
<service:list id="messageServiceList" 
	proxyTypes="interfaces.MessageService"
	filterExpression="(sender=*)"/>
```

As with the `import` element, you can also limit the entries to explicit export types using the `exportTypes` attribute. If `exportTypes` is specified, then
`proxyTypes` is optional.

TO ADD 'set' element - with 1.0M7.


---


### 'map' element ###

Used to create a `java.util.Map` which is backed by Impala service registry entries.

The following attributes apply:
  * `filterExpression`: used to filter services which are eligible for adding to the list
  * `exportTypes`: if exportTypes is present it is used to limit the candidate services to those which were exported using all of the specified set of export types.
  * `proxyTypes`: if present, then the proxyTypes are used to create the proxy to the service for each entry in the map. If not present, then the export types (from the exportTypes attribute) are also used as the proxy types. If neither is present, then Impala will attempt to create a CGLIB proxy of the class.
  * `mapKey`: the service attribute used as the key for the map. By default this will be 'mapkey'

Note that, as with the `list` element,
Spring beans of non-singleton scope (e.g. `prototype`), or beans backed by non-singleton `FactoryBeans` are not supported. Impala will simply filter these beans out so that they will not be included.

An example of the service registry map is shown below. Here, entries in the map must have been exported using the `sender` attribute.
Note also that because the map key being is not the default, it must be explicitly specified.

```
<service:map id="messageServiceMap" 
	proxyTypes="interfaces.MessageService"
	mapKey="sender"
	filterExpression="(sender=*)"/>
```

In the example above, the value for the sender attribute is used as the key for each map entry.


---


---


## Miscellaneous elements ##

The service namespace also contains a number of elements which are used to facilitate access to beans,
without necessarily involving the service registry directly.

### 'parent' element ###

Used to refer to a bean already parent application context.

The simplest creates a local application bean definition which points to a bean already defined with the same name in a parent context.

```
<service:parent id="entryService"/> 
```

The `parent` element can also be used to 'rename' the parent bean, by referring to the parent context bean using a different name,
as shown in the example below.

```
<service:parent id="parentEntryService" parentBeanName="entryService"/> 
```


---


### 'named-bean' element ###

The `named` bean is used to create a bean entry which points to a different bean in the application context (or parent), identified using
a specified name.

```
<bean id = "target" class = "org.impalaframework.spring.service.config.IntegerHolder" scope = "singleton"/>
<service:named-bean id ="namedBean" beanName = "target"/>
```

The `named-bean` element usually refers to a singleton bean target. However, it may also need to cope with the possibility that the bean to which it refers
may not be a singleton (for example, if it uses _prototype_ scope).

In this case, the _singletonAware_ attribute can be used:

```
<bean id = "prototypeTarget" class = "org.impalaframework.spring.service.config.IntegerHolder" scope = "prototype"/>
<service:named-bean id ="prototypeNamedBean" beanName = "prototypeTarget" singletonAware = "true"/>
```

In the above example, the bean `prototypeNamedBean` is automatically treated as a prototype, since the target bean is a prototype.

Impala also supports the ability to treat a prototype bean as a singleton, allowing it to be safely injected into another bean with
singleton scope. This is done through the `proxied` attribute.

```
<service:named-bean id ="proxyingBean" beanName = "prototypeTarget" proxied = "true"/>
```

In the above the example, the bean created is always a singleton, wrapping access to the prototype target bean using a proxy.


---


### 'optional' element ###

The `optional` bean is used to declare a bean definition which points to a named target bean if it is present. However, it also allows
for the possibility that the target bean will not be present, in which case the `fallback` bean definition is used.
The named target bean is hence optional; if it is not present the fallback is used. The fallback bean is not optional -
it must be present in the application context (or parent).

```
<service:optional id ="optionalTarget" beanName = "target" fallback="fallback"/>
```