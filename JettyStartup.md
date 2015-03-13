Impala applications can be deployed as WAR files on an application server such as Tomcat. Also, it can be distributed with an embedded Jetty server.

A more complex example of Jetty startup is shown below.

```
startjetty.sh 8443 /web sysprop.properties ../config https
```

The arguments used are as follows:

  * **port** _(8443)_ - the port on which the application will start
  * **context path** _(/web}_ - the context path used for the application. For example, in the example above, the application will be accessible from the base URL _https://hostname:8443/web_.
  * **system properties file** (optional) _(sysprop.properties)_ - a file which contains system properties which will be added set on startup.
  * **config directory** (_../config_) - a directory which contains application property files, such as _jdbc.properties_. Takes advantage of a mechanism supported in Impala which allows property files to be external to the application, so that the property values can survive upgrades without requiring user intervention.
  * **scheme** (_https_) - the scheme to be used, typically either _http_ or _https_. In the example above, the application is run over a secure socket on port 8443.