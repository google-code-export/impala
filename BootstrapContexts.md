An advanced user of Impala may want to introduce new functionality or customise the way that Impala is loaded.
Here, you have two choices.

  * You can add to the existing set of Impala Spring context locations using the **_extra.locations_** property.
  * Alternatively, you can specify precisely which Impala bootstrap context locations will be used, using the **_all.locations_** property.
In this case, any other configuration settings you may have applied will be ignored for the purposes of selecting
Impala bootstrap Spring context locations.

Both the properties _extra.locations_ and _all.locations_ are also described in PropertyConfiguration.

Prior to Impala 1.0M5 explicit choice of bootstrap locations was the only one available for configuring Impala startup locations.
This approach is not recommended, however, for the typical user, and even for the advanced user, a combination of Configuration Properties
and _extra.locations_ would be the preferred approach.

The example below show the use of the _all.locations_ property, which we explain in a bit more detail next.
Note that prior to Impala 1.0M5, the property `all.locations` was named `bootstrapLocations`.

```
all.locations=bootstrap,web-bootstrap,jmx-bootstrap,web-jar-module-bootstrap
application.version=SNAPSHOT
```

The `all.locations` property can be used to fine tune the Spring context locations, as a comma separated list.
The first entry in this list corresponds with the entry _META-INF/impala-bootstrap.xml_.
Note that there is a convention that is used which makes listing more concise. if the entry ends with _.xml_, then the full entry is used as is.
However, if it does not, then it is assumed to have the prefix _META-INF/impala-_ and the suffix _.xml_.

The example above is based on modules deployed as jar files in the _WEB-INF/modules_ directory, using the convention _%MODULE\_NAME%-%application.snapshot%.
The_petclinic-hibernate_module would in this example be found in_WEB-INF/modules/petclinic-hibernate-SNAPSHOT.jar_._

Of course, this scheme would not work running embedded
in Eclipse with modules deployed directly as expanded folders on the file system
The configuration required for this setup is contained in
`impala-embedded.properties`, also taken from the Petclinic sample:

```
#This entry is suitable for auto-reloading
all.locations=bootstrap,web-bootstrap,jmx-bootstrap,web-listener-bootstrap

#This entry is suitable for using the MX4J console
#all.locations=bootstrap,web-bootstrap,jmx-bootstrap,jmx-adaptor-bootstrap,web-jmx-bootstrap

#This entry allows JMX access via JConsole only
#all.locations=bootstrap,web-bootstrap,jmx-bootstrap,web-jmx-bootstrap
```

Notice that there are three configurations setup in this file, although obviously only one is active.
The first supports monitoring of module directories for changes, and automatic redeployment of modules.
The second supports module reloading through a JMX console. It also fires up a MX4J web console which can be used for this purpose.
The third option supports JMX reloading of modules, but does not start up the MX4J web application. The built-in JConsole can still be
used, however.

So how do we tell Impala to use `impala-embedded.properties` instead of the default `impala.properties`?
The answer is found in the class `StartServer`, which is used to start up the Jetty server embedded in Eclipse:

```
public class StartServer {
	public static void main(String[] args) {
		System.setProperty("org.mortbay.log.class", StdErrLog.class.getName());
		System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM, "classpath:impala-embedded.properties");
		StartJetty.main(new String[]{"8080", "../petclinic-web/context", "/petclinic-web"});
	}
}
```

The system property `bootstrapLocationsResource` is used for this purpose.

### Configuration Files ###

A list of the Spring configuration files used to set up Impala in a web application are as follows:

  * _impala-bootstrap.xml_: sets up the core Impala runtime.
  * _impala-graph-bootstrap.xml_: adds graph-based module support to Impala. Without this file present, only hierarchical modules and class loaders are supported.
  * _impala-web-bootstrap.xml_: adds web application support to Impala. This allows, for example, for modules to be `ServletContext`-aware.
  * _impala-web-moduleaware.xml_: adds module awareness to `ServletContext`, `HttpServletRequest` and `HttpSession` classes. This allows, for example, partitioning of `ServletContext` state by module, and for `HttpSession`s objects created within reloaded modules to survive across module reloads.
  * _impala-jmx-bootstrap.xml_: adds JMX support so that modules can be reloaded via interaction with a JMX console.
  * _impala-jmx-adaptor-bootstrap.xml_: adds functionality can be accessed via MX4J's web JMX console. If this file is not present, but _impala-jmx-bootstrap.xml_ is present, then a remote JMX console needs to be used (for example, JConsole).
  * _impala-web-listener-bootstrap.xml_: adds resource monitoring functionality which automatically reloads modules when detecting file system changes.
  * _impala-osgi-bootstrap.xml_: overrides core functionality to use OSGi class loader and runtime instead of Impala's native runtime. This feature is still experimental at this point.

**Note**:
For configuring the built-in capabilities of Impala, use the [configuration properties](PropertyConfiguration.md).

For more details on extending Impala, see ExtendingImpala.