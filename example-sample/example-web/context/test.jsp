<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<html>
<head>
</head>
<body>
<h1>Module Servlet</h1>
<p>
This example comes from servlet, which is mapped to the module 'example-web' via 
an entry in web.xml. It is mapped using the pattern '*.do'.
to an instance of <strong>org.impalaframework.web.spring.servlet.ExternalModuleServlet</strong>.
</p>

example-web/test.jsp<br/>
<b>The value for staticparam: <c:out value="${staticparam}"/></b><br/>
<b>The value for dynamicparam: <c:out value="${dynamicparam}"/></b>

</body>
</html>