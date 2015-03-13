Impala comes with a highly reusable build system, based (surprisingly to some) on ANT.

The build system will be available if you install Impala locally, that is, if you
download Impala and set up an environment variable `IMPALA_HOME` pointing to the install location.

You don't need to use Impala's build system to use Impala. In fact, you don't need any
build support in place if you simply want to test your application in the IDE.
However, you will need build support to deploy your application externally, and for this
you may want to take a look at Impala's build system.

## Master Build project ##

The default when creating a new Impala project is to create it with a _build_ project, which ]
is in effect the master build project. Each project can be built separately and individually -
but only the master build project is able to build all of the projects in the application.

Let's consider the build XML in the master build project.

```
<?xml version="1.0"?>
<project name="Build main" basedir=".">	
	
	<property environment="env"/>
	<property name = "workspace.root" location = ".."/>
    <property name = "impala.home" location = "${env.IMPALA_HOME}"/>

	<echo>Project using workspace.root: ${workspace.root}</echo>
	<echo>Project using impala home: ${impala.home}</echo>
	
	<property file = "build.properties"/>
	<property file = "web.properties"/>
	
	<import file = "${impala.home}/project-build.xml"/>
	<import file = "${impala.home}/shared-build.xml"/>
	<import file = "${impala.home}/shared-web-build.xml"/>
	<import file = "${impala.home}/shared-tomcat-build.xml"/>
	<import file = "${impala.home}/download-build.xml"/>
	<import file = "${impala.home}/repository-build.xml"/>
	<import file = "${impala.home}/execution-build.xml"/>
	
	<target name = "get" depends = "shared:get" description="Gets project dependencies as in dependencies.txt"/>
	<target name = "fetch" depends = "repository:fetch-impala" description="Fetches Impala libraries"/>
	<target name = "clean" depends = "shared:clean" description="Cleans all projects"/>
	<target name = "dist" depends = "shared:all-no-test" description="Builds jars for projects, and copies to repository's dist directory"/>
	<target name = "test" depends = "shared:test" description="Runs tests"/>
	<target name = "jetty" depends = "execution:jetty" description="Runs Jetty in ANT"/>
	<target name = "war" depends = "shared-web:war" description="Do war file build"/>
	<target name = "tomcat" depends = "shared-tomcat:copy" description="Deploy war to Tomcat"/>
	
</project>
```