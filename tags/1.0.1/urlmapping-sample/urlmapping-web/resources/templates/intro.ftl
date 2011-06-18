<#include "header.ftl"/>

<h2>Introduction</h2>

<b>Welcome to the Impala URL mapping sample</b>
<br/>
<p>
The purpose of this example is to demonstrate some of the mechanisms available for building fully modular web applications using Impala.
One of the obstacles to modularity of the web tier in a traditional Java web application is the use of the <i>web.xml</i>. 
Impala allows you to reduce your reliance on <i>web.xml</i>. Indeed, in this example, there is just a single filter definition, and no servlet definitions,
in the <i>web.xml</i> document. 

<pre>
&lt;filter&gt;
   &lt;filter-name&gt;web&lt;/filter-name&gt;
   &lt;filter-class&gt;org.impalaframework.web.spring.integration.ModuleProxyFilter&lt;/filter-class&gt;
   &lt;init-param&gt;
           &lt;param-name&gt;modulePrefix&lt;/param-name&gt;
           &lt;param-value&gt;urlmapping-web&lt;/param-value&gt;
   &lt;/init-param&gt;
&lt;/filter&gt;

&lt;filter-mapping&gt;
   &lt;filter-name&gt;web&lt;/filter-name&gt;
   &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
&lt;/filter-mapping&gt;
</pre>

<h3>The URL Mapping Example</h3>

<p>
The URL mapping example consists of a root module <code>urlmapping-root</code>, a root web module <code>urlmapping-web</code>, which itself
has three child modules: <code>urlmapping-view</code>, <code>urlmapping-admin</code> and <code>urlmapping-remote</code>. The application is really
just an extended "Hello World" application. However, it has three web modules which perform different functions in presenting and displaying a 
message to the user.
</p>

<ul>
<li><code>urlmapping-view</code> is a web module which contains a link to display the message to the user.</li>
<li><code>urlmapping-admin</code> is a web module which contains a form for updating the message.</li>
<li><code>urlmapping-remote</code> is a web module which also displays the message to the user. However, in this case, it returns 
the message as an XML document.</li>
</ul>

<p><a href = "../webview/viewMessage.htm">Click here</a> to view message, which shows a page from the <strong>urlmapping-webview</strong> module.</p>
<p><a href = "../webadmin/viewMessage.htm">Click here</a> to update message which displays to you, through a form from the 
<strong>urlmapping-webadmin</strong> module.</p>
<p><a href = "../webremote/viewMessage.xml">Click here</a> to view message in XML format from the <strong>urlmapping-webremote</strong> module.</p>

<#include "footer.ftl"/>