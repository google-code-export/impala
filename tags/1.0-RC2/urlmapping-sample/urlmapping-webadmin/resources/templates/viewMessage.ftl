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

<#include "webadminFooter.ftl"/>