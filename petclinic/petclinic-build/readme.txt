To run the application, follow these steps. First check out the source, then create an Eclipse workspace
from the checked out code.

    * svn co http://impala.googlecode.com/svn/trunk/petclinic petclinic
    * Open Eclipse, with the workspace set to the checkout directory (petclinic)

Now set up the database:

    * First, from petclinic/db, run createDB.txt (as root). This creates the petclinic user
      mysql -u root <>
    * Then as the petclinic user, insert the tables
      mysql -u petclinic -ppetclinic petclinic <>
    * To insert data, run
      mysql -u petclinic -ppetclinic petclinic <>

To run the tests from Eclipse, simply run the main class AllTests as a JUnit test, 
which is in the project petclinic-tests.

To run up the web application, simply run the main class StartServer, 
which is in the project petclinic-web. This time, run it as a (main) Java application.

To run up the interactive test runner, run HibernateClinicTest as a Java application. 
Type u for usage. You can run this class as a standard JUnit test - it's part of the AllTests suite.

This text is based on:
http://impalablog.blogspot.com/2008/01/springs-petclinic-sample-impala-style.html
