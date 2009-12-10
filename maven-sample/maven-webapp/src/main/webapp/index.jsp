<html>
<head>
</head>
<body>
<b>Welcome to the Impala Maven application</b><br/>

<p>This is the <i>Hello World</i> of Impala, built using Maven:</p>

<ul>
<li>A root or main module, which contains interfaces such as <code>MessageService</code> and core domain classes.
This module is contained within the project <strong>maven-main</strong>.
</li>
<li>A single implementation module, which contains an implementation for <code>MessageService</code> and exports this to Impala's service registry.
This module is contained within the project <strong>maven-module</strong>.</li>
<li>A web host module, which contains <i>WEB-INF/web.xml</i> as well as JSPs. (Impala currently does not support hosting
JSPs within the modules themselves).
This module is contained within the project <strong>maven-host</strong>.
</li>
<li>A web implementation module, which contains Spring MVC controllers, and other web artifacts.
This module is contained within the project <strong>maven-web</strong>.
</li>
</ul>

<p>The example has been built using Maven. Unlike other Impala examples, it does not contain <i>build.xml</i>,
<i>build.properties</i> or <i>dependencies.txt</i>. Instead, each project contains a <i>pom.xml</i> file.
The Maven parent project is contained in the project <strong>maven-build</strong>.
</p>

<p>The Maven example runs in Eclipse without having to run a Maven build. The Eclipse support is added using the 
normal <code>mvn eclipse:eclipse</code> command.</p>

<p><a href = "maven/message.htm">Click here</a> to go into the application.</p>
</body>
</html>