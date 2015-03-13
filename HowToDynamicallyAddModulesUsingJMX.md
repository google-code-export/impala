This page describes how to dynamically add modules at runtime.

You will need to familiarize yourself with the mechanism for [dynamically reloading modules using JMX](HowToReloadModulesUsingJMX.md).

Also note how the [module definitions file](ModuleConfiguration.md) can be used to define modules to be included in your application runtime.

The JMX operation `reloadModules` can be used to reload this configuration file. This will dynamically add or remove modules as per the changed configuration.

The example below shows this being done using JConsole.

![https://impala.googlecode.com/svn/wiki/images/jmx-reload.png](https://impala.googlecode.com/svn/wiki/images/jmx-reload.png)