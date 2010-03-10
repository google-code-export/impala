<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<html>
<head>
</head>
<body>
<h1>example-web</h1>
<p>
This example comes from servlet, which is mapped to the module 'example-web' via 
an entry in web.xml. It is mapped using the pattern '*.do'.
to an instance of <strong>org.impalaframework.web.spring.servlet.ExternalModuleServlet</strong>.
</p>

<p>Note that because the module type is <code>web_root</code>, Spring config resources are loaded as <code>ServletContextResource</code>
instances (that is, from /WEB-INF/example-web-servlet.xml), rather than from the module class path.
</p>

example-web/test.jsp<br/>
<b>The value for hostmessage: <c:out value="${hostmessage}"/></b><br/>
<b>The value for staticparam: <c:out value="${staticparam}"/></b><br/>
<b>The value for dynamicparam: <c:out value="${dynamicparam}"/></b>

</body>
</html>