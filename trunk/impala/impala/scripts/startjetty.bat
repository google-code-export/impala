@echo off

if "%1" == "" goto usage
if "%2" == "" goto usage

if "%3" == "" goto syspropfileSet
set SYSPROP=-Dsysprop-resource=%3
:syspropfileSet

if "%4" == "" goto noConfigdirSet
set CONFIGDIR=%4
:noConfigdirSet

if "%CONFIGDIR%" neq "" goto configdirSet
set CONFIGDIR=config
:configdirSet

@echo on
java %JPDA% %SYSPROP% -jar launcher.jar StartJetty --addclasspath %CONFIGDIR% --addjardir jetty %1 war %2
@echo off

:usage
echo startjetty [port] [contextpath] [systemproperty file (optional)] [config directory (optional)]
