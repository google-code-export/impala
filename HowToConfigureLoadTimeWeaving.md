## Background ##

Spring supports two flavours of AOP (Aspect Oriented Programming). Proxy-based AOP, and AOP using AspectJ.
Proxy-based AOP is heavily used in Spring for supporting transactional method invocation, and also in Impala, in
proxies for services 'imported' from the shared service registry. AOP using AspectJ is more powerful, but also
harder to configure, in that it involves enhancement of Java byte code.

As of Impala 1.0 RC3, also Impala supports load time weaving of aspects, although with some caveats. Most notable of these
is that it is currently only possible to weave aspects into classes loaded in the same module as the AOP configuration.
In other words, it is not possible to weave aspects into classes already loaded in other modules.

For more on load time weaving using Spring, see the [Spring documentation](http://static.springsource.org/spring/docs/2.5.x/reference/aop.html).

## Setup ##

**Note - this feature is available from Impala 1.0 RC3**

Setup of load time weaving involves a number of steps.

1) You will need to obtain the necessary AspectJ libraries, which you can do
via an entry in _dependencies.txt_ such as the following.

```
aspectj from org.aspectj:aspectjrt:1.5.4
aspectj from org.aspectj:aspectjweaver:1.5.4
```

You will also need to make sure you have the necessary Spring AOP libraries (e.g. _spring-aop.jar_) and their dependents.

2) Set the Impala property `load.time.weaving.enabled=true`. Without this, you will get a warning message such as the following:
```
No-op implementation of 'addTransformer()' invoked. Use 'load.time.weaving.enabled=true' and start JVM with '-javaagent:/path_to_aspectj_weaver/aspectjweaver.jar' switch to enable load time weaving of aspects.
```

3) Add the JVM switch '-javaagent:/path\_to\_aspectj\_weaver/aspectjweaver.jar'.

4) Set up your aspects, either using annotations, or using an _aop.xml_ file, as described in the Spring documentation.

5) Add a '

&lt;context:load-time-weaver/&gt;

' entry into the module(s) for which load time weaving is required, also as described in the Spring documentation.

Note that the only Impala-specific step is 2). Aspects can be defined an woven on a per module basis, but as mentioned above, it is currently
not possible to weave aspects into classes already loaded by other modules in the application.

Note that aspects weaving follows the module load life cycle. For example, if Module A reloads, any change in the aspect configuration made to
Module A will be picked up and applied.
