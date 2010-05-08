<#include "header.ftl"/>

<h2>Technical Notes</h2>

<ul>
<li>
<strong>How does this request get to the module urlmapping-webview?</strong><br/>
 
In <i>web.xml</i>, all requests are mapped to the 
<code>ModuleProxyFilter</code>. <code>ModuleProxyFilter</code>
provides has access to the mechanism by which the request is mapped to the module.
<br/>
Individual modules declare their interest in receiving requests for particular request prefixes. For example, in
<pre>
&lt;web:mapping&gt;
	&lt;web:prefix path = "/webview" setServletPath="true"/&gt;
	...
&lt;/web:mapping&gt;
</pre> 
the <code>url-webview</code> module receives requests with the URL prefix <code>/webview</code>. That is how the request to
<code>/webview/viewMessage.htm</code> reaches <code>urlmapping-webview</code>.
<br/>
<br/>
</li>
<li>
<strong>How does the request get mapped to a particular servlet or filter?</strong>
<br/>
Once in the module, the request is mapped to a particular servlet based on the suffix. 
The module contains two servlet definitions:

<pre>
&lt;web:servlet id = "urlmapping-webview" 
    servletClass = "org.impalaframework.web.spring.servlet.InternalModuleServlet"/&gt;
    
&lt;web:servlet id = "urlmapping-resources" 
    servletClass = "org.springframework.js.resource.ResourceServlet"
    initParameters = "cacheTimeout=10"/&gt;
</pre>

The first definition declares <code>InternalModuleServlet</code>. This servlet is an extension of Spring's 
<code>DispatcherServlet</code>. It is used as the entry point for any Spring controllers defined in the module itself.
The second definition declares the Spring JS <code>ResourceServlet</code>, which is used to serve resources contained in this
module. The resources in our example are limited to CSS resources.
<br/>
<br/>
Mapping to these servlets is done by extension. In this case, requests with the extension <code>htm</code>
are mapped to the dispatcher servlet, and ones with the extension <code>css</code> are mapped to the resource servlet.
<pre>
&lt;web:mapping&gt;
	...
	&lt;web:to-handler extension = "htm" servletName="urlmapping-webview"/&gt;
	&lt;web:to-handler extension = "css" servletName="urlmapping-resources"/&gt;
&lt;/web:mapping&gt;
</pre> 
Note that you can map each extension to one servlet, and as many filters as you like. 
There are two special case values for <code>extension</code>. The value <code>[none]</code> is used to map URLs for paths without a file extension, and 
<code>*</code> is used to map all requests to a particular servlet and/or set of filters.
<br/>
<br/>
</li>

<li>
<strong>What is the purpose of setServletPath?</strong>
<br/>
Because the <code>url-pattern</code> value <code>/*</code> is used to map to the <code>ModuleProxyFilter</code>, the <code>HttpServletRequest</code>
<code>getServletPath()</code> method will return an empty string. 
However, within Impala, the request has been mapped to the module using the path <code>/webview</code>. If <code>setServletPath</code>
is set, Impala will override the default servlet container behaviour, in this case, wrapping the <code>HttpServletRequest</code> with one which 
will return a value of <code>/webview</code> when <code>getServletPath</code>. This allows servlets and controllers to be treated within the module 
as if they had actually be resolved by the servlet container using the servlet path<code>/webview</code>.
<br/>
<br/>
For example, in the controller <code>MessageAdminController</code>, the request is directed to the controller using the mapping 
<code>@RequestMapping("/viewMessage.htm")</code>. Without the <code>setServletPath</code> attribute, the corresponding mapping would need to be
<code>@RequestMapping("/webview/viewMessage.htm")</code>
</li>
</ul>

<#include "footer.ftl"/>