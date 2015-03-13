The main migration tasks for users moving from Impala 1.0 M6 from earlier versions is to replace `ContributionProxyFactoryBean` and `ModuleContributionPostProcessor` with
their 1.0M6 replacements. While these classes will still work as before in 1.0 M6, they are now deprecated and slated for removal in future releases.

For more details on importing from and exporting to the service registry, see ImportingAndExportingServices.

## `ContributionProxyFactoryBean` ##

This factory bean was formerly used to import beans from the Impala service registry and to create the proxy to them which can be used by other client beans.

Here's an example:

```
<bean id="namedMessageService"
class="org.impalaframework.spring.service.proxy.ContributionProxyFactoryBean">
	<property name = "proxyTypes" value = "interfaces.MessageService"/>
	<property name = "exportName" value = "myNamedMessageService"/>
</bean>
```

`ContributionProxyFactoryBean` itself has been replaced by `NamedServiceProxyFactoryBean`:

```
<bean id="namedMessageService" class="org.impalaframework.spring.service.proxy.NamedServiceProxyFactoryBean">
     <property name="proxyTypes" value="interfaces.MessageService"/>
     <property name="exportName" value="myNamedMessageService"/>
</bean>
```

However, with the availability of the new `service` namespace, you can now set this up as follows:

```
<service:import id="namedMessageService" 
	proxyTypes="interfaces.MessageService"
	exportName="myNamedMessageService"/>
```

## `ModuleContributionPostProcessor` ##

Prior to 1.0 M6, `ModuleContributionPostProcessor` was used to automatically export beans which from the current module
which have parent modules which expose named service endpoints with the same name as the current module's bean name.

For example, if you had the above `ContributionProxyFactoryBean` declaration in the parent module for the bean named
`namedMessageService`, and the child module contained the bean

```
<bean class = "org.impalaframework.spring.service.exporter.ModuleContributionPostProcessor"/>
```

then a bean in the child module named `namedMessageService` would automatically be exported.

Again, this behaviour still applies in 1.0 M6. However, `ModuleContributionPostProcessor` has been replaced by `NamedServiceAutoExportPostProcessor`.

```
<bean class = "org.impalaframework.spring.service.exporter.NamedServiceAutoExportPostProcessor"/>
```

However, with the new `service` namespace, this can simply be replaced using:

```
<service:auto-export/>
```