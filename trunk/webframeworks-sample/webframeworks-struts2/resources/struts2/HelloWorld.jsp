<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
    <head>
        <title>Hello World!</title>
    </head>
    <body>
    	<img src = "../images/struts.gif"/>
        <h2><s:property value="message" /></h2>
        
<p>Make a changsssse to the action class HelloWorld, the reload module using the 
<a href = "http://localhost:8001/invoke?operation=reloadModule&objectname=impala%3Aservice%3DmoduleManagementOperations&value0=struts2&type0=java.lang.String">JMX Console</a>, 
then refresh this page.
</p>

<p>
<s:url id="url" action="struts2/tutorial/LinkPage">
</s:url>
<s:a href="%{url}">Link page</s:a>
</p>

    </body>
</html>
