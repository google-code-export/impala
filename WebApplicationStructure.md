In ModuleStructure the overall structure of an Impala project is described.

Still confused on the how the Impala property configuration files relate to each other. Read on.

1.
The context listener defined in the _web.xml_ is invoked. This is based on the presence in _web.xml_
of a Spring context loader listener.

```
<listener>
    <listener-class>org.impalaframework.web.spring.loader.ImpalaContextLoaderListener</listener-class>
</listener>
```

2.
The _impala.properties\_� file is read (when running in embedded mode_impala-embedded.properties_is used) to get the built in Impala config
files plus any other files which are part of the Impala bootstrapping
configuration_

3.
The _moduledefinitions.xml_ is read to determine which modules to load. The correct load order of the modules is determined from the
relationship between the modules (determined, for example, using the `parent` and `depends-on` properties in module.properties). At this
point, Impala will also figure out the metadata for each module (what type of module, which configuration files to load, any other module properties,
etc.)

Now, Impala is instantiated, but the application has not been loaded.

4.
Modules are then loaded in the correct order. For each module, the Spring config files are read, beans are instantiated, contributed to the
service registry, imported from the service registry, etc.