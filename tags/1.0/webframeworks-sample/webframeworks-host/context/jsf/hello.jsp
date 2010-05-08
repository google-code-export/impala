<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
 <head>
  <title>enter your name page</title>
 </head>
 <body>
   <f:view>
     <h1>
      <h:outputText value="Header"/>
     </h1>
     <h:form id="helloForm">
      <h:outputText value="Prompt"/>
      <h:inputText value="Person" />
      <h:commandButton action="greeting" value="Text" />
     </h:form>
   </f:view>
 </body>
</html>  
