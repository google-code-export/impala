<html>
<title></title>
<head></head>
<body>
<H1>Hello from Struts </H1>
<p>Greeting: Hello <%=request.getAttribute("from") %></b>!
<p>Serialized session value: Hello <%=request.getAttribute("serializedSessionValue") %></b>
<p>Session value: Hello <%=request.getAttribute("sessionValue") %></b>
</p>
</body>
</html>