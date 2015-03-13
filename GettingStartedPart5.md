# Building and Deploying #

One thing that you may have noticed in this getting started series is that, apart from a few scripts in [part one](GettingStartedPart1.md), we haven't had to leave the safety and comfort of Eclipse to perform any of our development tasks. This is great, becausing leaving the IDE slows you down, but there comes a time when you want to make your application available to real some real users. For this, you will need to deploy your application outside of your IDE. Which of course means, you will need to build and deploy your application.

Impala comes with some built-in support for build automation. Controversially to some perhaps, the build support is based on ANT. ANT is not the sexiest of tools, but it works, it's easy to use, and if you careful about the way you set up your build scripts, you can even write build scripts which are maintainable and well factored.

## Build setup ##

If you're happy to live without Impala's build support, you can simply add the Impala jars into your project's jar repository and forget about any of the other files that come with the Impala distribution. In order to use the Impala build system, you will need to download the Impala distribution.
The latest release can be downloaded [from the project site](http://code.google.com/p/impala/downloads/list). As we recall from [part one](GettingStartedPart1.md), you will need to download and unzip this distribution, and set your `IMPALA_HOME` environment variable to point to the unzipped contents folder.

## Getting library dependencies ##

### Getting third party dependencies ###

A typically problematic aspect of new project setup is obtaining third party libraries. Impala has a simple but effective approach this problem, which takes advantage of the existence of Maven repositories.

Obtaining third party dependencies is a three step process:
  * Figure out what the dependencies are. You can do this from the Maven POM of particular libraries you are interested, from previous experience working with particular libraries, from the web sites of the libraries themselves, or from another trusted source, such as the Spring framework distribution.
  * Create a _dependencies.txt_ file, which you place in the root directory of the project which brings in the dependencies.
  * Run the command `ant get` from the same project.

In the generated sample application, the _dependencies.txt_ in the _main_ project looks as follows:

```
compile from org.slf4j:jcl-over-slf4j:1.5.8
compile from log4j:log4j:1.2.15
compile from org.springframework:spring-core:3.2.2.RELEASE
compile from org.springframework:spring-beans:3.2.2.RELEASE
compile from org.springframework:spring-context:3.2.2.RELEASE
compile from org.springframework:spring-context-support:3.2.2.RELEASE
compile from org.springframework:spring-aop:3.2.2.RELEASE
compile from org.springframework:spring-expression:3.2.2.RELEASE
compile from org.springframework:spring-web:3.2.2.RELEASE
compile from aopalliance:aopalliance:1.0
runtime from cglib:cglib-nodep:2.2
runtime from org.slf4j:slf4j-api:1.5.8
runtime from org.slf4j:slf4j-log4j12:1.5.8
test from junit:junit:3.8.2
test from org.easymock:easymock:2.4 
test from org.easymock:easymockclassextension:2.4
```

The  _dependencies.txt_ in the _web_ project contains the following entries:

```
compile from org.springframework:spring-webmvc:3.2.2.RELEASE
provided from javax.servlet:servlet-api:2.5
provided from javax.servlet.jsp:jsp-api:2.1
provided from org.mortbay.jetty:jetty:6.1.21
provided from org.mortbay.jetty:jetty-util:6.1.21
provided from org.apache.tomcat:jasper:6.0.20
provided from org.apache.tomcat:jasper-el:6.0.20
provided from org.apache.tomcat:jasper-jdt:6.0.20
provided from org.apache.tomcat:juli:6.0.20
provided from xerces:xercesImpl:2.9.1
provided from commons-el:commons-el:1.0
```

Consider the entry
```
main from commons-logging:commons-logging:1.1
```

This is shorthand for saying that I want the Maven repository jar file with organisation id `commons-logging`, artifact id `commons-logging` and version number `1.1` to be downloaded and placed into the workspace's repository folder _main_. Note that our workspace repository in found in the project _repository_, with _main_ a folder relative to this location.
The ability to specify the download location in this way makes it easy to apply rules to include or exclude certain jars from ending up in built artifacts, such as war files. For example, the EasyMock dependencies are clearly present for testing, so there would be no point bundling these into a war file. Similarly, the Jetty dependencies are clearly not required in a war file. The whole point of a war file is that it can be simply dropped into a web container, so it wouldn't make much sense including Jetty server jars in such an artifact.

### Getting Impala dependencies ###

If you've gone to the trouble of downloading the Impala distribution and setting up your `IMPALA_HOME` environment variable, it wouldn't be very helpful having to go onto the internet to download Impala jars. The quick start application features a simple command to retrieve Impala jars, and place them in the _main_ directory of your workspace's repository:

```
ant fetch
```

## Build project build file ##

Let's take a look at the _build_ project build file which you get with the generated quickstart application:

```
<?xml version="1.0"?>
<project name="Build build" basedir="."> 
    
    <property environment="env"/>
    <property name = "workspace.root" location = ".."/>
    <property name = "impala.home" location = "${env.IMPALA_HOME}"/>

    <echo>Project using workspace.root: ${workspace.root}</echo>
    <echo>Project using impala home: ${impala.home}</echo>

    <property file = "build.properties"/>
    <property file = "web.properties"/>
    <property file = "shared.properties"/>
    
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

As you can see, there is not much to it. Most of the entries are to define environment properties and to import helper build scripts from the Impala home directory. There are also a few convenience targets towards the end of the script.

In addition, there are three property files which locally define properties used for the build.

The first two are _build.properties_ and _shared.properties_ first:

build.properties:
```
project.name=build
project.list=main,module1,web
web.project.list=host
#uncomment to run in debug mode
#run.jvm.args=-Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y
#test.jvm.args=-Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y
```

shared:properties
```
base.repository.urls=file://${user.home}/.m2/repository,http://repo1.maven.org/maven2/,http://ibiblio.org/pub/packages/maven2/
project.version=1.0
repository.dir=${workspace.root}/repository
```

These properties are useful, because they control aspects of the build. For example:
  * `base.repository.urls`: controls the maven repository locations used in the `get` target described earlier. Note that we have specified locations for both the remote as well as the local Maven repositories.
  * `project.list`: denotes the projects which are controlled by the main build file.

The third locally defined properties file is _web.properties_, shown below:

```
webapp.project.name=host
context.path=/${webapp.project.name}
context.dir=../${webapp.project.name}/context
webapp.port=8080
tomcat.home=${env.TOMCAT_HOME}
```

A recommended approach is to set the `TOMCAT_HOME` environment variable to your Tomcat installation directory.

On Unix based systems you can do so using the command:
```
export TOMCAT_HOME=/mypathtoTomcat/
```

## Building a war, and deploying to Tomcat ##

The main build file defines a bunch of convenience methods. To build a war file using the generated quick start application:

```
ant clean dist war
```

If the Tomcat home directory is correctly specified, we can deploy directly to Tomcat using the following command:

```
ant tomcat
```

Place a command prompt at the Tomcat bin directory, and start Tomcat.

```
cd ${TOMCAT_HOME}/bin
./catalina.sh run
```

This will start Tomcat. Once again, point the browser to

```
http://localhost:8080/web/message.htm
```

and we'll end seeing the same result as in [part three](GettingStartedPart3.md)

## War project structure ##

We can end the discussion with a brief look at the structure of the exploded war file in the Tomcat _webapps/web_ application directory. Apart from the _test.jsp_, all the deployed files are contained in _WEB-INF_.

```
WEB-INF/classes:
    impala-embedded.properties    
    impala.properties        
    log4j.properties        
    moduledefinitions.xml

WEB-INF/lib:
	spring-core-3.2.2.RELEASE.jar
	spring-context-3.2.2.RELEASE.jar
	spring-webmvc-3.2.2.RELEASE.jar
	spring-web-3.2.2.RELEASE.jar
	spring-beans-3.2.2.RELEASE.jar
	impala-core-1.0.3.jar
	log4j-1.2.15.jar
	spring-aop-3.2.2.RELEASE.jar
	cglib-nodep-2.2.jar
	spring-expression-3.2.2.RELEASE.jar
	impala-web-1.0.3.jar
	spring-context-support-3.2.2.RELEASE.jar
	impala-interactive-1.0.3.jar
	impala-build-1.0.3.jar
	impala-command-1.0.3.jar
	slf4j-api-1.5.8.jar
	jcl-over-slf4j-1.5.8.jar
	impala-jmx-1.0.3.jar
	slf4j-log4j12-1.5.8.jar
	launcher.jar
	startjetty.jar
	aopalliance-1.0.jar

WEB-INF/modules:
    main-1.0.jar        
    module1-1.0.jar        
    web-1.0.jar        
```

_WEB-INF/classes_ contains the files from the _web_ project's _webconfig_ directory. Note that these files are all found in the web application class path. _WEB-INF/lib_ contains all the third party jars, including the Impala jar files. Finally, the modules are contained in jar files in _WEB-INF_ modules. The reason for this location is that the module jars need to be loaded using a different class loader from the third party jars, and therefore should not be on the web application's class path.

## Jetty Deployment ##

The advantage of using a Jetty deployment is that you can easily "ship" the Jetty container with your application as a single zip file. This makes it possible to literally unzip and run in the target environment, without requiring a web container (e.g. Tomcat) to be separately installed.

To build to Jetty, follow these instructions:

From the main project:

```
ant clean dist
```

From the web project:

```
ant jetty:zip
```

This results in a zip file to be copied to the location _../deploy/%hostprojectname%-jetty.zip_.

You can then unzip this file. CD to the folder containing _startjetty.sh_ and _startjetty.bat_. Run the relevant script. For example.

```
startjetty 8080 web
```

starts the Jetty web container on port 8080.

You can then browse to the running application on: http://localhost:8080/web/message.htm

There are some useful configuration options which can be applied, for example, to set system properties and to tweak Impala startup options, but this this basic configuration will get you going without pain.

For more details, on deploying and starting up Impala aplications in Jetty, see [JettyStartup](JettyStartup.md).