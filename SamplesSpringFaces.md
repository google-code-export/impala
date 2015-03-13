# Spring Faces sample #

The Petclinic sample is based on the Spring Faces hotel booking application, which features:

  * JPA for persistence
  * Spring Web Flow for the view controller
  * JSF (Facelets) for the view layer

The original (not Impala) version of the application is hosted online [here](http://richweb.springframework.org/swf-booking-faces/spring/intro). Use this to get an idea of what the application is supposed to look like. Of course, once its running in Impala it will look the same.

## Project setup ##

The easiest way to access the examples is to check them out directly from trunk.

Get the source using:

```
svn co http://impala.googlecode.com/svn/trunk/springfaces-sample springfaces-sample
```

Then open an Eclipse workspace in the new _spring-faces_ folder, and import all the projects into the workspace.

## Running the application ##

To run up the web application, simply run the main class StartServer,
which is in the project _springfaces-host_. This time, run it as a (main) Java application.

Connect using the URL http://localhost:8080/springfaces-host/