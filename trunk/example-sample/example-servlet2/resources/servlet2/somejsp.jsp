<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<html>
<head>
</head>
<body>
<h1>example-servlet2</h1>
<p>
This example comes from servlet2, which is mapped to the module 'module2' via 
an entry in web.xml. It is mapped using the pattern '*.to'.
to an instance of <strong>org.impalaframework.web.spring.integration.ModuleProxyServlet</strong>
and serviced via <strong>org.springframework.web.servlet.DispatcherServlet</strong>.

The base path <CODE>/servlet2</CODE> maps the request to module2.
</p>

<p>
Note that while <CODE>DispatcherServlet</CODE> is not Impala-aware, any servlet which extends <code>org.springframework.web.servlet.FrameworkServlet</code>
(and this includes <CODE>DispatcherServlet</CODE>) will be automatically adapted to work within an Impala multi-module environment.
</p>

example-web/servlet2/test.jsp<br/>
<b>The value for staticparam: <c:out value="${staticparam}"/></b><br/>
<b>The value for dynamicparam: <c:out value="${dynamicparam}"/></b><br/>
<b>The value for message: <c:out value="${message}"/></b><br/>

<p>Session value sharing: non-prefixed session attributes are only visible to this module.</p>

<b>The value for shared_intvalue: <c:out value="${shared_intvalue}"/></b><br/>
<b>The value for intvalue: <c:out value="${intvalue}"/></b><br/>

</body>
</html>