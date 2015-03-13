## Background ##

Impala is simple to extend because Impala is simply a collection of Spring beans. This means you can easily add new Spring beans into the Impala
environment, or override the definitions of existing beans. You simply need to understand two things: first, how does Impala's extension mechanism work,
and secondly, you need to know a little bit about Impala's bean definitions to know what definitions to override.

## Setup ##

**1) Introduce new bean definitions**

The easiest way to do this is to add new Spring bean definition files to the application class path, which at runtime corresponds to the _/WEB-INF/classes_
directory. In terms of the development project structure, this would correspond to the _resources_ directory in the applications 'host' project.

If you need to write new classes to support your bean definitions, you can add these to the _src_ directory in the host project, and once again,
they will be visible on the web application classpath from the _/WEB-INF/classes_ directory.

**2) Make new bean definitions visible to Impala**

Suppose you add new bean definitions to a Spring configuration file called _custom-config.xml_

Simply add an entry to the _impala.properties_ file.

```
extra.locations=custom-config.xml
```

Note that you can have multiple additional Spring configuration files - simply specify these as a comma-separated list.

Note that bean definitions in the custom configuration files will override any already present in one of the built-in Impala configuration files.

For more information:
  * BootstrapContexts: Spring configuration files used for Impala's built-in configuration.
  * InternalInterfaces: describes some of Impala's internal interfaces.

See also [more detailed documentation on extending Impala](ExtendingImpala.md).


