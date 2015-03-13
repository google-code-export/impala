## Background ##

Impala also supports reloading of modules using a JMX interface. This allows you to explicitly initiate a module reload,
and does not require you having to have any background monitoring of changes in module contents.

## Setup ##

When Impala starts up it automatically exposes a JMX bean under the key `impala:service=moduleManagementOperations`, which
includes an operation to invoke a module reload. Impala uses [Spring JMX](http://static.springsource.org/spring/docs/2.5.x/reference/jmx.html).

By default Impala will use Spring JMX to create a new JMX MBean server to host MBeans. You can tell Spring to locate an existing
MBean server using the _impala.properties_ entry.

```
jmx.locate.existing.server=true
```

By default you will be able to connect to the MBean server using [JConsole](http://java.sun.com/j2se/1.5.0/docs/guide/management/jconsole.html), but only from the local machine.

Other options for connecting to the JMX are as follows:

**1) Use the MX4J web interface**

To enable this, you need to add the [MX4J](http://mx4j.sourceforge.net/) libraries into your application's classpath.

Then add the following entries into _impala.properties_:

```
expose.mx4j.adaptor=true
jmx.adaptor.port=8003
```

This will start an HTTP server on port 8003. At present, this is not a secure interface, so should be used with caution.

If pointing the browser to port 8003, you will see the following.

![http://impala.googlecode.com/svn/wiki/images/mx4j-home.png](http://impala.googlecode.com/svn/wiki/images/mx4j-home.png)

Click on the 'impala:service=moduleManagementOperations' link and you get the shown below:

![http://impala.googlecode.com/svn/wiki/images/mx4j-moduleops.png](http://impala.googlecode.com/svn/wiki/images/mx4j-moduleops.png)

The screen above allows you to explicitly invoke a module reload.

**2) Add a JSR-160 Connector**

The [Spring JMX](http://static.springsource.org/spring/docs/2.5.x/reference/jmx.html) explains how to set up a JSR-160 connector to your MBean server.
You can easily add the necessary support by adding an extra Spring beans to your Impala configuration:

```
extra.locations=custom-jmx-config.xml
```

Simply deploy the additional Spring bean configuration file in your web application's class path (e.g. under _/WEB-INF/classses_).

Note that this technique can be used to customise the creation of your MBean server and other aspects of the JMX configuration.

See [more details on how to extend Impala](HowToExtendImpala.md).