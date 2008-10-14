1) Add 
,hibernate to project.list

{{{
project.list=main,module1,web,hibernate
}}}

2) Run ant get
Downloads any dependencies expressed for that module

3) Refresh repositories project in Eclipse
Pull these into the classpath via Eclipse. Don't forget to export

4) Add domain entities in main/src/domain (you can rename the package later)

{{{
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
}}}

and 

{{{
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
}}}

5) TO set up the database, run the following

drop database if exists testdb;
create database if not exists testdb;

grant all privileges on testdb.* to testdb@"%" identified by 'testdb';
grant all privileges on testdb.* to testdb@localhost identified by 'testdb';
flush privileges;

6) Edit JDBC properties

{{{
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost/testdb?autoReconnect=false&useUnicode=true&characterEncoding=utf-8
jdbc.username=testdb
jdbc.password=testdb
hibernate.dialectName=org.hibernate.dialect.MySQL5InnoDBDialect
}}}

7) Create your hibernate mapping files and domain objects, as required

e.g.

Uncomment the entry in hibernate-context.xml

8) Add the following entries to parent-context.xml

 <bean id="dataSource" class="org.impalaframework.spring.module.ContributionProxyFactoryBean">
	 <property name = "proxyInterfaces" value = "javax.sql.DataSource"/>
 </bean>
	
 <bean id="sessionFactory" class="org.impalaframework.spring.module.ContributionProxyFactoryBean">
	 <property name = "proxyInterfaces" value = "org.hibernate.SessionFactory"/>
 </bean>

9) Add the integration test to the suite

