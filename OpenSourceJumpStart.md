### Showcase application ###
The Impala project has a number of sample projects, but none which has been specifically built from the ground up with Impala's capabilities in mind. The samples that do exist are those which have either been adapted from the Spring versions (Petclinic, the SWF Hotel Booking), or more basic applications which demonstrate more specific features.

What's required is an application which demonstrates Impala's core capabilities and best practices in a significant but not overwhelming enterprise application.

This application will consist of a number of modules and be built using Spring MVC and Hibernate (possibly JPA). Other technologies to be used may include JSP, JQuery (for JavaScript/AJAX features, where appropriate), Quartz (for scheduling), JavaMail (for email), etc.

If working on this task doesn't grab you, or seems a bit ambitious (it possibly will for the morning session), how about one of the following:

### Add/remove modules via JMX ###
This should be easier than it might sound. There are examples of other module operations available for JMX operations, and the core capability for these operations exist.
The task should be a matter of marshalling inputs and outputs via the Spring JMX integration mechanism.

### Logos/styles for Web Frameworks sample ###
Would be nice to add a little bit of styling to the existing 'Hello World' style pages in these samples, and at the same time, show how image/style resolution can work for resources hosted in modules for each of the supported web frameworks.

### Add another framework to the Web Frameworks Sample ###
Currently, there is a [web frameworks sample](SamplesWebframework.md) which supports Struts, Struts2, JSF, Tapestry and Wicket. Do you use another web framework which might be straightforward to integrate with Impala. Perhaps we can add this?

### Are you a Maven enthusiast? ###
Impala has a Maven plugin, but it would also be useful to have Maven archetypes specifically for Impala:
one which can be used to generate the Impala host web application.
one to add new modules to an existing application.
These archetypes could help with project quickstarts for Maven users.

### Are you a non-Eclipse user? ###
Impala has targeted Eclipse as an IDE. An outstanding task to determine what is required to make Impala work easily with other IDEs. What files need to be generated? What manual steps need to be  performed, etc. Are there any other serious gotchas.

### Are you a non-Tomcat or Jetty user? ###
Impala has targeted Tomcat as the primary testing environment for WAR file deployment. Jetty is also supported out the box, both in embedded mode, or using Jetty as a standalone server. Do you use other application servers �  in particular Weblogic and WebSphere. Some testing of one or more of the existing sample applications in these environments would be very helpful.

### Are you a Scala user? ###
Scala is a JVM language which is growing in popularity. In theory, it should be straightforward to deploy modules written in Scala in Impala. If you have some experience with Scala, why not give this task a go?

### Impala needs a logo ... ###
Are you a red hot graphic designer? Impala needs a logo to add to its project home page, to samples, and to presentations.

### Other ideas/suggestions? ###
Other thoughts/ideas/suggestions would be welcome. Please feel free to discuss.