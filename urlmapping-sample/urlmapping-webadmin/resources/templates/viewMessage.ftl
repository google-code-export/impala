<#include "webadminHeader.ftl"/>

<form method = "post" action = "updateMessage.htm">

<#if result??>
${result}
</#if>

<p>
Update message to be displayed to user.
</p>

<p>
<input type = "text" name = "message" value = "${message}"/>
</p>

<input type = "submit" value = "Update"/>
</form>

<p>
<a href = "../webview/viewMessage.htm">Back to message display screen</a><br/>
<a href = "../webremote/viewMessage.xml">Message returned in XML format</a>
</p>

<hr/>

<a href = "../main/notes.htm">Technical notes for the application</a>

<#include "webadminFooter.ftl"/>