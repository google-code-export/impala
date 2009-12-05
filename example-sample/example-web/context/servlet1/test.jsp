<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<html>
<head>
</head>
<body>
<h1>example-servlet1</h1>
<p>
This example comes from servlet1, which is mapped to the module 'module1' via 
an entry in web.xml. It is mapped using the pattern '*.do1'.
to an instance of <strong>org.impalaframework.web.spring.servlet.ExternalModuleServlet</strong>.
</p>

example-web/servlet1/test.jsp<br/>
<b>The value for staticparam: <c:out value="${staticparam}"/></b><br/>
<b>The value for dynamicparam: <c:out value="${dynamicparam}"/></b><br/>
<b>The value for shared_intvalue: <c:out value="${shared_intvalue}"/></b><br/>
<b>The value for intvalue: <c:out value="${intvalue}"/></b><br/>
</body>
</html>