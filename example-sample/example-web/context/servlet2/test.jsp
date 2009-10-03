<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<html>
<head>
</head>
<body>
<h1>example-servlet2</h1>
<p>
This example comes from servlet1, which is mapped to the module 'module1' via 
an entry in web.xml. It is mapped using the pattern '*.to'.
to an instance of <strong>org.impalaframework.web.spring.integration.ModuleProxyServlet</strong>
and serviced via <strong>org.impalaframework.web.spring.servlet.InternalModuleServlet</strong>.

The base path <CODE>/servlet2</CODE> maps the request to module2.
</p>

example-web/servlet2/test.jsp<br/>
<b>The value for staticparam: <c:out value="${staticparam}"/></b><br/>
<b>The value for dynamicparam: <c:out value="${dynamicparam}"/></b>

</body>
</html>