<html>
<title>Hello from Struts</title>
<head></head>
<body>
<H1>Hello from Struts</H1>
<p>Greeting: Hello <%=request.getAttribute("from") %></b>!

<p>This value should carry on incrementing with successive module reloads:</p>
<p>Serialized session value: Hello <%=request.getAttribute("serializedSessionValue") %></p>
<br/>
<p>This value should be reset with successive module reloads, as it is not serializable:</p>
<p>Session value: Hello <%=request.getAttribute("sessionValue") %></p>

<p>Make a change to HelloWorldAction, the reload module using the 
<a href = "http://localhost:8001/invoke?operation=reloadModule&objectname=impala%3Aservice%3DmoduleManagementOperations&value0=struts&type0=java.lang.String">JMX Console</a>, 
then refresh this page.
</p>

</body>
</html>