# Introduction #

The following are some manual steps you may want to follow to get Hibernate up and running once you have created a new Hibernate module.

The steps are really just indicative.
Once you're familiar with Impala and how things fit together, you will probably want to customise the steps so they fit in more closely with your own application and environment,
and you'll certainly want to take them 'with a pinch of salt'.

(Remember: the project generation features in Impala were never supposed to be a substitute for Grails- or Rails-style scaffolding.)

**1)** Add the new module you have created to the _main/build.properties_ `project.list` property.
For example, if the module is called _hibernate_, the entry will be changed to the following:

```
project.list=main,module1,web,hibernate
```

The purpose of this is to include the module's project in subsequently invoked build scripts, such as the one that follows.

**2)** Run `ant get`
This will download the Hibernate dependencies from the pre-configured Maven repositories.

**3)** Refresh repositories project in Eclipse
Pull these into the classpath via Eclipse, as show below.

![http://impala.googlecode.com/svn/wiki/images/hibernate_libraries.png](http://impala.googlecode.com/svn/wiki/images/hibernate_libraries.png)

Don't forget to export the refreshed entries using the 'Order and Exports'.

**4)** Add a new domain entity classes in _main/src/domain_. This will correspond with the package `domain`. You can rename the package later.

```
package domain;

public class ApplicationProperty extends BaseDomainObject {

	private static final long serialVersionUID = 1L;
	private String name;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String type) {
		this.value = type;
	}
}
```

and

```
package domain;

public class BaseDomainObject  {

	private static final long serialVersionUID = 1L;
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BaseDomainObject other = (BaseDomainObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
```

Of course, you're free to edit these files as you wish. They are just convenient starting points.

**5)** Set up your database. For now, assume we're using the _testdb_ database in MySQL.

Of course, if you're using a different database, the commands will be different.

```
drop database if exists testdb;
create database if not exists testdb;

grant all privileges on testdb.* to testdb@"%" identified by 'testdb';
grant all privileges on testdb.* to testdb@localhost identified by 'testdb';
flush privileges;
```

**6)** Edit _resources/jdbc.properties_ in the Hibernate module to reflect the database settings. Here's an example.

```
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost/testdb?autoReconnect=false&useUnicode=true&characterEncoding=utf-8
jdbc.username=testdb
jdbc.password=testdb
hibernate.dialectName=org.hibernate.dialect.MySQL5InnoDBDialect
```

**7)** Create your Hibernate mapping files and domain objects, as required. You can start,
for example, by uncommenting the `sessionFactory` bean's `mappingResources` property:

```
<property name="mappingResources">
	<list>
		<!-- 
		Uncomment this after creating the corresponding domain class
		<value>domain/ApplicationProperty.hbm.xml</value>
		-->
	</list>
</property>
```

**8)** Add the following entries to the main module's _parent-context.xml_ Spring
configuration file.

```
 <bean id="dataSource" class="org.impalaframework.spring.module.ContributionProxyFactoryBean">
	 <property name = "proxyInterfaces" value = "javax.sql.DataSource"/>
 </bean>
	
 <bean id="sessionFactory" class="org.impalaframework.spring.module.ContributionProxyFactoryBean">
	 <property name = "proxyInterfaces" value = "org.hibernate.SessionFactory"/>
 </bean>
```

This will make the `dataSource` and `sessionFactory` available to submodules.

**9)** Flesh out the integration test `MappingIntegrationTest`. If you can run this successfully, then you are probably up and running.

**10)** Add your DAO module and service modules.

You've probably got the hang of the idea of adding a new module by now. At this point, you'd probably want to add a new DAO module so that
you can do something useful with your Hibernate mappings, such as saving and updating objects, and issuing Hibernate queries.

It's a good idea to separate your DAO from your Hibernate module, because this gives you the flexibility of adding new persistence operations
(in your DAO module) without having to refresh your Hibernate session factory (in your Hibernate module).

Once you've created a couple of basic persistence operations, you can start adding service operations, either into new or existing modules, which
use the DAOs.
