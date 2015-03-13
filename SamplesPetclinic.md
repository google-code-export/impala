# Petclinic sample #

The Petclinic sample is based on the Spring Petclinic 2.0.x sample, with a few minor modifications. The principal modification is that the application has been divided into a number of modules:

  * a _main_ module
  * a _hibernate_ module
  * a _dao_ module
  * a _web_ module

## Project setup ##

The easiest way to access the examples is to check them out directly from trunk.

Get the source using:

```
svn co http://impala.googlecode.com/svn/trunk/petclinic-sample petclinic-sample
```

Then open an Eclipse workspace in the new _petclinic-sample_ folder, and import all the projects into the workspace.

## Database setup ##

Because this example uses the MySQL database, you will need this installed.

Now set up the database:
  * First, from petclinic/db, run createDB.txt (as root). This creates the petclinic user
> > `mysql -u root < createDB.txt`
  * Then as the petclinic user, insert the tables
> > `mysql -u petclinic -ppetclinic petclinic < initDB.txt`
  * To insert data, run
> > `mysql -u petclinic -ppetclinic petclinic < populateDB.txt`

## What to run ##

To run the tests from Eclipse, simply run the main class `AllTests` as a JUnit test,
which is in the project _petclinic-tests_.

To run up the web application, simply run the main class StartServer,
which is in the project _petclinic-host_. This time, run it as a (main) Java application.

Connect using the URL http://localhost:8080/petclinic-host/

To run up the interactive test runner, run `HibernateClinicTest` as a Java application.
Type u for usage. You can run this class as a standard JUnit test - it's part of the `AllTests` suite.

**Note**: this text is based on:
http://impalablog.blogspot.com/2008/01/springs-petclinic-sample-impala-style.html