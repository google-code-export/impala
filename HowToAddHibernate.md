## Background ##

This page describes how to add Hibernate support to your project. The recommended practice is to add Hibernate support in its own module.
Reloading a Hibernate `SessionFactory` can be a relatively time-consuming part of the overall project reload, so it's a good idea to
separate the module which contains the `SessionFactory` from the one which contains the application DAOs.

## Setup ##

**1) Add a new module**

Add a new 'empty' module to your project, as described in AddingModules.

You will want to give the module a name which clearly identifies it as a 'Hibernate' module.

**2) Add the Hibernate libraries to your project**

You can bring these into your project using an entry such as the following in _dependencies.txt_.
Note that you will also need to add these to your host project's class path.

```
compile from antlr:antlr:2.7.6
compile from cglib:cglib-nodep:2.1_3
compile from commons-collections:commons-collections:2.1.1
compile from dom4j:dom4j:1.6.1
compile from net.sf.ehcache:ehcache:1.3.0
compile from org.hibernate:hibernate:3.2.3.ga
compile from hsqldb:hsqldb:1.8.0.7 
```

**3) Add the Hibernate Spring beans to your module's Spring configuration file**

For example, the _petclinic-hibernate_ module in the [Petclinic Sample](SamplesPetclinic.md) contains the following entry.

```
<bean id="propertyConfigurer"
    class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="locations">
        <list>
            <value>classpath:mysql.properties</value>
        </list>
    </property>
</bean> 

<bean id="dataSource"
    class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="driverClassName"
        value="${jdbc.driverClassName}" />
    <property name="url" value="${jdbc.url}" />
    <property name="username" value="${jdbc.username}" />
    <property name="password" value="${jdbc.password}" />
</bean>

<bean id="sessionFactory" class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
    <property name="dataSource"><ref local="dataSource"/></property>
    <property name="mappingResources">
        <value>petclinic.hbm.xml</value>
    </property>
    <property name="hibernateProperties">
        <props>
            <prop key="hibernate.dialect">${hibernate.dialect}</prop>
            <prop key="hibernate.show_sql">true</prop>      
        </props>
    </property>
    <property name="eventListeners">
        <map>
            <entry key="merge">
                <bean class="org.springframework.orm.hibernate3.support.IdTransferringMergeEventListener"/>
            </entry>
        </map>
    </property>
</bean>

<bean id="transactionManager" class="org.springframework.orm.hibernate3.HibernateTransactionManager">
    <property name="sessionFactory"><ref local="sessionFactory"/></property>
</bean>
```

**4) Export the relevant beans**

Typically, you will want to export the `DataSource`, `SessionFactory` and `PlatformTransactionManager` beans, using one of the
[Impala service namespace](NamespaceReferenceService.md). For example.

```
<service:export-array beanNames="dataSource,sessionFactory,transactionManager"/>
```

**5) Import the exported beans where needed**

Use one of the [Impala service namespace](NamespaceReferenceService.md) import beans to bring these into other
modules in your application, using entries such as the following:

```
<service:import id="dataSource" exportName = "dataSource" proxyTypes = "javax.sql.DataSource"/>
    
<service:import id="sessionFactory" exportName = "sessionFactory" proxyTypes = "org.hibernate.SessionFactory"/>
    
<service:import id="transactionManager" exportName = "transactionManager" proxyTypes = "org.springframework.transaction.PlatformTransactionManager"/>
```

This creates proxies which can be injected into other beans in your application.