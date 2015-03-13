## SVN Repository Location ##

http://impala.googlecode.com/svn/

## Workspace checkout options ##

When checking out source from SVN, you probably want to achieve two things:
  * get a bunch of file out of your repository
  * import the relevant projects into your IDE, so you can look at the code, run sample, etc.

If you are using Eclipse as your IDE, you basically have two options for doing this:

  1. use the SVN command line to check out the workspace, and then use Eclipse to import the projects
  1. check out the workspace directly from Eclipse

### Option 1: Check out via SVN and import into Eclipse ###

Simply use the following commands:
```
mkdir impala-workspaces
cd impala-workspaces
svn checkout http://impala.googlecode.com/svn/trunk/impala/ impala
```

This will create a workspace called _impala_. Now open Eclipse in this workspace, and import the projects.s

File -> Import -> General -> Existing Projects into Workspace

brings up the following dialog box which you can use to select the project to import.

![http://impala.googlecode.com/svn/wiki/images/impala_import.png](http://impala.googlecode.com/svn/wiki/images/impala_import.png)

### Option 2: Check out directly from Eclipse ###

I personally find it much easier to check out directly into Eclipse.

Open the SVN Repository Exploring perspective in Eclipse.
Add the location _http://impala.googlecode.com/svn/trunk/impala_

Then simply check out the projects as shown below:

![http://impala.googlecode.com/svn/wiki/images/impala_checkout.png](http://impala.googlecode.com/svn/wiki/images/impala_checkout.png)

### Discussion ###

I prefer to use the second method as it is simpler, and has the added benefit of being able to easily host
projects from different SVN locations in the same workspace.

## Checking out trunk ##

The current Impala _trunk_ repository consists of workspaces both for Impala as well as the sample applications.
YThe source is fairly large (I'll probably moving dependencies out, which will reduce its size),
so you probably won't want to check out the entire trunk in one go using the command

```
svn checkout http://impala.googlecode.com/svn/trunk/ impala-workspaces
```

Instead, you'd only want check out contained workspaces individually, using one of the
two options described above . For example, to check out the Petclinic sample

```
svn checkout http://impala.googlecode.com/svn/trunk/petclinic/ petclinic
```

## Checking out from a particular version ##

You will need to follow these instructions if you wish to **build a particular version from source**.

Each time a new Impala release is created, a new tag is created in the Impala SVN repository. For example, for the release 1.0M3, a new tag in the source was created
corresponding to that release is created in the location:

```
http://impala.googlecode.com/svn/tags/1.0M3
```

In order to retrieve source to build this release, simply check out as follows:

```
svn checkout http://impala.googlecode.com/svn/tags/1.0M3 impala-1.0M3-workspace
```

At this point, there is no intention to create source builds. However, source jars are created for each Impala class jar, and included as part of the binary release.