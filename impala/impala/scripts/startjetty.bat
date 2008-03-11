@echo off

if "%1" == "" goto usage
if "%2" == "" goto usage

if "%3" == "" goto syspropfileSet
set SYSPROP=-Dsysprop-resource=%3
:syspropfileSet

@echo on
java %JPDA% %SYSPROP% -jar launcher.jar StartJetty --addclasspath config --addjardir jetty %1 war %2

:usage
echo startjetty [port] [contextpath] [systemproperty file (optional)]
