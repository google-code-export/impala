## Background ##

Impala features a built-in [ANT-based build system](BuildSystem.md) which allows you to set up a build environment for your project with minimal effort
by reusing the existing build scripts provided by Impala. For early versions of Impala, this was the only way to build your project.
From about 1.0 RC2, the Impala project structure conventions have been modified (generally simplified) to make them simpler to use with Maven,
the other widely used Java-based build system. This page describes how to make your Impala project work effectively with Maven.

Before commencing, it would be worth reading the Maven [getting started](http://maven.apache.org/guides/getting-started/index.html) tutorials,
and the tutorial on setting a [multi-module Eclipse project](http://maven.apache.org/guides/mini/guide-ide-eclipse.html).

## Setup ##

The use of Maven with Impala is demonstrated in the [Maven Sample](SamplesMaven.md), shown below.

![http://impala.googlecode.com/svn/wiki/images/maven_project.png](http://impala.googlecode.com/svn/wiki/images/maven_project.png)

The examples consist of the following projects:
  * _maven-build_: consists of the parent POM, which is used to determine which modules are includes as part of the build.
  * _maven-host_: a standard Java web project which functions as the host project for the web application.
  * _maven-main_: the 'root' Impala module, which typically contains the main interfaces and shared domain classes used widely in the application.
  * _maven-module_: a single Impala implementation module. In a real-world application, there would be several, even many of these.
  * _maven-web_: an Impala web module. Depends on _maven-main_, but not on _maven-module_. In a real-world application, there would be several or many of these.

### maven-build ###

This project contains the parent POM, so contains shared entries, such as repositories used.
Since this is the parent of all other modules, it is also a convenient location for external dependency entries.
An excerpt of the _maven-build_ POM is shown below:

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                      http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>org.impalaframework.samples.maven</groupId>
	<version>1.0</version>
	<artifactId>maven-build</artifactId>
	<packaging>pom</packaging>
	<properties>
		<spring.version>3.0.0.RELEASE</spring.version>
		<application.version>1.0-SNAPSHOT</application.version>
		<impala.version>1.0-SNAPSHOT</impala.version>
	</properties>
	<name>maven-build Parent build project for multi-module Impala build</name>
	<modules>
		<module>../maven-main</module>
		<module>../maven-module</module>
		<module>../maven-web</module>
		<module>../maven-host</module>
	</modules>	
	<repositories>
		<repository>
			<id>Impala</id>
			<url>http://oss.sonatype.org/content/repositories/impalaframework-snapshots</url>
		</repository>
		<repository>
			<id>Spring</id>
			<url>http://maven.springframework.org/milestone</url>
		</repository>
	</repositories>
	<dependencies>
		<dependency>
			<groupId>org.impalaframework</groupId>
			<artifactId>impala-core</artifactId>
			<version>${impala.version}</version>
		</dependency>
...
		<dependency>
			<groupId>org.impalaframework</groupId>
			<artifactId>impala-interactive</artifactId>
			<version>${impala.version}</version>
			<scope>test</scope>
		</dependency>
	</dependencies>
...
</project>
```

### maven-host ###

The Maven host project is just a standard web application. We can see from the screenshot that this project contains a
_WEB-INF/web.xml_ file. The big difference is that it doesn't contain any application files. These are contained within the
various modules.

Note that the classes contained within _maven-host_ _src_ directory will be
deployed in _WEB-INF/classes_ and will be loaded using the standard web application class loader, as with any standard web application.
As such, these classes are visible to classes contained within the modules _maven-main_, _maven-module_ and _maven-web_, but not
vice versa.

### maven-main ###

This project is the root Impala module. In our example, it simply contains the `MessageService` interface, and
related Spring application context definitions.

```
public interface MessageService {
    public String getMessage();  
}
```

There is nothing special about this module's POM. It defines the _maven-build_ project as parent, and
relies on the `dependencies` declarations there for third party dependencies.

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<parent>
		<groupId>org.impalaframework.samples.maven</groupId>
		<artifactId>maven-build</artifactId>
		<version>1.0</version>
	</parent>
	<modelVersion>4.0.0</modelVersion>
	<artifactId>maven-main</artifactId>
	<version>${application.version}</version>
	<packaging>jar</packaging>
	<name>maven-main - Maven root application module</name>
</project>
```

### maven-module ###

This module contains the implementation of `MessageService`, as well as related Spring application context definitions.

```
public class MessageServiceImpl implements MessageService {

    public String getMessage() {
        return "Hello World, Maven style";
    }
}
```

The POM for this module is very similar to that of _maven-main_, with the difference that the
it also defines an additional dependency on _maven-main_, reflecting the fact that a module level
dependency exists between _maven-module_ and _maven-main_.

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.impalaframework.samples.maven</groupId>
	<artifactId>maven-build</artifactId>
    <version>1.0</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <artifactId>maven-module</artifactId>
  <version>${application.version}</version>
  <packaging>jar</packaging>
  <name>maven-module - Maven application module</name>
  <!-- Dependencies shared by sub-projects -->
  <dependencies> 
    <dependency>
      <groupId>org.impalaframework.samples.maven</groupId>
      <artifactId>maven-main</artifactId>
      <version>${application.version}</version>
    </dependency>
  </dependencies>
</project>
```

### maven-web ###

This module contains the web bits of the application, including the controllers, JSPs, etc.

The POM for this module mirrors that of _maven-module_, and is shown below.

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.impalaframework.samples.maven</groupId>
	<artifactId>maven-build</artifactId>
    <version>1.0</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <artifactId>maven-web</artifactId>
  <version>${application.version}</version>
  <packaging>jar</packaging>
  <name>maven-web - Maven web module</name>
  <dependencies> 
    <dependency>
      <groupId>org.impalaframework.samples.maven</groupId>
      <artifactId>maven-main</artifactId>
      <version>${application.version}</version>
    </dependency>
  </dependencies>
</project>
```