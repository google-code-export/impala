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

<a href = "../webview/viewMessage.htm">Back to message display screen</a>

<#include "webadminFooter.ftl"/>