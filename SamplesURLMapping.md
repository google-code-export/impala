# URL mapping sample #

Demonstrates techniques for mapping requests to modules, and for mapping requests within modules to particular servlets and filters registered within the respective modules.

## Project setup ##

The easiest way to access the examples is to check them out directly from trunk.

```
svn co http://impala.googlecode.com/svn/trunk/urlmapping-sample urlmapping-sample
```

Then open an Eclipse workspace in the new _urlmapping-sample_ folder, and import all the projects into the workspace.

The workspace should look something like this:

![http://impala.googlecode.com/svn/wiki/images/url_mapping.png](http://impala.googlecode.com/svn/wiki/images/url_mapping.png)

## Run the sample ##

From within Eclipse, run StartServer as a web application. Then connect using the URL:

http://localhost:8080/